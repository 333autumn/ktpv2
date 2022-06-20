package com.lsc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lsc.eneity.Grade;
import com.lsc.eneity.Task;
import com.lsc.eneity.TaskVo;

import java.util.List;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 15:10
 */
public interface TaskService extends IService<Task> {
    boolean createTask(Task task);

    List<TaskVo> selectAll(String courseId, String userId);

    boolean correct(Grade grade);

    void addSubmitNum(String taskId);
}
