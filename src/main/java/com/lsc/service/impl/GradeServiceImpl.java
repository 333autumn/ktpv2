package com.lsc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsc.eneity.Grade;
import com.lsc.eneity.Task;
import com.lsc.mapper.GradeMapper;
import com.lsc.service.GradeService;
import com.lsc.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 15:12
 */
@Service
@RequiredArgsConstructor
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {


}
