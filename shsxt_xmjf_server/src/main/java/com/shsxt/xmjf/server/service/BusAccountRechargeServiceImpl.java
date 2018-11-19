package com.shsxt.xmjf.server.service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.shsxt.xmjf.api.constants.AlipayConfig;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.enums.OrderStatus;
import com.shsxt.xmjf.api.enums.RechaggeType;
import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.po.BusAccountRecharge;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.api.service.IBusAccountRechargeService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.api.utils.MD5;
import com.shsxt.xmjf.api.utils.RandomCodesUtils;
import com.shsxt.xmjf.server.db.dao.BusAccountRechargeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxuan
 * @date 2018/11/19
 * @time 23:00
 */

@Service
public class BusAccountRechargeServiceImpl implements IBusAccountRechargeService {

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private BusAccountRechargeMapper busAccountRechargeMapper;

    /**
     * 用户充值
     *
     * @param userId
     * @param amount
     * @param busiPassword
     * @return
     */
    @Override
    public String addBusAccountRecharge(Integer userId, BigDecimal amount, String busiPassword) {

        //参数校验
        checkParams(userId, amount, busiPassword);

        BusAccountRecharge busAccountRecharge = new BusAccountRecharge();
        busAccountRecharge.setUserId(userId);
        String orderNo = "SXT_P2P" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + RandomCodesUtils.createRandom(true, 10);
        busAccountRecharge.setOrderNo(orderNo);
        busAccountRecharge.setStatus(OrderStatus.PAY_CHECKING.getStatus());
        busAccountRecharge.setRechargeAmount(amount);
        busAccountRecharge.setResource("PC端用户充值");
        busAccountRecharge.setType(RechaggeType.PC.getType());
        busAccountRecharge.setRemark("PC端用户充值");
        busAccountRecharge.setAddtime(new Date());
        AssertUtil.isTrue(busAccountRechargeMapper.insert(busAccountRecharge) < 1, XmjfConstant.OPS_FAILED_MSG);

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl
                , AlipayConfig.app_id
                , AlipayConfig.merchant_private_key
                , "json"
                , AlipayConfig.charset
                , AlipayConfig.alipay_public_key
                , AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = System.currentTimeMillis() + "";
        //订单名称，必填
        String subject = "用户充值";
        //商品描述，可空
        String body = "用户充值";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("out_trade_no", orderNo);
        params.put("total_amount", amount);
        params.put("subject", "用户充值");
        params.put("body", "用户充值");
        params.put("product_code", "FAST_INSTANT_TRADE_PAY");
        alipayRequest.setBizContent(JSON.toJSONString(params));
        String result = null;
        try {
            result = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 参数校验
     *
     * @param userId
     * @param amount
     * @param busiPassword
     */
    private void checkParams(Integer userId, BigDecimal amount, String busiPassword) {
        BasUserSecurity basUserSecurity = basUserSecurityService.queryBasUserSecurityByUserId(userId);
        AssertUtil.isTrue(basUserSecurity.getRealnameStatus() != 1, "用户未实名!");
        AssertUtil.isTrue(StringUtils.isBlank(busiPassword), "请输入交易密码!");
        AssertUtil.isTrue(!(basUserSecurity.getPaymentPassword().equals(MD5.toMD5(busiPassword))), "交易密码不正确!");
        AssertUtil.isTrue(null == amount, "请输入支付金额!");
        AssertUtil.isTrue(amount.compareTo(BigDecimal.ZERO) <= 0, "支付金额必须大于零!");
    }
}
