package com.wang.activiti.controller.base;

import com.wang.activiti.util.ActivitiUtils;
import org.activiti.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @program: AbstractController
 * @description: 抽象Controller,提供一些基础的方法、属性
 * @author: Mr.Wang
 * @create: 2019-04-09 11:09
 * @Version 1.0
 **/
public abstract class AbstractController {

    protected ProcessEngine processEngine = null;

    private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    public AbstractController(){


        super();
        processEngine = ActivitiUtils.getProcessEngine();
    }

}
