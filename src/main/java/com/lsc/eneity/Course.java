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
 * @date 2022/6/13 15:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("course")
public class Course {
    @TableId("course_id")
    private String courseId;
    private String courseName;
    private String semester; //学期
    private String semesterYear; //学年
    private String ownerId; //创建人id
    private Integer courseStatus; //课程状态0->正常 1->归档 2->删除
    private Integer studentNum; //学生数量
    private String addCourseCode; //8位加课码
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
