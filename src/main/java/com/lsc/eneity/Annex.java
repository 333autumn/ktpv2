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
 * @date 2022/6/13 17:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("annex")
public class Annex {
    @TableId("annex_id")
    private String annexId; //附件id
    private String taskId; //作业id
    private String ownerId; //所有者id
    private String path; //附件内容地址
    private String remarks;  //详情
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}

