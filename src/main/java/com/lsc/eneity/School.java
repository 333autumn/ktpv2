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
 * @date 2022/6/13 14:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("school")
public class School {
    @TableId("school_id")
    private String schoolId;
    private String schoolName; //学校名
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
