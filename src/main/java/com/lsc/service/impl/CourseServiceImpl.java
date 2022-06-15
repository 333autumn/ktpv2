package com.lsc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsc.eneity.Course;
import com.lsc.mapper.CourseMapper;
import com.lsc.service.CourseService;
import org.springframework.stereotype.Service;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/13 15:37
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
}
