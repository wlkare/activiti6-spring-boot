package com.wang.activiti.identity;

import com.wang.activiti.util.UserUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户登陆与退出
 */

@Controller
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ProcessEngine processEngine;

    @GetMapping("/login")
    public String login(@RequestParam(value = "username", required = false) String userName, @RequestParam(value = "password", required = false) String password, HttpSession session){
        logger.info("login request: {username={}, password={}}", userName, password);

        //activiti Identify Service
        IdentityService identityService = processEngine.getIdentityService();

        boolean checkPassword = identityService.checkPassword(userName, password);
        if (checkPassword){
            //查看用户是否存在
            User user = identityService.createUserQuery().userId(userName).singleResult();
            UserUtil.saveUserToSession(session, user);

            //读取角色
            List<Group> groupList = identityService.createGroupQuery().groupMember(user.getId()).list();
            session.setAttribute("groups", groupList);

            String[] groupNames = new String[groupList.size()];
            for (int i = 0; i < groupNames.length; i++){
                groupNames[i] = groupList.get(i).getName();
            }
            session.setAttribute("groupNames", ArrayUtils.toString(groupNames));

            return "redirect:first";
        }else {
            logger.info("用户名或密码错误：{username={}, password={}}",userName,password);
            return "login";
        }

    }

    //重定向，页面跳转
    @RequestMapping("first")
    public ModelAndView indexFirst(ModelAndView modelAndView) {
        modelAndView.setViewName("index-first");
        return modelAndView;
    }

    //退出登陆
    @GetMapping("/logout")
    public String logout(HttpSession session){

        logger.info("退出系统");

        //1,清空session中的用户信息
        session.removeAttribute("loginUser");
        //2,再将session进行注销
        session.invalidate();
        return "redirect:login";

    }

}
