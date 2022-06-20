package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.heima.apis.schedule.IScheduleClient;
import com.heima.emus.TaskTypeEnum;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.schedule.dto.Task;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.utils.common.ProtostuffUtil;
import com.heima.wemedia.service.WmNewsAutoScanService;
import com.heima.wemedia.service.WmNewsTaskService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {

    @Autowired
    private IScheduleClient iScheduleClient;


    /**
     * 添加任务到延迟队列中
     *
     * @param id          文章的id
     * @param publishTime 发布的时间  可以做为任务的执行时间
     */
    @Override
    public void addNewsToTask(Integer id, Date publishTime) {
        try {
            //填充参数
            Task task = new Task();
            task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
            task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
            task.setExecuteTime(publishTime.getTime());//毫米值
            WmNews wmNews = new WmNews();
            wmNews.setId(id);
            OutputStream fileOutputStream = new FileOutputStream(String.valueOf(wmNews));
            //利用谷歌开源的工具把他序列化
            task.setParameters(ProtostuffUtil.serialize(wmNews));//参数
            //调用方法完成添加
            iScheduleClient.addTask(task);
            log.info("已经把任务添加到延迟任务队列");
        } catch (IOException e) {
            log.error("添加到延迟任务队列失败");
        }

    }

    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    /**
     * W
     * 消费延迟队列
     */
    @Scheduled(fixedRate = 1000)
    @Override
    @SneakyThrows
    public void scanNewsByTask() {
        ResponseResult responseResult = iScheduleClient.poll(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(),
                TaskTypeEnum.NEWS_SCAN_TIME.getPriority());

        if (responseResult.getCode().equals(200)&& responseResult.getData() != null) {
            Object data = responseResult.getData();
            //把data转换成json字符串再转换成对象
            String toJSONString = JSON.toJSONString(data);
            Task tasks = JSON.parseObject(toJSONString, Task.class);
            byte[] parameters = tasks.getParameters();
            //解析对象得到id（反序列化）
            WmNews deserialize = ProtostuffUtil.deserialize(parameters, WmNews.class);
            Integer id = deserialize.getId();
            //调用方法进行审核
            wmNewsAutoScanService.autoScanWmNews(id);
        }


    }

}

