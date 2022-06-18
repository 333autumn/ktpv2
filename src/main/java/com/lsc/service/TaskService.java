package com.lsc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lsc.eneity.Task;

import java.util.List;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 15:10
 */
public interface TaskService extends IService<Task> {
    boolean createTask(Task task);

    List<Task> selectAll(String courseId);
}
