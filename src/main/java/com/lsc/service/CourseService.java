package com.lsc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lsc.eneity.Course;
import com.lsc.utils.ResponseResult;

import java.util.List;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/13 15:35
 */
public interface CourseService extends IService<Course> {

    List<Course> selectAllNormalCourses(String id);

    List<Course> selectAllArchiveCourses(String id);

    void archiveCourse(String courseId);

    ResponseResult selectAllMembers(String courseId);

    Course addCourse(Course course);

    boolean isRepeatedCode(String addCourseCode);
}
