package com.wang.activiti.controller;

import com.wang.activiti.util.UserUtil;
import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: TaskController
 * @description: 任务列表
 * @author: Mr.Wang
 * @create: 2019-04-12 09:11
 * @Version 1.0
 **/

@Controller
public class TaskController {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private FormService formService;

    private static String TASK_LIST = "redirect:/task-list";

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    /**
     * 读取启动流程的表单字段
     */
    @RequestMapping(value = "task-list")
    public ModelAndView todoTasks(HttpSession session) throws Exception{

        ModelAndView mav = new ModelAndView("task-list");
        User user = UserUtil.getUserFromSession(session);

        //使用代码代办查询
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(user.getId()).list();

        mav.addObject("tasks", tasks);

        return mav;
    }

    /**
     * 签收任务
     */
    @RequestMapping(value = "task-claim/{id}")
    public String claim(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes){
        String userId = UserUtil.getUserFromSession(session).getId();
        TaskService taskService = processEngine.getTaskService();
        taskService.claim(taskId, userId);
        redirectAttributes.addFlashAttribute("message", "任务已签收");
        return TASK_LIST;
    }

    /**
     * 读取用户任务的表单字段
     */
    @RequestMapping(value = "task-getform/{taskId}")
    public ModelAndView readTaskForm(@PathVariable("taskId") String taskId) throws Exception{
        ModelAndView mav = new ModelAndView("task-form");
        TaskFormData taskFormData = formService.getTaskFormData(taskId);

        if (taskFormData.getFormKey() != null){
            Object renderedTaskForm = formService.getRenderedTaskForm(taskId);
            TaskService taskService = processEngine.getTaskService();

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            mav.addObject("task", task);
            mav.addObject("taskFormData", renderedTaskForm);
            mav.addObject("hasFormKey", true);
            logger.info("taskFormData:{}",renderedTaskForm);
        }else {
            mav.addObject("taskFormData",taskFormData);
            mav.addObject("hasFormKey", false);
            logger.info("taskFormData:{}",taskFormData);
        }
        return mav;
    }


    /**
     * 读取启动流程的表单字段
     */
    @RequestMapping(value = "task-getform/task-complete/{taskId}")
    public String completeTask(@PathVariable("taskId") String taskId, HttpServletRequest request) throws Exception{
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        String formKey = taskFormData.getFormKey();

        //从请求中获取表单字段的值
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        HashMap<String, String> formValues = new HashMap<>();

        if (StringUtils.isNotBlank(formKey)){ //form表单
            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
            for (Map.Entry<String, String[]> entry : entrySet){
                String key = entry.getKey();
                formValues.put(key, entry.getValue()[0]);
            }
        }else {  //动态表单
            for (FormProperty formProperty : formProperties){
                String value = request.getParameter(formProperty.getId());
                formValues.put(formProperty.getId(), value);
            }
        }
        formService.submitTaskFormData(taskId, formValues);
        return TASK_LIST;
    }


}
