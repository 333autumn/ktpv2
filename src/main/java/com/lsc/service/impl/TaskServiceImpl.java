package com.lsc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsc.eneity.Task;
import com.lsc.mapper.TaskMapper;
import com.lsc.service.TaskService;
import org.springframework.stereotype.Service;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 15:10
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
}
