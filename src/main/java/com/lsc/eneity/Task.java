package com.lsc.eneity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/13 17:00
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("task")
public class Task {
    @TableId("task_id")
    private String taskId;
    private String courseId; //课程id
    private String taskName; //作业名
    private Date releaseTime; //发布时间
    private Date cutOffTime; //截止时间
    private Integer submitNum; //提交的作业数量
    private String remarks; //作业描述
    private Integer correctNum; //批改数量
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
