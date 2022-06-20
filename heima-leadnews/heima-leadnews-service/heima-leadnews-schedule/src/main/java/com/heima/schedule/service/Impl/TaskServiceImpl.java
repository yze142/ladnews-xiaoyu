package com.heima.schedule.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.model.schedule.dto.Task;
import com.heima.model.schedule.pojos.Taskinfo;
import com.heima.model.schedule.pojos.TaskinfoLogs;
import com.heima.schedule.mapper.TaskinfoLogsMapper;
import com.heima.schedule.mapper.TaskinfoMapper;
import com.heima.schedule.redis.CacheService;
import com.heima.schedule.service.TaskSerivce;

import io.micrometer.core.instrument.binder.BaseUnits;
import org.apache.commons.beanutils.BeanUtilsBean;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @program: heima-leadnews
 * @description: 添加任务到redis
 * @author: hello.xaioyu
 * @create: 2022-06-10 21:06
 **/
@Service
@Transactional
public class TaskServiceImpl implements TaskSerivce {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private TaskinfoLogsMapper taskinfoLogsMapper;

    @Autowired
    private TaskinfoMapper taskinfoMapper;

    public String FUTURE = "future_";   //未来数据key前缀

    public String TOPIC = "topic_";     //当前数据key前缀

    public static final int CANCELLED = 2;   //已取消状态


    @Override
    public long addTask(Task task) {
        //添加到数据库
        boolean success = addTaskDb(task);
        //添加任务到redis
        if (success) {
            addTaskToCache(task);

        }


        return task.getTaskId();
    }

    /**
     * 取消任务
     *
     * @param taskId 任务表id
     * @return
     */
    @Override
    public boolean cancelTask(long taskId) {
        //删除数据库中的任务
        Task task = deleteTask(taskId, 2);

        //删除redis中的任务
        //设置key
        String s = task.getTaskType() + "_" + task.getPriority();

        boolean deletTask = false;
        if (task != null) {

            if (task.getExecuteTime() <= System.currentTimeMillis()) {
                cacheService.lRemove(TOPIC + s, 0, JSON.toJSONString(task));
                deletTask = true;
            } else {

                cacheService.zRemove(FUTURE + s, JSON.toJSONString(task));
                deletTask = true;
            }
        }


        return deletTask;
    }

    /**
     * 消费任务
     *
     * @param taskType 任务类型
     * @param priority 任务优先级
     * @return
     */
    @Override
    public Task consumption(int taskType, int priority) {
        //根据任务的优先级生成键取出并删除list中的值
        String key = taskType + "_" + priority;

        String lRightPop = cacheService.lRightPop(TOPIC + key);
        //取出值然后转换成task然后删除
        Task task = JSON.parseObject(lRightPop, Task.class);
        //数据库删除任务表，并修改状态
        if (task!=null){
            deleteTask(task.getTaskId(), 1); //完成任务就是1}
        }



        return task;
    }

    /**
     * 1 删除数据库里的任务
     *
     * @param taskId
     * @return
     */
    private Task deleteTask(long taskId, int status) {

        taskinfoMapper.deleteById(taskId);
        //删除任务日志表
        TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);
        taskinfoLogs.setStatus(status);
        taskinfoLogs.setTaskId(taskId);
        taskinfoLogsMapper.updateById(taskinfoLogs);
        Task task = new Task();
        BeanUtils.copyProperties(taskinfoLogs, task);
        task.setExecuteTime(taskinfoLogs.getExecuteTime().getTime());

        return task;
    }


    /**
     * 把任务添加到redis中
     *
     * @param task
     */
    private void addTaskToCache(Task task) {
        //如果任务的时间小于当前时间 存入redis
        //设置key
        String s = task.getTaskType() + "_" + task.getPriority();
        //获取五分钟后的时间 毫秒值
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, 5);
        long nextScheduleTime = instance.getTimeInMillis();

        if (task.getExecuteTime() <= System.currentTimeMillis()) {

            cacheService.lLeftPush(TOPIC + s, JSON.toJSONString(task));
        } else if (task.getExecuteTime() <= nextScheduleTime) {
            //发布的任务大于当前时间，小于等于预设时间
            cacheService.zAdd(FUTURE + s, JSON.toJSONString(task), task.getExecuteTime());

        }

        //如果任务的时间大于预设的时间（五分钟）存入zset中
    }

    /**
     * 添加任务到数据库中
     *
     * @param task
     * @return
     */
    private boolean addTaskDb(Task task) {
        Taskinfo taskinfo = new Taskinfo();
        //保存到任务表
        BeanUtils.copyProperties(task, taskinfo);
        taskinfo.setExecuteTime(new Date(task.getExecuteTime()));
        taskinfoMapper.insert(taskinfo);

        task.setTaskId(taskinfo.getTaskId());
        //保存任务日志数据
        TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
        BeanUtils.copyProperties(taskinfo, taskinfoLogs);
        taskinfoLogs.setVersion(1);
        taskinfoLogs.setStatus(0);
        taskinfoLogsMapper.insert(taskinfoLogs);
        return true;
    }


    /**
     * 定时任务
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh() {

        String future_task_sync = cacheService.tryLock("FUTURE_TASK_SYNC", 1000 * 30);

        if (future_task_sync != null) {
            System.out.println(System.currentTimeMillis() / 1000 + "执行了定时任务");
            // 获取所有未来数据集合的key值
            Set<String> scan = cacheService.scan(FUTURE + "*");
            //遍历键
            for (String scanSet : scan) {
                String topicKey = TOPIC + scanSet.split(FUTURE)[1];
                //查询的是min是最小值——到最大值的数据 范围查询
                Set<String> tasks = cacheService.zRangeByScore(scanSet, 0, System.currentTimeMillis());
                if (!tasks.isEmpty()) {
                    //将这些任务数据添加到消费者队列中
                    cacheService.refreshWithPipeline(scanSet, topicKey, tasks);
                    System.out.println("成功的将" + scanSet + "下的当前需要执行的任务数据刷新到" + topicKey + "下");
                }
            }
        }
    }

    /**
     * 定时同步数据到redis进行消费
     */
    @Scheduled(cron = "0 */5 * * * ?")
    @PostConstruct
    public void SyncRedis() {
        //1.删除原本redis缓存中的数据
        Set<String> futureSetKeys = cacheService.scan(FUTURE + "*");

        Set<String> topicSetKeys = cacheService.scan(TOPIC + "*");
        cacheService.delete(futureSetKeys);
        cacheService.delete(topicSetKeys);

        //2.查询数据库中的数据
        System.out.println("数据库同步到缓存" );
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);

        //查看小于未来5分钟的所有任务
        List<Taskinfo> allTasks = taskinfoMapper.selectList
                (Wrappers.<Taskinfo>lambdaQuery().lt(Taskinfo::getExecuteTime,calendar.getTime()));

        //3.调用方法进行数据同步
        for (Taskinfo allTask : allTasks) {
            Task task=new Task();
            BeanUtils.copyProperties(allTask,task);
            task.setExecuteTime(allTask.getExecuteTime().getTime());
            addTaskToCache(task);
        }

    }


}
