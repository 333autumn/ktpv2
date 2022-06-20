package com.lsc.controller;

import com.lsc.eneity.Grade;
import com.lsc.service.impl.GradeServiceImpl;
import com.lsc.service.impl.TaskServiceImpl;
import com.lsc.service.impl.UserServiceImpl;
import com.lsc.utils.ResponseResult;
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

    private final UserServiceImpl userService;

    private final TaskServiceImpl taskService;

    /**
     * 教师批改作业
     * 需要token
     */
    @PostMapping("/correct")
    public ResponseResult correct(@RequestBody Grade grade, @RequestHeader String token) {
        if (token.length() == 0) {
            log.error("教师批改作业,未携带token");
            return ResponseResult.error("未携带token");
        }
        //判断用户是否是教师
        //根据token判断请求用户的身份
        if (!userService.getStatus(token).equals("1")) {
            return ResponseResult.error("只有教师可以批改作业");
        }
        if (taskService.correct(grade)) {
            return ResponseResult.ok("批改作业成功");
        }
        return ResponseResult.error("批改作业失败");
    }

}
