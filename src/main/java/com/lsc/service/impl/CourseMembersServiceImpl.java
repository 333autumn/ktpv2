package com.lsc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsc.eneity.CourseMembers;
import com.lsc.mapper.CourseMembersMapper;
import com.lsc.service.CourseMembersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/13 15:37
 */
@Service
@RequiredArgsConstructor
public class CourseMembersServiceImpl extends ServiceImpl<CourseMembersMapper, CourseMembers> implements CourseMembersService {


}
