package com.lsc.controller;

import com.lsc.eneity.School;
import com.lsc.service.impl.SchoolServiceImpl;
import com.lsc.utils.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
    public ResponseResult saveSchool(@RequestParam String schoolName){
        if (Objects.isNull(schoolName)){
            log.error("新增学校传入参数为空");
            return ResponseResult.error("传入参数为空");
        }
        log.info("新增学校,schoolName==>{}",schoolName);
        try {
            schoolService.add(schoolName);
        }catch (RuntimeException e){
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.ok("新增学校成功");
    }

    @GetMapping("/selectAll")
    public ResponseResult selectAll(){
        List<School> schools=schoolService.list();
        return ResponseResult.ok("查询所有学校成功",schools);
    }

    @GetMapping("/isRepeat")
    public boolean isRepeat(String schoolName){
        if (Objects.isNull(schoolName)){
            log.error("查询学校是否重复传入参数为空");
            return true;
        }
        log.info("查询学校是否重复,schoolName==>{}",schoolName);
       return schoolService.isRepeat(schoolName);
    }

}
