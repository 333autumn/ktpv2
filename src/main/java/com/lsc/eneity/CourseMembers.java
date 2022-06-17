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
 * @date 2022/6/13 15:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("course_members")
public class CourseMembers {
    @TableId("id")
    private String id;
    private String courseId; //课程号
    private String userId; //用户id
    private String status; //0代表学生,1代表老师
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
