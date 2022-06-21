package com.lsc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lsc.eneity.*;
import com.lsc.mapper.TaskMapper;
import com.lsc.service.TaskService;
import com.lsc.utils.BeanCopyUtils;
import com.lsc.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * @author lsc
 * @version 1.0
 * @date 2022/6/15 15:10
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    private final GradeServiceImpl gradeService;

    private final CourseServiceImpl courseService;

    private final AnnexServiceImpl annexService;

    private final TaskMapper taskMapper;
    /**
     * 发布作业
     */
    @Override
    public boolean createTask(Task task, MultipartFile file,String userId) {
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

        annexService.uploadAnnex(file,taskId,userId);

        return true;
    }

    //获取课程下的所有作业信息
    @Override
    public List<TaskListVo> selectAll(String courseId, String userId) {
        LambdaQueryWrapper<Task> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Task::getCourseId, courseId);
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

}
