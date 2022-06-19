package com.lsc.controller;

import com.lsc.eneity.Grade;
import com.lsc.service.impl.GradeServiceImpl;
import com.lsc.utils.ResponseResult;
import com.lsc.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/16 14:50
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/grade")
public class GradeController {
    private final GradeServiceImpl gradeService;

    /**
     * 教师批改作业
     * 需要token
     */
    @PostMapping("/correct")
    public ResponseResult correct(@RequestBody Grade grade, @RequestHeader String token){
        if (token.length()==0){
            log.error("教师批改作业,未携带token");
            return ResponseResult.error("未携带token");
        }
        String userId= TokenUtils.getUserId(token);
        //判断用户是否是教师
        //TODO 封装一下
        if (gradeService.correct(grade)){
            return ResponseResult.ok("批改作业成功");
        }
        return ResponseResult.error("批改作业失败");
    }

}
