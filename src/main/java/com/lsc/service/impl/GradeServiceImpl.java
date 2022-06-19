package com.lsc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsc.eneity.Grade;
import com.lsc.eneity.Task;
import com.lsc.mapper.GradeMapper;
import com.lsc.service.GradeService;
import com.lsc.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 15:12
 */
@Service
@RequiredArgsConstructor
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {

    private final TaskServiceImpl taskService;

    /**
     * 批改作业
     */
    @Override
    public boolean correct(Grade grade) {
        grade.setCreateTime(DateUtils.now());
        grade.setUpdateTime(DateUtils.now());
        if (save(grade)){
            //批改作业成功,作业的批改人数+1
            String taskId=grade.getTaskId();
            //根据作业id查找作业

            Task task=taskService.getById(taskId);

            task.setCorrectNum(task.getCorrectNum()+1);

            taskService.updateById(task);
            return true;
        }
        return false;
    }
}
