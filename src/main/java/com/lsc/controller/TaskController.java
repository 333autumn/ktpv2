package com.lsc.controller;

import com.lsc.eneity.Task;
import com.lsc.eneity.TaskVo;
import com.lsc.service.impl.AnnexServiceImpl;
import com.lsc.service.impl.TaskServiceImpl;
import com.lsc.service.impl.UserServiceImpl;
import com.lsc.utils.ResponseResult;
import com.lsc.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    private final AnnexServiceImpl annexService;

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
    public ResponseResult selectAll(@RequestParam String courseId, @RequestHeader String token) {
        if (token.length() == 0) {
            return ResponseResult.error("请求未携带token");
        }
        String userId= TokenUtils.getUserId(token);
        log.info("获取所有作业,courseId==>{},userId==>{}", courseId,userId);
        List<TaskVo> tasks = taskService.selectAll(courseId,userId);
        return ResponseResult.ok("获取所有作业成功", tasks);
    }


    /**
     * 提交作业
     */
    @PostMapping("/submit")
    public ResponseResult submit(MultipartFile file,@RequestParam String taskId,@RequestParam String userId,
                                 @RequestParam String remarks){
        annexService.submit(file,taskId,userId,remarks);
        //提交作业成功后,当前作业的提交数量+1
        taskService.addSubmitNum(taskId);
        return ResponseResult.ok("提交作业成功");
    }

    /**
     * 通过作业id查询作业详情
     */
    @GetMapping("/selectById")
    public ResponseResult selectById(String taskId){
        Task task=taskService.getById(taskId);
        return ResponseResult.ok("作业查询成功",task);
    }

}
