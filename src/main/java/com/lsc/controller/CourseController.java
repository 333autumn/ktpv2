package com.lsc.controller;

import com.lsc.utils.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 返回用户的所有课程信息
     */
    @GetMapping("/selectAllCourses")
    public ResponseResult selectAllCourses(@RequestHeader("token") String token){
        //从token中获取用户信息
        return null;
    }

}
