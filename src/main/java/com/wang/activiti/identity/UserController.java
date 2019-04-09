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
    public String login(@RequestParam(value = "username",required = false) String userName
            , @RequestParam(value = "password",required = false) String password, HttpSession session){
        logger.debug("login request: {username={}, password={}}", userName, password);

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

            return "redirect:/index";
        }else {
            return "redirect:/login.ftl?error=true";
        }

    }

}
