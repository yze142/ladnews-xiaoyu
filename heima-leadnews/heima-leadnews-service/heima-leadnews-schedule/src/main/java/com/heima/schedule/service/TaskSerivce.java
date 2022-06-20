package com.heima.schedule.service;


import com.heima.model.schedule.dto.Task;

public interface TaskSerivce {

    /**
     * 把任务加到redis中
     */

        public long addTask(Task task);

    /**
     * 取消任务
     */
    public boolean cancelTask(long taskId);

    /**
     * 消费任务 根据任务的优先级和类型
     */
 public Task consumption(int taskType,int priority );

}
