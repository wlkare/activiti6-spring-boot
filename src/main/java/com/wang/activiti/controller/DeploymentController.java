package com.wang.activiti.controller;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @program: DeploymentController
 * @description: 部署流程
 * @author: Mr.Wang
 * @create: 2019-04-09 11:29
 * @Version 1.0
 **/

@Controller
public class DeploymentController {

    private static final Logger logger = LoggerFactory.getLogger(DeploymentController.class);

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 流程定义列表
     */
    @RequestMapping(value = "/deployactiviti")
    public ModelAndView deployList() {

        // 对应
        ModelAndView mav = new ModelAndView("deployactiviti");

        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();

        mav.addObject("processDefinitionList", processDefinitionList);

        return mav;
    }


    /**
     * 部署流程资源,可部署多种类型文件
     */
    @RequestMapping(value = "/deploy")
    public String deploy(@RequestParam(value = "file", required = true) MultipartFile file){

        //获取上传的文件名
        String filename = file.getOriginalFilename();

        try {
            //得到输入流（字节流）对象
            InputStream fileInputStream = file.getInputStream();

            //获取文件的拓展名
            String extension = FilenameUtils.getExtension(filename);

            //zip或bar类型的文件用ZipInputStream方式部署
            DeploymentBuilder deployment = repositoryService.createDeployment();
            if (extension.equals("zip") || extension.equals("bar")){
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment.addZipInputStream(zip);
            }else {
                //其他类型的文件直接部署
                deployment.addInputStream(filename, fileInputStream);
            }
            deployment.deploy();

        } catch (IOException e) {
            logger.error("error on deploy process, because of file input stream");
        }

        return "redirect:deployactiviti";

    }

    /**
     * 读取资源文件
     * processDefinitionId:资源定义ID
     * resourceName: 资源名称
     *
     */
    @RequestMapping("/read-resource")
    public void readResource(@RequestParam("pdid") String processDefinitionId, @RequestParam("resourceName") String resourceName, HttpServletResponse response)
        throws Exception{
        ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();

        //通过接口读取
        InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

        //输出资源内容到相应对象
        byte[] bytes = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(bytes, 0, 1024)) != -1){
            response.getOutputStream().write(bytes, 0, len);
        }
    }

    /**
     * 删除部署的流程，级联删除流程实例
     * deploymentId: 流程部署ID
     */
    @RequestMapping("/delete-deployment")
    public String deteleProcessDefinition(@RequestParam("deploymentId") String deploymentId){
        repositoryService.deleteDeployment(deploymentId, true);
        return "redirect:deployactiviti";
    }


}
