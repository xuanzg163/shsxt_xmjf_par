package com.shsxt.xmjf.web;

import com.alibaba.fastjson.JSON;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.BusiException;
import com.shsxt.xmjf.api.exceptions.NoLoginException;
import com.shsxt.xmjf.api.model.ResultInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;


/**
 * 全局异常处理
 * @author zhangxuan
 * @date 2018/11/11
 * @time 21:45
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request
            , HttpServletResponse response
            , Object handler
            , Exception ex) {
        ModelAndView modelAndView = new ModelAndView();

        /**
         * 判断是否登陆
         */
        if (ex instanceof NoLoginException){
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }

        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
            if (null != responseBody){
                /**
                 * 当前方法相应内容为json
                 */
                ResultInfo resultInfo=new ResultInfo();
                // 设置默认错误消息
                resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
                resultInfo.setMsg(XmjfConstant.OPS_FAILED_MSG);
                if(ex instanceof BusiException){
                    BusiException pe= (BusiException) ex;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());
                }

                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter pw=null;
                try {
                    pw=response.getWriter();
                    pw.write(JSON.toJSONString(resultInfo));
                    pw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(null !=pw){
                        pw.close();
                    }
                }
                return null;
            }else {
                /**
                 * 当前方法相应内容为视图
                 */
                if (ex instanceof BusiException){
                    BusiException pe= (BusiException) ex;
                    modelAndView.addObject("code",pe.getCode());
                    modelAndView.addObject("msg",pe.getMsg());
                }
                return modelAndView;
            }
        }else{
            return getDefultModelAndView();
        }
    }
    /**
     * 默认错误视图与错误消息
     * @return
     */
    public ModelAndView getDefultModelAndView() {
        ModelAndView mv= new ModelAndView("error");
        mv.addObject("code",300);
        mv.addObject("msg","操作失败!");
        return mv;
    }
}
