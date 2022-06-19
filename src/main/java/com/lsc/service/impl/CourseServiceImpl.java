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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/13 15:37
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    private final CourseMembersServiceImpl courseMembersService;

    private final UserServiceImpl userService;

    /**
     * 返回所有用户创建的课程和加入的课程
     * 课程状态为正常
     */
    @Override
    public List<Course> selectAllCourses(String userId, String courseStatus) {
        //根据id查询对应的用户
        LambdaQueryWrapper<User> userQW = new LambdaQueryWrapper<>();
        userQW.eq(User::getUserId, userId);
        User user = userService.getOne(userQW);
        if (Objects.isNull(user)) {
            throw new RuntimeException("没有对应的用户");
        }
        //查询用户加入的课程信息
        LambdaQueryWrapper<CourseMembers> courseMembersQW = new LambdaQueryWrapper<>();
        courseMembersQW.eq(CourseMembers::getUserId, userId);
        List<CourseMembers> courseMembers = courseMembersService.list(courseMembersQW);
        //获取所有用户加入的课程id
        List<String> courseIds = courseMembers
                .stream()
                .map(CourseMembers::getCourseId)
                .collect(Collectors.toList());
        //根据课程id查询对应的课程信息
        LambdaQueryWrapper<Course> courseQW = new LambdaQueryWrapper<>();
        courseQW.in(Course::getCourseId, courseIds)
                .eq(Course::getCourseStatus, courseStatus);
        return list(courseQW);
    }

    /**
     * 课程归档
     * 将课程的状态改为归档
     * 只有教师可以归档课程
     */
    @Override
    public void archiveCourse(String courseId) {
        //根据课程id查询对应的课程
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getCourseId, courseId);
        Course course = getOne(queryWrapper);
        if (course == null) {
            throw new RuntimeException("没有对应的课程");
        }
        if (course.getCourseStatus().equals(Constant.CourseStatus.ARCHIVE)) {
            throw new RuntimeException("课程已经归档");
        }
        course.setCourseStatus(Constant.CourseStatus.ARCHIVE);
        //更新状态
        if (!updateById(course)) {
            throw new RuntimeException("课程归档失败");
        }
    }

    /**
     * 查询课程下的所有成员
     * @param courseId 课程id
     */
    @Override
    public ResponseResult selectAllMembers(String courseId) {
        Map<String, Object> data=new HashMap<>();
        //获取所有加入该课程的教师
        List<User> teachers=selectMembersByStatus(Constant.UserStatus.TEACHER,courseId);
        data.put("teacherNum",teachers.size());
        data.put("teachers",teachers);
        //获取所有加入该课程的学生
        List<User> students=selectMembersByStatus(Constant.UserStatus.STUDENT,courseId);
        data.put("studentNum",students.size());
        data.put("students",students);
        return ResponseResult.ok(data);
    }

    /**
     * 教师创建课程
     * 教师自动加入课程
     */
    @Override
    public Course createCourse(Course course) {
        //设置课程状态
        course.setCourseStatus(Constant.CourseStatus.NORMAL);
        //设置学生数量
        course.setStudentNum(0);
        //获取不重复的8位加课码
        String addCourseCode = StringUtils.getAddCourseCode();
        while (isRepeatedCode(addCourseCode)) {
            addCourseCode = StringUtils.getAddCourseCode();
        }
        //设置加课码
        course.setAddCourseCode(addCourseCode);
        //设置创建时间
        course.setCreateTime(DateUtils.now());
        course.setUpdateTime(DateUtils.now());
        if (save(course)) {
            log.info("课程创建成功");
            //根据加课码查询课程
            LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Course::getAddCourseCode, addCourseCode);
            Course course1=getOne(queryWrapper);
            //课程创建成功,教师自动加入课程
            CourseMembers courseMembers=new CourseMembers();
            courseMembers.setCourseId(course1.getCourseId());
            courseMembers.setUserId(course.getOwnerId());
            courseMembers.setCreateTime(DateUtils.now());
            courseMembers.setUpdateTime(DateUtils.now());
            courseMembersService.updateById(courseMembers);
            log.info("教师自动加入课程成功");
            return course1;
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

    /**
     * 加入课程
     * @param addCourseCode 加课码
     * @param userId   用户id
     */
    @Override
    public void joinCourse(String addCourseCode, String userId) {
        //根据加课码判断课程是否存在
        LambdaQueryWrapper<Course> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getAddCourseCode,addCourseCode);
        Course course=getOne(queryWrapper);
        if (Objects.isNull(course)){
            throw new RuntimeException("课程不存在,无法加入课程");
        }

        //判断用户是否存在
        User user = userService.getById(userId);
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在,无法加入课程");
        }
        //获取刚才根据加课码查到的课程的课程id
        String courseId=course.getCourseId();

        //判断用户是否已经加入课程
        LambdaQueryWrapper<CourseMembers> courseMembersQW=new LambdaQueryWrapper<>();
        courseMembersQW.eq(CourseMembers::getCourseId,courseId)
                .eq(CourseMembers::getUserId,userId);
        if (Objects.nonNull(courseMembersService.getOne(courseMembersQW))){
            throw new RuntimeException("用户已加入课程");
        }

        //如果是学生加入课程,课程的学生数量需要加1
        if (user.getStatus().equals(Constant.UserStatus.STUDENT)){
            course.setStudentNum(course.getStudentNum()+1);
            updateById(course);
        }

        //封装CourseMembers对象
        CourseMembers courseMembers = new CourseMembers();
        courseMembers.setCourseId(courseId);
        courseMembers.setUserId(userId);
        courseMembers.setUserStatus(user.getStatus());
        courseMembers.setCourseStatus(Constant.CourseStatus.NORMAL);
        courseMembers.setCreateTime(DateUtils.now());
        courseMembers.setUpdateTime(DateUtils.now());

        //保存
        if (!courseMembersService.save(courseMembers)) {
            throw new RuntimeException("数据库发生错误,加入课程失败");
        }
    }

    /**
     * 查询课程下某个身份的所有成员
     * @param status 用户身份
     * @param courseId 课程id
     */
    public List<User> selectMembersByStatus(String status,String courseId) {
        List<User> list=new ArrayList<>();
        //根据课程id获取所有课程信息
        LambdaQueryWrapper<CourseMembers> qw = new LambdaQueryWrapper<>();
        qw.eq(CourseMembers::getCourseId, courseId)
                .eq(CourseMembers::getUserStatus, status);

        List<CourseMembers> courseMembers = courseMembersService.list(qw);

        if (courseMembers.size()==0){
            return list;
        }

        //根据课程信息获取所有加入该课程用户的id
        List<String> courseTeachersIds = courseMembers.stream()
                .map(CourseMembers::getUserId)
                .collect(Collectors.toList());

        //根据用户id查询用户信息
        LambdaQueryWrapper<User> userQW = new LambdaQueryWrapper<>();
        userQW.in(User::getUserId, courseTeachersIds);
        list=userService.list(userQW);
        return list;
    }
}
