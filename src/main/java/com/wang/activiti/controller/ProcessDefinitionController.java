package com.wang.activiti.controller;

import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @program: ProcessDefinitionController
 * @description: 流程定义相关功能：读取动态表单字段、读取外置表单内容 todo
 * @author: Mr.Wang
 * @create: 2019-04-10 10:06
 * @Version 1.0
 **/

@Controller
public class ProcessDefinitionController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private FormService formService;

    /**
     * 读取启动流程的表单字段
     */
    @RequestMapping(value = "getform/start/{processDefinitionId}")
    public ModelAndView readStartForm(@PathVariable("processDefinitioned") String processDefinitionId) throws Exception{

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        boolean hasStartFormKey = processDefinition.hasStartFormKey();

        //根据是否有formkey属性判断使用哪个展示层
//        String viewName = "start-process-form";
//        ModelAndView mav = new ModelAndView(viewName);

        ModelAndView mav = new ModelAndView("start-process-form");

        //判断是否有formkey属性
        if (hasStartFormKey){
            Object renderedStartForm = formService.getRenderedStartForm(processDefinitionId);
            mav.addObject("startFormData", renderedStartForm);
            mav.addObject("processDefinition", processDefinition);
        }else { //动态表单
            StartFormData startFormData = formService.getStartFormData(processDefinitionId);
            mav.addObject("startFormData", startFormData);
        }

        mav.addObject("hasStartFormKey", hasStartFormKey);
        mav.addObject("processDefinitionId", processDefinitionId);

        return mav;
    }
}
