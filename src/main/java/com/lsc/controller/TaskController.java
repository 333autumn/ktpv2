package com.lsc.controller;

import com.lsc.eneity.Task;
import com.lsc.service.impl.TaskServiceImpl;
import com.lsc.service.impl.UserServiceImpl;
import com.lsc.utils.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/16 14:50
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/task")
public class TaskController {

    private final UserServiceImpl userService;

    private final TaskServiceImpl taskService;

    @PostMapping("/createTask")
    public ResponseResult createTask(@RequestHeader String token, @RequestBody Task task) {
        if (token.length() == 0) {
            log.error("token请求头为空,发布作业失败");
            return ResponseResult.error("未携带token");
        }
        //根据token判断请求用户的身份
        if (!userService.getStatus(token).equals("1")) {
            return ResponseResult.error("只有教师可以发布作业");
        }
        if (taskService.createTask(task)) {
            return ResponseResult.ok("发布作业成功");
        }
        return ResponseResult.error("发布作业失败");
    }

    @GetMapping("/selectAll")
    public ResponseResult selectAll(String courseId) {
        log.info("获取所有作业,courseId==>{}", courseId);
        List<Task> tasks = taskService.selectAll(courseId);
        return ResponseResult.ok("获取所有作业成功", tasks);
    }

}
