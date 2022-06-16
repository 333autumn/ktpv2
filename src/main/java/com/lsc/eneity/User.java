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
 * @date 2022/6/12 15:12
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {
    @TableId("user_id")
    private String userId;
    private String username;
    private String password;
    private String name;
    private Integer age;
    private String gender;
    private String nickname; //用户昵称
    private String avatar; //用户头像
    private String stno; //教师工号或学生学号
    private String schoolId; //所属学校id
    private String status; //用户身份,0代表学生,1代表老师
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
