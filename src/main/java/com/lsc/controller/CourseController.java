package com.lsc.controller;

import com.lsc.eneity.Course;
import com.lsc.service.impl.CourseServiceImpl;
import com.lsc.utils.ResponseResult;
import com.lsc.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/13 21:39
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/course")
public class CourseController {

    private final CourseServiceImpl courseService;

    /**
     * 返回所有教师创建的课程和加入的课程（课程状态为正常）
     */
    @GetMapping("/teacher/selectAllNormalCourses")
    public ResponseResult selectAllNormalCourses(@RequestHeader String token) {
        //从token中获取用户id
        String id = TokenUtils.getUserId(token);
        try {
            List<Course> courses = courseService.selectAllNormalCourses(id);
            return ResponseResult.ok(courses);
        } catch (RuntimeException e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 返回所有教师归档的课程
     */
    @GetMapping("/teacher/selectAllArchiveCourses")
    public ResponseResult selectAllArchiveCourses(@RequestHeader String token) {
        //从token中获取用户id
        String id = TokenUtils.getUserId(token);
        try {
            List<Course> courses = courseService.selectAllArchiveCourses(id);
            return ResponseResult.ok(courses);
        } catch (RuntimeException e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 编辑课程
     */
    @PostMapping("/editCourses")
    public ResponseResult editCourses(@RequestBody Course course) {
        if (courseService.updateById(course)) {
            return ResponseResult.ok("编辑课程成功");
        }
        return ResponseResult.error("编辑课程失败");
    }

    /**
     * 删除课程
     */
    @PostMapping("/deleteCourse")
    public ResponseResult deleteCourse(@RequestBody Course course) {
        if (courseService.removeById(course)) {
            return ResponseResult.ok("删除课程成功");
        }
        return ResponseResult.error("删除课程失败");
    }

    /**
     * 课程归档
     */
    @PostMapping("/archiveCourse")
    public ResponseResult archiveCourse(@RequestParam("courseId") String courseId) {
        try {
            courseService.archiveCourse(courseId);
        } catch (RuntimeException e) {
            return ResponseResult.error("课程归档失败");
        }
        return ResponseResult.ok("课程归档成功");
    }

    /**
     * 查询所有课程成员
     */
    @GetMapping("selectAllMembers")
    public ResponseResult selectAllMembers(@RequestParam("courseId") String courseId) {
        return courseService.selectAllMembers(courseId);
    }

    /**
     * 教师创建课程
     * 返回创建后课程的信息
     */
    @PostMapping("addCourse")
    public ResponseResult addCourse(@RequestBody Course course) {
        Course data = courseService.addCourse(course);
        if (data != null) {//新增课程成功
            return ResponseResult.ok("新增课程成功", data);
        }
        return ResponseResult.error("新增课程失败");
    }

}
