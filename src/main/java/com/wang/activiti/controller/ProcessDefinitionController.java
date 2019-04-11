package com.wang.activiti.controller;

import com.wang.activiti.util.UserUtil;
import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: ProcessDefinitionController
 * @description: 流程定义相关功能：读取动态表单字段、读取外置表单内容 todo
 * @author: Mr.Wang
 * @create: 2019-04-10 10:06
 * @Version 1.0
 **/

@Controller
public class ProcessDefinitionController {

    private static final Logger logger = LoggerFactory.getLogger(ProcessDefinitionController.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private FormService formService;

    @Autowired
    private IdentityService identityService;

    /**
     * 读取启动流程的表单字段
     */
    @RequestMapping(value = "getform/start/{processDefinitionId}")
    public ModelAndView readStartForm(@PathVariable("processDefinitionId") String processDefinitionId) throws Exception{

        logger.info("======开始读取启动流程表单字段,processDefinitionId:{}", processDefinitionId);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        boolean hasStartFormKey = processDefinition.hasStartFormKey();

        //根据是否有formkey属性判断使用哪个展示层
//        String viewName = "start-process-form";
//        ModelAndView mav = new ModelAndView(viewName);

        ModelAndView mav = new ModelAndView("start-process-form");

        //判断是否有formkey属性
        if (hasStartFormKey){ //外置表单
            Object renderedStartForm = formService.getRenderedStartForm(processDefinitionId);
            mav.addObject("startFormData", renderedStartForm);
            mav.addObject("processDefinition", processDefinition);
        }else { //动态表单字段
            /**
             * 通过getStartFormData()方法即可读取启动流程时需要填写的表单数据（设计流程定义时的字段集合）
             * 得到一个StartFormData对象，还可以获取表单字段对象集合
             */
            StartFormData startFormData = formService.getStartFormData(processDefinitionId);
            mav.addObject("startFormData", startFormData);
        }

        mav.addObject("hasStartFormKey", hasStartFormKey);
        mav.addObject("processDefinitionId", processDefinitionId);

        logger.info("=======读取启动流程的表单字段完毕========");

        return mav;
    }

    @RequestMapping(value = "process-instance/start/{processDefinitionId}")
    public String startProcessInstance(@PathVariable("processDefinitionId") String pdid,
                                       HttpServletRequest request,
                                       RedirectAttributes redirectAttributes){

        logger.info("========开始启动流程实例========");

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(pdid).singleResult();
        boolean hasStartFormKey = processDefinition.hasStartFormKey();

        HashMap<String, String> formValuesMap = new HashMap<>();

        if (hasStartFormKey){ //form表单
            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
            for (Map.Entry<String, String[]> entry : entrySet){
                String key = entry.getKey();
                formValuesMap.put(key, entry.getValue()[0]);
            }
        }else { // 动态表单
            //先读取表单字段，再根据表单字段的ID读取请求参数值
            StartFormData startFormData = formService.getStartFormData(pdid);

            //从请求中获取表单字段的值
            List<FormProperty> formProperties = startFormData.getFormProperties();
            for (FormProperty formProperty : formProperties){
                String value = request.getParameter(formProperty.getId());
                formValuesMap.put(formProperty.getId(), value);
            }

        }

        //获取当前登陆的用户
        User user = UserUtil.getUserFromSession(request.getSession());

        //用户未登录不能操作，最好使用权限框架实现，例如spring Security、Shiro等
        if (user == null || StringUtils.isBlank(user.getId())){
            return "redirect:login";
        }
        identityService.setAuthenticatedUserId(user.getId());

        //提交表单字段并启动一个新的流程实例
        ProcessInstance processInstance = formService.submitStartFormData(pdid, formValuesMap);

        logger.info("开启一个流程实例：{}", processDefinition);

        //重定向带参数跳转
        redirectAttributes.addFlashAttribute("message", "流程已启动，实例ID：" + processInstance.getId());
        return "redirect:/deployactiviti";

    }
}
