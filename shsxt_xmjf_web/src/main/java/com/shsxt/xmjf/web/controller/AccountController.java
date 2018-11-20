package com.shsxt.xmjf.web.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.shsxt.xmjf.api.constants.AlipayConfig;
import com.shsxt.xmjf.api.exceptions.BusiException;
import com.shsxt.xmjf.api.service.IBusAccountRechargeService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.web.aop.annotations.RequireLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zhangxuan
 * @date 2018/11/19
 * @time 19:27
 */
@Controller
@RequestMapping("account")
public class AccountController extends BaseControl {

    @Resource
    private IBusAccountRechargeService busAccountRechargeService;

    @RequestMapping("index")
    @RequireLogin
    public String index() {
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
    public String returnCallback(@RequestParam(name = "out_trade_no") String orderNo,
                                 @RequestParam(name = "total_amount") BigDecimal totalAmount,
                                 @RequestParam(name = "seller_id") String sellerId,
                                 @RequestParam(name = "trade_no") String busiNo,
                                 @RequestParam(name = "app_id") String appId,
                                 HttpServletRequest request) {

        Boolean flag = checkSign(request);

        try {
            AssertUtil.isTrue(!flag, "验签失败!");
            busAccountRechargeService.updateBusAccountRechargeInfo(orderNo, totalAmount, sellerId, appId, busiNo);
            return "success";
        } catch (BusiException e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", e.getMsg());
            return "failed";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "订单支付失败!");
            return "failed";
        }
    }


    /**
     * 支付宝异步回调通知
     */
    @RequestMapping("notifyUrl")
    public void notifyCallback(HttpServletRequest request) {
        System.out.println("异步回调通知...");
    }

    /**
     * 验签
     *
     * @param request
     * @return
     */
    public Boolean checkSign(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        System.out.println("----------------------");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            System.out.println(entry.getKey() + "--" + entry.getValue());
        }
        System.out.println("-------------------------");
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return signVerified;

    }
}
