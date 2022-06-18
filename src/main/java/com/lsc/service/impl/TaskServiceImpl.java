package com.lsc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsc.eneity.Task;
import com.lsc.mapper.TaskMapper;
import com.lsc.service.TaskService;
import com.lsc.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 15:10
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    /**
     * 发布作业
     */
    @Override
    public boolean createTask(Task task) {
        //设置提交数量
        task.setSubmitNum(0);
        //设置已批改数量
        task.setCorrectNum(0);
        //设置创建时间
        task.setCreateTime(DateUtils.now());
        //设置更新时间
        task.setUpdateTime(DateUtils.now());
        //保存作业信息
        return save(task);
    }

    //获取课程下的所有作业信息
    @Override
    public List<Task> selectAll(String courseId) {
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Task::getCourseId, courseId);
        return list(queryWrapper);
    }

}
