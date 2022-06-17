package com.lsc.controller;

import com.lsc.eneity.Course;
import com.lsc.service.impl.CourseServiceImpl;
import com.lsc.utils.Constant;
import com.lsc.utils.ResponseResult;
import com.lsc.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

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
     * 返回所有用户创建的课程和加入的课程（课程状态为正常）
     */
    @GetMapping("/selectAllNormalCourses")
    public ResponseResult selectAllNormalCourses(@RequestParam String userId) {
        if (Objects.isNull(userId)) {
            return ResponseResult.error("传入参数为空");
        }
        log.info("获取所有课程传入userId==>{}", userId);
        try {
            List<Course> courses = courseService.selectAllCourses(userId, Constant.CourseStatus.NORMAL);
            log.info("获取所有课程成功");
            return ResponseResult.ok(courses);
        } catch (RuntimeException e) {
            log.error("获取所有课程失败,错误信息==>{}", e.getMessage());
            return ResponseResult.error(e.getMessage());
        }
    }

    /**
     * 返回所有教师归档的课程
     */
    @GetMapping("/selectAllArchiveCourses")
    public ResponseResult selectAllArchiveCourses(@RequestHeader String token) {
        //从token中获取用户id
        String id = TokenUtils.getUserId(token);
        try {
            List<Course> courses = courseService.selectAllCourses(id, Constant.CourseStatus.ARCHIVE);
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
    @GetMapping("/selectAllMembers")
    public ResponseResult selectAllMembers(@RequestParam String courseId) {
        if (Objects.isNull(courseId)) {
            return ResponseResult.error("传入参数为空");
        }
        log.info("查询所有课程成员:courseId==>{}", courseId);
        return courseService.selectAllMembers(courseId);
    }

    /**
     * 教师创建课程
     * 返回创建后课程的信息
     * 默认教师加入该课程
     */
    @PostMapping("/createCourse")
    public ResponseResult createCourse(@RequestBody Course course) {
        Course data = courseService.createCourse(course);
        if (data != null) {//新增课程成功
            return ResponseResult.ok("创建课程成功", data);
        }
        return ResponseResult.error("创建课程失败");
    }

    /**
     * 加入课程
     */
    @PostMapping("/joinCourse")
    public ResponseResult joinCourse(@RequestParam String courseId, @RequestParam String userId) {
        if (Objects.isNull(courseId) || Objects.isNull(userId)) {
            return ResponseResult.error("传入参数为空");
        }
        log.info("加入课程传入参数:courseId==>{},userId==>{}", courseId, userId);
        try {
            courseService.joinCourse(courseId, userId);
            log.info("加入课程成功");
            return ResponseResult.ok("加入课程成功");
        } catch (RuntimeException e) {
            log.error("加入课程发生错误,错误信息==>{}", e.getMessage());
            return ResponseResult.error(e.getMessage());
        }
    }
}
