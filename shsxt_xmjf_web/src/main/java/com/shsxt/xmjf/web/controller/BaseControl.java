package com.shsxt.xmjf.web.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * 基本控制层
 * @author zhangxuan
 * @date 2018/11/14
 * @time 19:59
 */
public class BaseControl {
    @ModelAttribute
    public void preMethod(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
    }
}
