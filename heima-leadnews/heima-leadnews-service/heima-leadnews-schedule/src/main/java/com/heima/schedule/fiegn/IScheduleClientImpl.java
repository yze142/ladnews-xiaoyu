package com.heima.schedule.fiegn;

import com.heima.apis.schedule.IScheduleClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.schedule.dto.Task;
import com.heima.schedule.service.TaskSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: heima-leadnews
 * @description: 延迟发布远程调用
 * @author: hello.xaioyu
 * @create: 2022-06-12 16:14
 **/
@RestController
public class IScheduleClientImpl implements IScheduleClient {

    @Autowired
    private TaskSerivce taskSerivce;

    /**
     * 添加任务
     * @param task   任务对象
     * @return       任务id
     */
    @Override
    @PostMapping("/api/v1/task/add")
    public ResponseResult addTask(@RequestBody Task task) {
        return ResponseResult.okResult(taskSerivce.addTask(task));
    }

    /**
     * 取消任务
     * @param taskId 任务id
     * @return 取消结果
     */
    @GetMapping("/api/v1/task/cancel/{taskId}")
    @Override
    public ResponseResult cancelTask(@PathVariable("taskId") long taskId) {
        return ResponseResult.okResult(taskSerivce.cancelTask(taskId));
    }

    /**
     * 按照类型和优先级来拉取任务
     * @param type
     * @param priority
     * @return
     */
    @GetMapping("/api/v1/task/poll/{type}/{priority}")
    @Override
    public ResponseResult poll(@PathVariable("type") int type,@PathVariable("priority")  int priority){

        return ResponseResult.okResult(taskSerivce.consumption(type,priority));
    }
}
