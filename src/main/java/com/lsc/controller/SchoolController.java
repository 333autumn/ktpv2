package com.lsc.controller;

import com.lsc.service.impl.SchoolServiceImpl;
import com.lsc.utils.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 21:47
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/school")
public class SchoolController {

    private final SchoolServiceImpl schoolService;

    @PostMapping("/add")
    public ResponseResult saveSchool(@RequestParam("schoolName") String schoolName){
        try {
            schoolService.add(schoolName);
        }catch (RuntimeException e){
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.ok("新增学校成功");
    }

}
