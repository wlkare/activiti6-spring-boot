package com.wang.activiti.util;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

/**
 * @program: ActivitiUtils
 * @description: Activiti工具
 * @author: Mr.Wang
 * @create: 2019-04-09 10:59
 * @Version 1.0
 **/
public class ActivitiUtils {

    private static ProcessEngine processEngine;

    /**
     * 单例模式获取引擎对象
     *
     */
    public static ProcessEngine getProcessEngine(){
        if (processEngine == null){
            //使用默认的配置文件名称（activiti.cfg.xml）创建引擎对象

            processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine();
        }

        return processEngine;
    }
}
