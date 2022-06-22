package com.lsc.controller;

import com.alibaba.fastjson.JSON;
import com.lsc.eneity.Task;
import com.lsc.eneity.TaskDetailsVo;
import com.lsc.eneity.TaskListVo;
import com.lsc.service.impl.AnnexServiceImpl;
import com.lsc.service.impl.TaskServiceImpl;
import com.lsc.service.impl.UserServiceImpl;
import com.lsc.utils.ResponseResult;
import com.lsc.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

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

    /**
     * 教师发布作业
     */
    @PostMapping("/createTask")
    public ResponseResult createTask(@RequestHeader String token,@RequestPart MultipartFile file,@RequestParam String courseId,
                                     @RequestParam String taskName,@RequestParam String releaseTime,@RequestParam String cutOffTime,
                                     @RequestParam String remarks) {
        if (token.length() == 0) {
            log.error("token请求头为空,发布作业失败");
            return ResponseResult.error("未携带token");
        }
        //根据token判断请求用户的身份
        if (!userService.getStatus(token).equals("1")) {
            return ResponseResult.error("只有教师可以发布作业");
        }

        //获取教师id
        String userId=TokenUtils.getUserId(token);
        //封装task对象
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd");
        Task task=new Task();
        task.setCourseId(courseId);
        task.setTaskName(taskName);
        try {
            task.setReleaseTime(simpleDateFormat.parse(releaseTime));
            task.setCutOffTime(simpleDateFormat.parse(cutOffTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        task.setRemarks(remarks);

        log.info("发布作业,用户id==>{}",userId);
        log.info("发布作业,作业信息==>{}", JSON.toJSONString(task));
        log.info("发布作业,上传的原始文件名==>{}",file.getOriginalFilename());

        if (taskService.createTask(task,file,userId)) {
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
        List<TaskListVo> tasks = taskService.selectAll(courseId,userId);
        return ResponseResult.ok("获取所有作业成功", tasks);
    }


    /**
     * 提交作业
     */
    @PostMapping("/submit")
    public ResponseResult submit(MultipartFile file,@RequestParam String taskId,@RequestParam String userId,
                                 @RequestParam String remarks){
        if (annexService.submit(file,taskId,userId,remarks)){
            //返回结果为false说明作业是更新不是新增,提交数量不用+1
            //提交作业成功后,当前作业的提交数量+1
            taskService.addSubmitNum(taskId);
        }
        return ResponseResult.ok("提交作业成功");
    }

    /**
     * 通过作业id查询作业详情
     */
    @GetMapping("/selectById")
    public ResponseResult selectById(String taskId){
        TaskDetailsVo task=taskService.getDetailsVoById(taskId);
        return ResponseResult.ok("作业查询成功",task);
    }

    /**
     * 获取指定作业下的所有成绩
     */
    @GetMapping("/getAllGrades")
    public ResponseResult getAllGrades(String taskId){
        if (Objects.isNull(taskId)){
            return ResponseResult.error("传入参数为空");
        }
        //根据作业id找到教师id
        String userId=taskService.getUserIdByTaskId(taskId);

        log.info("获取指定作业下的所有成绩,courseId==>{},userId==>{}",taskId,userId);
        return taskService.getAllGrades(taskId,userId);
    }

}
