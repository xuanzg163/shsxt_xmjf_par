package com.shsxt.xmjf.web.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.shsxt.xmjf.api.constants.AlipayConfig;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.BusiException;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.service.IBusAccountRechargeService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.web.aop.annotations.RequireLogin;
import com.shsxt.xmjf.web.controller.BaseControl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author zhangxuan
 * @date 2018/11/19
 * @time 19:06
 */
@Controller
@RequestMapping("alipay")
public class AlipayController extends BaseControl {


    @Resource
    private IBusAccountRechargeService busAccountRechargeService;

    /**
     * 用户充值
     * @param amount 充值金额
     * @param busiPassword 交易密码
     * @param imageCode 图片验证码
     * @param request
     * @return
     */
    @RequestMapping("doRecharge")
    @RequireLogin
    public String doRecharge(BigDecimal amount,String busiPassword,String imageCode,HttpServletRequest request) {
        UserModel userModel= (UserModel) request.getSession().getAttribute(XmjfConstant.SESSION_USER);

        try {
            AssertUtil.isTrue(StringUtils.isBlank(imageCode),"请输入图片验证码!");
            String sessionImageCode= (String) request.getSession().getAttribute(XmjfConstant.SESSION_IMAGE);
            AssertUtil.isTrue(StringUtils.isBlank(sessionImageCode),"当前页面已失效,请重新刷新页面!");
            AssertUtil.isTrue(!(imageCode.equals(sessionImageCode)),"图片验证码不正确!");
            request.getSession().removeAttribute(XmjfConstant.SESSION_IMAGE);
            String result= busAccountRechargeService.addBusAccountRecharge(userModel.getUserId(),amount,busiPassword);
            request.setAttribute("result",result);
        } catch (BusiException e) {
            e.printStackTrace();
            request.setAttribute("msg",e.getMsg());
            return "recharge";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg",XmjfConstant.OPS_FAILED_MSG);
            return "recharge";
        }
        return "result";
    }
}
