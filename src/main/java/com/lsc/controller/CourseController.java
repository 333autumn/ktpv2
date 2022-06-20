package com.lsc.controller;

import com.lsc.eneity.Course;
import com.lsc.service.impl.CourseServiceImpl;
import com.lsc.service.impl.UserServiceImpl;
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

    private final UserServiceImpl userService;

    /**
     * 返回所有用户创建的课程和加入的课程（课程状态为正常）
     */
    @GetMapping("/selectAllNormalCourses")
    public ResponseResult selectAllNormalCourses(@RequestParam String userId) {
        if (Objects.isNull(userId)) {
            return ResponseResult.error("传入参数为空");
        }
        log.info("获取所有课程传入userId==>{}", userId);
        List<Course> courses = courseService.selectAllCourses(userId, Constant.CourseStatus.NORMAL);
        log.info("获取所有课程成功");
        return ResponseResult.ok(courses);
    }

    /**
     * 返回所有用户归档的课程
     */
    @GetMapping("/selectAllArchiveCourses")
    public ResponseResult selectAllArchiveCourses(@RequestParam String userId) {
        List<Course> courses = courseService.selectAllCourses(userId, Constant.CourseStatus.ARCHIVE);

        return ResponseResult.ok(courses);
    }

    /**
     * 编辑课程
     * 只有教师可以编辑课程
     */
    @PostMapping("/editCourse")
    public ResponseResult editCourses(@RequestBody Course course, @RequestHeader String token) {
        if (token.length() == 0) {
            return ResponseResult.error("请求未携带token");
        }
        //根据token判断请求用户的身份
        if (!userService.getStatus(token).equals("1")) {
            return ResponseResult.error("只有教师可以编辑课程");
        }
        if (courseService.updateById(course)) {
            return ResponseResult.ok("编辑课程成功");
        }
        return ResponseResult.error("编辑课程失败");
    }

    /**
     * 删除课程
     * 教师可以删除自己创建和加入的课程
     */
    @PostMapping("/deleteCourse")
    public ResponseResult deleteCourse(@RequestParam String courseId, @RequestHeader String token) {
        if (token.length() == 0) {
            return ResponseResult.error("请求未携带token");
        }
        if (Objects.isNull(courseId)) {
            log.error("删除课程传入参数为空");
            return ResponseResult.error("传入参数为空");
        }
        //根据token获取用户id
        String userId = TokenUtils.getUserId(token);
        log.info("删除课程,courseId==>{},userId==>{}", courseId, userId);

        courseService.deleteCourse(courseId, userId);
        return ResponseResult.ok("删除课程成功");

    }

    /**
     * 课程归档自己
     */
    @PostMapping("/archiveCourse")
    public ResponseResult archiveCourse(@RequestParam String courseId, @RequestParam String userId) {

        courseService.archiveCourse(courseId, userId);

        return ResponseResult.ok("课程归档成功");
    }

    /**
     * 课程归档全部
     * 加入该课程的所有人的关联表里的状态改为归档
     * 只有教师可以归档全部
     */
    @PostMapping("/archiveCourseAll")
    public ResponseResult archiveCourseAll(@RequestParam String courseId, @RequestHeader String token) {
        if (token.length() == 0) {
            return ResponseResult.error("请求未携带token");
        }
        //根据token判断用户是否是教师
        if (!userService.getStatus(token).equals("1")) {
            return ResponseResult.error("只有教师可以归档全部");
        }

        courseService.archiveCourseALL(courseId);

        return ResponseResult.ok("课程归档全部成功");
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
    public ResponseResult createCourse(@RequestBody Course course, @RequestHeader String token) {
        if (token.length() == 0) {
            return ResponseResult.error("请求未携带token");
        }
        //根据token判断请求用户的身份
        if (!userService.getStatus(token).equals("1")) {
            return ResponseResult.error("只有教师可以创建课程");
        }
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
    public ResponseResult joinCourse(@RequestParam String addCourseCode, @RequestParam String userId) {
        if (Objects.isNull(addCourseCode) || Objects.isNull(userId)) {
            return ResponseResult.error("传入参数为空");
        }
        log.info("加入课程传入参数:addCourseCode==>{},userId==>{}", addCourseCode, userId);
        courseService.joinCourse(addCourseCode, userId);
        log.info("加入课程成功");
        return ResponseResult.ok("加入课程成功");
    }

    /**
     * 退出课程
     * 学生退课
     */
    @PostMapping("/exitCourse")
    public ResponseResult exitCourse(@RequestParam String courseId, @RequestHeader String token) {
        if (token.length() == 0) {
            return ResponseResult.error("未携带token");
        }
        log.info("学生退课,token==>{}",token);
        String userId = TokenUtils.getUserId(token);
        courseService.exitCourse(courseId, userId);
        return ResponseResult.ok("退课成功");
    }

    /**
     * 恢复归档的课程
     */
    @PostMapping("/recoverCourse")
    public ResponseResult recoverCourse(@RequestParam String courseId,@RequestParam String userId) {
        log.info("恢复归档的课程,courseId==>{},userId==>{}", courseId, userId);

        courseService.recoverCourse(courseId, userId);

        return ResponseResult.ok("恢复归档课程成功");
    }

    /**
     * 通过课程id获取课程信息
     */
    @GetMapping("/selectByCourseId")
    public ResponseResult selectByCourseId(String courseId){
        return courseService.selectByCourseId(courseId);
    }

}
