package com.lsc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsc.eneity.Course;
import com.lsc.eneity.CourseMembers;
import com.lsc.eneity.User;
import com.lsc.mapper.CourseMapper;
import com.lsc.service.CourseService;
import com.lsc.utils.Constant;
import com.lsc.utils.DateUtils;
import com.lsc.utils.ResponseResult;
import com.lsc.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/13 15:37
 */
@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    private final CourseMembersServiceImpl courseMembersService;
    private final UserServiceImpl userService;

    /**
     * 返回所有教师创建的课程和加入的课程
     * 课程状态为正常
     */
    @Override
    public List<Course> selectAllNormalCourses(String id) {
        //根据id查询对应的教师
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,id);
        User user=userService.getOne(queryWrapper);
        if (Objects.isNull(user)){
            throw new RuntimeException("没有对应的教师");
        }
        //查询教师加入的课程
        LambdaQueryWrapper<CourseMembers> queryWrapper2=new LambdaQueryWrapper<>();
        queryWrapper2.eq(CourseMembers::getStNo,user.getStno());
        //查询教师加入的课程
        List<CourseMembers> courseMembers = courseMembersService.list(queryWrapper2);
        //获取所有教师加入的课程id
        List<String> courseIds = courseMembers
                .stream()
                .map(CourseMembers::getId)
                .collect(Collectors.toList());
        //根据课程id查询对应的课程信息
        LambdaQueryWrapper<Course> queryWrapper3=new LambdaQueryWrapper<>();
        queryWrapper3.in(Course::getCourseId,courseIds)
                .eq(Course::getCourseStatus,Constant.CourseStatus.NORMAL);
        return list(queryWrapper3);
    }

    /**
     * 返回所有教师创建的课程和加入的课程
     * 课程状态为归档
     */
    @Override
    public List<Course> selectAllArchiveCourses(String id) {
        //根据id查询对应的教师
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,id);
        User user=userService.getOne(queryWrapper);
        if (Objects.isNull(user)){
            throw new RuntimeException("没有对应的教师");
        }
        //查询教师加入的课程
        LambdaQueryWrapper<CourseMembers> queryWrapper2=new LambdaQueryWrapper<>();
        queryWrapper2.eq(CourseMembers::getStNo,user.getStno());
        //查询教师加入的课程
        List<CourseMembers> courseMembers = courseMembersService.list(queryWrapper2);
        //获取所有教师加入的课程id
        List<String> courseIds = courseMembers
                .stream()
                .map(CourseMembers::getId)
                .collect(Collectors.toList());
        //根据课程id查询对应的课程信息
        LambdaQueryWrapper<Course> queryWrapper3=new LambdaQueryWrapper<>();
        queryWrapper3.in(Course::getCourseId,courseIds)
                .eq(Course::getCourseStatus,Constant.CourseStatus.ARCHIVE);
        return list(queryWrapper3);
    }

    /**
     * 课程归档
     * 将课程的状态改为归档
     */
    @Override
    public void archiveCourse(String courseId) {
        //根据课程id查询对应的课程
        LambdaQueryWrapper<Course> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getCourseId,courseId);
        Course course=getOne(queryWrapper);
        if (course==null){
            throw new RuntimeException("没有对应的课程");
        }
        if (course.getCourseStatus().equals(Constant.CourseStatus.ARCHIVE)){
            throw new RuntimeException("课程已经归档");
        }
        course.setCourseStatus(Constant.CourseStatus.ARCHIVE);
        //更新状态
        if (!updateById(course)){
            throw new RuntimeException("课程归档失败");
        }
    }

    /**
     * 查询课程下的所有成员
     * @param courseId 课程id
     */
    @Override
    public ResponseResult selectAllMembers(String courseId) {
        
        return null;
    }

    /**
     * 新增课程
     */
    @Override
    public Course addCourse(Course course) {
        //设置课程状态
        course.setCourseStatus(Constant.CourseStatus.NORMAL);
        //设置学生数量
        course.setStudentNum(0);
        //获取不重复的8位加课码
        String addCourseCode= StringUtils.getAddCourseCode();
        while (isRepeatedCode(addCourseCode)){
            addCourseCode=StringUtils.getAddCourseCode();
        }
        //设置加课码
        course.setAddCourseCode(addCourseCode);
        //设置创建时间
        course.setCreateTime(DateUtils.now());
        course.setUpdateTime(DateUtils.now());
        if (save(course)){
            //根据加课码查询用户
            LambdaQueryWrapper<Course> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(Course::getAddCourseCode,addCourseCode);
            return getOne(queryWrapper);
        }
        return null;
    }

    /**
     * 判断加课码是否重复
     */
    @Override
    public boolean isRepeatedCode(String addCourseCode) {
        if (Objects.isNull(addCourseCode)) {
            return true;
        }
        //根据加课码查询课程信息
        //如果返回数据不为空说明加课码重复
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getAddCourseCode, addCourseCode);
        return getOne(queryWrapper) != null;
    }
}
