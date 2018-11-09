package com.shsxt.xmjf.server.service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.service.ISmsService;
import com.shsxt.xmjf.api.service.IUserService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.api.utils.PhoneUtil;
import com.shsxt.xmjf.api.utils.RandomCodesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 实现短信发送
 *
 * @author zhangxuan
 * @date 2018/11/8
 * @time 20:11
 */

@Service
public class SmsServiceImpl implements ISmsService {

    @Resource
    private IUserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public void sendSms(String phone, Integer type) {

        /**
         * 1.参数校验
         *     手机号 不能为空 格式合法 长度11  13X   16X 15X 17X  18X  19X
         *     手机号唯一
         *    短信类型
         *       非空   类型必须合法
         * 2.执行短信发送
         *     调用阿里云SDK
         * 3.加入redis缓存 并设置过期时间 3分钟
         *
         */

        //参数校验
        checkParams(phone, type);

        //发送短信
        String code = RandomCodesUtils.createRandom(true, 4);
        if(type==XmjfConstant.SMS_REGISTER_TYPE){
            AssertUtil.isTrue(null !=userService.queryBasUserByPhone(phone),"该手机号已注册!");
            doSendSms(phone,XmjfConstant.SMS_REGISTER_CODE,code);
        }else if(type==XmjfConstant.SMS_LOGIN_TYPE){
            doSendSms(phone,XmjfConstant.SMS_LOGIN_CODE,code);
        }else if(type==XmjfConstant.SMS_REGISTER_SUCCESS_NOTIFY_TYPE){
            doSendSms(phone,XmjfConstant.SMS_REGISTER_SUCCESS_NOTIFY_CODE,code);
        }else{
            System.out.println("类型不合法!!!");
            return;
        }

        //将手机号，短信类型 加入redis缓存 设置失效时间 180s
        String key = "phone::" + phone + "::type::" + type;
        redisTemplate.opsForValue().set(key, code, 180, TimeUnit.SECONDS);

    }

    /**
     * 短信发送方法
     *
     * @param phone
     * @param templateCode
     * @param code
     */
    private void doSendSms(String phone, String templateCode, String code) {
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //短信API产品名称（短信产品名固定，无需修改）
            final String product = XmjfConstant.SMS_PRODUCT;
            //短信API产品域名（接口地址固定，无需修改）
            final String domain = XmjfConstant.SMS_DOMAIN;
            //你的accessKeyId,参考本文档步骤2
            final String accessKeyId = XmjfConstant.SMS_AK;
            //你的accessKeySecret，参考本文档步骤2
            final String accessKeySecret = XmjfConstant.SMS_SK;
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                    accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            request.setPhoneNumbers(phone);
            request.setSignName(XmjfConstant.SMS_SIGN);
            request.setTemplateCode(templateCode);
            Map<String, String> map = new HashMap<String, String>();
            map.put("code", code);
            request.setTemplateParam(JSON.toJSONString(map));
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            AssertUtil.isTrue(!(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")), "短信发送失败!");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    private void checkParams(String phone, Integer type) {
        AssertUtil.isTrue(StringUtils.isBlank(phone), "请输手机号");
        AssertUtil.isTrue(!(PhoneUtil.checkPhone(phone)), "手机号不合法!");

        AssertUtil.isTrue(null == type ||
                        !(type == XmjfConstant.SMS_LOGIN_TYPE
                                || type == XmjfConstant.SMS_REGISTER_TYPE
                                || type == XmjfConstant.SMS_REGISTER_SUCCESS_NOTIFY_TYPE),
                "短信类型不合法!");
    }
}
