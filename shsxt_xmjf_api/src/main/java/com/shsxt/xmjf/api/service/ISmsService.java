package com.shsxt.xmjf.api.service;

/**
 *  发送短信接口
 * @author zhangxuan
 * @date 2018/11/8
 * @time 19:41
 */

public interface ISmsService {

    /**
     * 发送短信接口
     * @param phone
     * @param type 短信类型，1是登陆，2是注册
     */
    public void sendSms(String phone, Integer type);
}
