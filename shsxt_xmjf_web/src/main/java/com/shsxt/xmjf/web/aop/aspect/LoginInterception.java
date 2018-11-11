package com.shsxt.xmjf.web.aop.aspect;

import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.NoLoginException;
import com.shsxt.xmjf.api.model.UserModel;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 切面，前置通知
 * @author zhangxuan
 * @date 2018/11/11
 * @time 21:27
 */

@Component
@Aspect
public class LoginInterception {

    @Resource
    private HttpSession session;

    /**
     * 自定义注解形成切面
     */
    @Pointcut("@annotation(com.shsxt.xmjf.web.aop.annotations.RequireLogin)")
    public void cut(){};

    /**
     * 前置通知
     */
    @Before(value = "cut()")
    public void before(){
        UserModel userModel = (UserModel) session.getAttribute(XmjfConstant.SESSION_USER);
        if (null == userModel){
            throw new NoLoginException(XmjfConstant.NO_LOGIN_MSG);
        }
    }

}
