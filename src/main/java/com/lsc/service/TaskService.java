package com.lsc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lsc.eneity.Grade;
import com.lsc.eneity.Task;
import com.lsc.eneity.TaskDetailsVo;
import com.lsc.eneity.TaskListVo;
import com.lsc.utils.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 15:10
 */
public interface TaskService extends IService<Task> {
    boolean createTask(Task task, MultipartFile file,String userId);

    List<TaskListVo> selectAll(String courseId, String userId);

    boolean correct(Grade grade);

    void addSubmitNum(String taskId);

    TaskDetailsVo getDetailsVoById(String taskId);

    ResponseResult getAllGrades(String taskId,String userId);

    String getUserIdByTaskId(String taskId);
}
