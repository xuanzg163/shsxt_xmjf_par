package com.shsxt.xmjf.web.controller;

import com.shsxt.xmjf.web.aop.annotations.RequireLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhangxuan
 * @date 2018/11/19
 * @time 19:27
 */
@Controller
@RequestMapping("account")
public class AccountController extends BaseControl {

    @RequestMapping("index")
    @RequireLogin
    public String index(){
        return "account";
    }

    @RequestMapping("recharge")
    @RequireLogin
    public String reCharge() {
        return "recharge";
    }

    /**
     * 支付宝同步回调
     * 通知
     */
    @RequestMapping("returnUrl")
    public void returnCallback() {
        System.out.println("同步回调通知=======");
    }

    /**
     * 支付宝异步回调通知
     */
    @RequestMapping("notifyUrl")
    public void notifyCallback(){
        System.out.println("异步回调通知...");
    }

}
