package com.shsxt.xmjf.api.constants;

/**
 *  常量类
 * @author zhangxuan
 * @date 2018/11/8
 * @time 19:25
 */

public class XmjfConstant {

    public static final  Integer OPS_SUCCESS_CODE = 200;
    public static final  String OPS_SUCCESS_MSG = "操作成功";

    public static final  Integer OPS_FAILED_CODE = 300;
    public static final  String OPS_FAILED_MSG = "操作失败";

    public static final Integer NO_LOGIN_CODE=305;
    public static final String  NO_LOGIN_MSG="未登录";

    //图片验证码常量
    public static final String SESSION_IMAGE = "image";

    //保存用户信息常量
    public static final String SESSION_USER = "userInfo";

    /**
     * 投资页页码展示
     */
    public static final int PAGE_NUM = 1;
    public static final int PAGE_SIZE = 10;


    /**
     * 短信类型定义
     */
    public static final int SMS_LOGIN_TYPE=1;
    public static final int SMS_REGISTER_TYPE=2;
    public static final int SMS_REGISTER_SUCCESS_NOTIFY_TYPE=3;
    public static final int SMS_RECHARGE_SUCCESS_NOTIFY_TYPE=4;

    /**
     * 阿里云短信常量配置
     */
    /*public static final String SMS_AK="LTAIftPLNzhgsHmx";
    public static final String SMS_SK="VgLO26MfrjUiEohAZVJ5YfLsjl8p3J";
    public static final String SMS_PRODUCT="Dysmsapi";
    public static final String SMS_DOMAIN="dysmsapi.aliyuncs.com";
    public static final String SMS_SIGN="小马金服";
    public static final String SMS_REGISTER_CODE="SMS_115100107";
    public static final String SMS_LOGIN_CODE="SMS_109450111";
    public static final String SMS_REGISTER_SUCCESS_NOTIFY_CODE="SMS_109450119";*/

    /**
     * 备用短信配置常量
     */
    public static final String SMS_AK="LTAI71HGQAm4FhJl";
    public static final String SMS_SK="v4Rykey4OactIwgB5wQUvt4OqRyaP3";
    public static final String SMS_PRODUCT="Dysmsapi";
    public static final String SMS_DOMAIN="dysmsapi.aliyuncs.com";
    public static final String SMS_SIGN="MikuGames";
    public static final String SMS_REGISTER_CODE="SMS_150736238";
    public static final String SMS_LOGIN_CODE="SMS_150736683";
    public static final String SMS_REGISTER_SUCCESS_NOTIFY_CODE="SMS_150742429";
    public static final String SMS_RECHARGE_SUCCESS_NOTIFY_CODE="SMS_150742429";


    /**
     * 配置实名认常量参数
     */
    public static final String SM_HOST= "https://1.api.apistore.cn";
    public static final String SM_PATH="/idcard3";
    public static final String SM_REQUEST_TYPE="POST";
    public static final String SM_CODE="a386f386635c4dc89638459e09469710";


}
