package com.shsxt.xmjf.web.aop.annotations;

import java.lang.annotation.*;

/**
 * 自定义注解
 * 拦截非登陆资源
 * @author zhangxuan
 * @date 2018/11/11
 * @time 21:25
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireLogin {
}
