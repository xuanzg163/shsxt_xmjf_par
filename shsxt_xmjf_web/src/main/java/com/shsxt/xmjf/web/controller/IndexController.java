package com.shsxt.xmjf.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;

/**
 * 显示主页内容
 * @author zhangxuan
 * @date 2018/11/8
 * @time 14:18
 */

@Controller
public class IndexController {

    /**
     * 登陆
     * @param request
     * @return
     */
    @GetMapping("login")
    public String login(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "login";
    }

    /**
     * 注册
     * @param request
     * @return
     */
    @GetMapping("register")
    public  String register(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "register";
    }

    /**
     * 主页
     * @param request
     * @return
     */
    @GetMapping("index")
    public  String index(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "index";
    }

    /**
     * 快速登陆
     * @param request
     * @return
     */
    @GetMapping("quickLogin")
    public  String quickLogin(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "quick_login";
    }
}
