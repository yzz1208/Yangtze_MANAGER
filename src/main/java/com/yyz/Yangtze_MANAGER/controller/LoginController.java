package com.yyz.Yangtze_MANAGER.controller;

import com.yyz.Yangtze_MANAGER.entity.User;
import com.yyz.Yangtze_MANAGER.service.UserService;
import com.yyz.Yangtze_MANAGER.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author yyz
 * @version 1.0
 * @description: 处理用户登录和注销
 * @date 2022/7/15 8:34
 */
@Controller
public class LoginController {
    // 打印日志的对象
    private Logger logger=Logger.getLogger(LoginController.class);
    @Resource
    private UserService userService;
    /**
     * 登录方法
     * @param userCode 用户编码
     * @param userPassword 用户密码
     * @return
     */
    @RequestMapping(value="/dologin",method = RequestMethod.POST)
    public ModelAndView doLogin(@RequestParam String userCode, String userPassword, HttpSession session, HttpServletRequest request){
        logger.debug("doLogin....");
        User user = userService.login(userCode, userPassword);
        System.out.println(user);
        if(user !=null){
            // 将用户的信息存入session
            session.setAttribute(Constants.USER_SESSION,user);
            // 登陆成功 跳转首页
            return new ModelAndView("/frame");
        }else{
            // 登录失败
            request.setAttribute("error","用户名或密码错误！");
            //return new ModelAndView("redirect:/login.jsp");
            return new ModelAndView("forward:/login.jsp");
        }
    }

    /**
     * 用户退出功能
     * @param session
     * @param request
     * @return
     */
    @GetMapping("/logout")
    //@RequestMapping(value = "/logout",method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session, HttpServletRequest request){
        // 清除session中的用户数据
        session.removeAttribute(Constants.USER_SESSION);
        return new ModelAndView("forward:/login.jsp");
    }
}
