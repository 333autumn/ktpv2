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
@TableName("course_students")
public class CourseMembers {
    @TableId("id")
    private String id;
    private String courseId; //课程号
    private String stNo; //学生学号或者教师工号
    private Integer courseMembersStatus; //课程状态 0->正常 1->归档 2->退课
    private Integer status; //0代表学生,1代表老师
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
