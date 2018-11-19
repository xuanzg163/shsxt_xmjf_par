package com.shsxt.xmjf.web.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author zhangxuan
 * @date 2018/11/19
 * @time 19:06
 */
@Controller
@RequestMapping("alipay")
public class AlipayController {

    /**
     *  用户充值
     * @param amount 充值金额
     * @param request
     * @return
     */
    @RequestMapping("doRecharge")
    public String doRecharge(BigDecimal amount, HttpServletRequest request) {

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，订单号码唯一，必填
        String out_trade_no = System.currentTimeMillis()+"";

        //订单名称
        String subject = "用户充值";

        //商品描述
        String body = "用户充值";

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        try {
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            request.setAttribute("result",result);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        //视图，用作表单提交
        return "result";
    }
}
