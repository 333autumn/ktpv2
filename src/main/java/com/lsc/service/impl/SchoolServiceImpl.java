package com.lsc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsc.eneity.School;
import com.lsc.mapper.SchoolMapper;
import com.lsc.service.SchoolService;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/13 15:36
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements SchoolService {
    /**
     * 新增学校
     */
    @Override
    public void add(String schoolName) {
        //判断学校名是否重复
        LambdaQueryWrapper<School> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(School::getSchoolName,schoolName);
        if (getOne(queryWrapper)!=null){
            throw new RuntimeException("学校名不能重复");
        }
        //封装school对象
        School school=new School();
        school.setSchoolName(schoolName);
        school.setCreateTime(new Date(System.currentTimeMillis()));
        school.setUpdateTime(new Date(System.currentTimeMillis()));
        //保存学校
        save(school);
    }
}
