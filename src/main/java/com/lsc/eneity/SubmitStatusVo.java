package com.lsc.eneity;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/22 14:47
 */

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 查询某项作业下的提交情况
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitStatusVo {

    private String studentId;  //学生id

    private String stno;  //学号

    private String name;  //学生姓名

    private String score;  //分数

    private String path; //附件路径

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date summitTime;  //提交时间(附件创建时间)

}
