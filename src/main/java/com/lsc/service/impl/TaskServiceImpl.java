package com.lsc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsc.eneity.*;
import com.lsc.mapper.TaskMapper;
import com.lsc.service.TaskService;
import com.lsc.utils.BeanCopyUtils;
import com.lsc.utils.Constant;
import com.lsc.utils.DateUtils;
import com.lsc.utils.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 15:10
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    private final GradeServiceImpl gradeService;

    private final CourseServiceImpl courseService;

    private final AnnexServiceImpl annexService;

    private final TaskMapper taskMapper;

    private final UserServiceImpl userService;

    private final CourseMembersServiceImpl courseMembersService;
    /**
     * 发布作业
     */
    @Override
    public boolean createTask(Task task, MultipartFile file,String userId) {
        if (Objects.isNull(task)){
            return false;
        }
        //设置提交数量
        task.setSubmitNum(0);
        //设置已批改数量
        task.setCorrectNum(0);
        //设置创建时间
        task.setCreateTime(DateUtils.now());
        //设置更新时间
        task.setUpdateTime(DateUtils.now());
        //保存作业信息
        taskMapper.insert(task);

        //获取作业id
        String taskId=task.getTaskId();

        //判断用户上传的作业信息是否携带了文件
        if (Objects.isNull(file)){
            log.info("发布作业,未提交附件");
            return true;
        }

        annexService.uploadAnnex(file,taskId,userId);

        return true;
    }

    //获取课程下的所有作业信息
    @Override
    public List<TaskListVo> selectAll(String courseId, String userId) {
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Task::getCourseId, courseId)
                .orderByDesc(Task::getCreateTime);
        List<Task> tasks=list(queryWrapper);
        List<TaskListVo> taskVos = BeanCopyUtils.copyBeanList(tasks, TaskListVo.class);
        for (TaskListVo taskVo : taskVos) {
            //根据课程id和用户id去grade表里查,结果不为空说明学生的该项作业已经批改
            LambdaQueryWrapper<Grade> gradeQW=new LambdaQueryWrapper<>();
            gradeQW.eq(Grade::getTaskId,taskVo.getTaskId())
                    .eq(Grade::getStudentId,userId);

            Grade grade = gradeService.getOne(gradeQW);
            if (Objects.isNull(grade)){
                taskVo.setSituation("未批改");
                continue;
            }
            //设置分数
            taskVo.setGrade(grade.getScore());
            taskVo.setSituation("已批改");
        }
        //设置分数
        return taskVos;
    }

    /**
     * 批改作业
     */
    @Override
    public boolean correct(Grade grade) {
        //判断是否是第一次批改
        //根据作业id和学生id查询对应的成绩
        LambdaQueryWrapper<Grade> gradeQW=new LambdaQueryWrapper<>();
        gradeQW.eq(Grade::getStudentId,grade.getStudentId())
                        .eq(Grade::getTaskId,grade.getTaskId());

        Grade selectGrade=gradeService.getOne(gradeQW);
        if (Objects.nonNull(selectGrade)){//说明该作业不是第一次批改
            //覆盖之前的作业批改信息
            log.info("教师批改作业,不是第一次批改");
            selectGrade.setUpdateTime(DateUtils.now());
            selectGrade.setScore(grade.getScore());
            return  gradeService.updateById(selectGrade);
        }
        log.info("教师批改作业,首次批改");
        grade.setCreateTime(DateUtils.now());
        grade.setUpdateTime(DateUtils.now());
        if (gradeService.save(grade)) {
            //批改作业成功,作业的批改人数+1
            String taskId = grade.getTaskId();
            //根据作业id查找作业

            Task task = getById(taskId);

            task.setCorrectNum(task.getCorrectNum() + 1);

            return updateById(task);
        }
        return false;
    }

    /**
     * 指定作业的提交数量+1
     * @param taskId 作业id
     */
    @Override
    public void addSubmitNum(String taskId) {
        Task task=getById(taskId);

        if (Objects.isNull(task)){
            throw new RuntimeException("指定作业不存在");
        }

        task.setSubmitNum(task.getSubmitNum()+1);

        if (!updateById(task)){
            throw new RuntimeException("作业提交数量增加失败");
        }

    }

    /**
     * 根据作业id查询对应的作业
     * 附带教师上传的附件下载地址
     * @param taskId 作业id
     */
    @Override
    public TaskDetailsVo getDetailsVoById(String taskId) {
        Task task=getById(taskId);

        if (Objects.isNull(task)){
            throw new RuntimeException("作业id不存在,无法获取作业信息");
        }

        TaskDetailsVo taskDetailsVo = BeanCopyUtils.copyBean(task, TaskDetailsVo.class);

        //根据作业id找到课程id
        String courseId=task.getCourseId();
        //根据课程id找到教师id
        LambdaQueryWrapper<Course> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getCourseId,courseId);

        Course course=courseService.getOne(queryWrapper);

        if (Objects.isNull(course)){
            throw new RuntimeException("作业对应的课程不存在或已删除");
        }

        String teacherId=course.getOwnerId();
        //根据作业id和教师id找到教师上传到该作业上的附件
        LambdaQueryWrapper<Annex> annexLambdaQueryWrapper=new LambdaQueryWrapper<>();
        annexLambdaQueryWrapper.eq(Annex::getTaskId,taskId)
                .eq(Annex::getOwnerId,teacherId);

        Annex annex=annexService.getOne(annexLambdaQueryWrapper);

        //设置作业附件路径
        if (Objects.nonNull(annex)){
            taskDetailsVo.setPath(annex.getPath());
        }

        return taskDetailsVo;
    }


    /**
     * 获取指定作业下提交的全部作业
     * 未提交作业的学生信息也要返回
     * @param taskId 作业id
     * @param userId 教师id
     */
    @Override
    public ResponseResult getAllGrades(String taskId,String userId) {

        //根据作业id查询课程id
        Task task=getById(taskId);

        if (Objects.isNull(task)){
            return ResponseResult.error("作业不存在");
        }

        String courseId=task.getCourseId();

        //根据课程id获取对应课程下的所有学生id
        LambdaQueryWrapper<CourseMembers> courseMembersQW=new LambdaQueryWrapper<>();
        courseMembersQW.eq(CourseMembers::getCourseId,courseId)
                .eq(CourseMembers::getUserStatus, Constant.UserStatus.STUDENT);

        List<CourseMembers> courseMembers = courseMembersService.list(courseMembersQW);

        List<String> studentIds=courseMembers.stream()
                .map(CourseMembers::getUserId)
                .collect(Collectors.toList());

        List<SubmitStatusVo> data = new ArrayList<>();

        for (String id : studentIds) {
            User user=userService.getById(id);
            SubmitStatusVo temp = new SubmitStatusVo();
            //设置学生id
            temp.setStudentId(id);
            //设置学号
            temp.setStno(user.getStno());
            //设置学生姓名
            temp.setName(user.getName());
            //根据学生id和作业id查询分数
            LambdaQueryWrapper<Grade> gradeQW=new LambdaQueryWrapper<>();
            gradeQW.eq(Grade::getStudentId,id)
                    .eq(Grade::getTaskId,taskId);

            Grade grade = gradeService.getOne(gradeQW);
            if (Objects.isNull(grade)){//教师未批改
                temp.setScore("0");
            }else {
                temp.setScore(grade.getScore());
            }
            //设置附件(学生提交的作业)路径
            //设置附件提交时间
            //根据作业id和学生id查询附件
            LambdaQueryWrapper<Annex> annexQW=new LambdaQueryWrapper<>();
            annexQW.eq(Annex::getTaskId,taskId)
                    .eq(Annex::getOwnerId,id);

            Annex annex=annexService.getOne(annexQW);
            if (Objects.isNull(annex)){//未提交作业
                temp.setPath("");
            }else {
                temp.setPath(annex.getPath());
                temp.setSummitTime(annex.getCreateTime());
            }

            data.add(temp);
        }

//        //根据作业id查询所有提交的作业
//        LambdaQueryWrapper<Annex> annexQW = new LambdaQueryWrapper<>();
//        annexQW.eq(Annex::getTaskId, taskId)
//                .ne(Annex::getOwnerId, userId);  //去掉教师为作业添加的附件
//
//        List<Annex> list = annexService.list(annexQW);
//
//        List<SubmitStatusVo> data = new ArrayList<>();
//        for (Annex annex : list) {
//            SubmitStatusVo temp = new SubmitStatusVo();
//            //设置学生id
//            temp.setStudentId(annex.getOwnerId());
//            //设置学号和姓名
//            User user = userService.getById(annex.getOwnerId());
//            if (Objects.nonNull(user)) {
//                if (Objects.nonNull(user.getStno())) {
//                    temp.setStno(user.getStno());
//                }
//                if (Objects.nonNull(user.getName())) {
//                    temp.setName(user.getName());
//                }
//            }
//            //根据作业id和学生id找到对应的学生分数
//            LambdaQueryWrapper<Grade> gradeQW = new LambdaQueryWrapper<>();
//            gradeQW.eq(Grade::getTaskId, taskId)
//                    .eq(Grade::getStudentId, annex.getOwnerId());
//
//            Grade grade = gradeService.getOne(gradeQW);
//
//            //设置分数
//            if (Objects.nonNull(grade)) {
//                temp.setScore(grade.getScore());
//            } else {
//                temp.setScore("0");
//            }
//
//            //设置提交时间
//            temp.setSummitTime(annex.getCreateTime());
//
//            //设置附件路径
//            temp.setPath(annex.getPath());
//
//            data.add(temp);
//        }
        return ResponseResult.ok(data);
    }


    /**
     * 根据作业id找到教师id
     * @param taskId 作业id
     */
    @Override
    public String getUserIdByTaskId(String taskId) {
        //根据作业id获取课程id
        String courseId=getById(taskId).getCourseId();
        if (Objects.isNull(courseId)){
            throw new RuntimeException("作业所属的课程不存在");
        }
        //根据课程id获取教师id
        return courseService.getById(courseId).getOwnerId();
    }


    /**
     * 获取某一作业的指定学生的提交情况
     * @param taskId 作业id
     * @param studentId 学生id
     */
    @Override
    public ResponseResult getSubmitStatus(String taskId, String studentId) {
        //根据作业id和学生id查找对应的作业
        LambdaQueryWrapper<Annex> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Annex::getTaskId, taskId)
                .eq(Annex::getOwnerId, studentId);

        Annex annex = annexService.getOne(queryWrapper);

        Map<String, Object> data = new HashMap<>();

        if (Objects.isNull(annex)) {//找不到对应的附件,说明未提交作业
            data.put("isSubmit", false);
            data.put("score", 0);
            data.put("path", "");
            data.put("remarks","");
        } else {
            data.put("isSubmit", true);
            data.put("path", annex.getPath());
            data.put("remarks",annex.getRemarks());
            //根据作业id和学生id查找成绩
            LambdaQueryWrapper<Grade> gradeQW = new LambdaQueryWrapper<>();
            gradeQW.eq(Grade::getTaskId, taskId)
                    .eq(Grade::getStudentId, studentId);

            Grade grade = gradeService.getOne(gradeQW);

            if (Objects.isNull(grade)) {//说明作业还未批改
                data.put("score", 0);
            } else {
                data.put("score", grade.getScore());
            }

        }
        return ResponseResult.ok(data);
    }

}
