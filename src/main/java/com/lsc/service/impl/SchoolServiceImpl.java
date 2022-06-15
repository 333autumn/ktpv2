package com.lsc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsc.eneity.School;
import com.lsc.mapper.SchoolMapper;
import com.lsc.service.SchoolService;
import org.springframework.stereotype.Service;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/13 15:36
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements SchoolService {
}
