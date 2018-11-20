package com.shsxt.xmjf.api.service;


import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.BasUser;
import com.shsxt.xmjf.api.po.User;

public interface IUserService {

    public User queryUserByUserId(Integer userId);

    /**
     * 根据手机号码查询用户
     */
    public BasUser queryBasUserByPhone(String phone);

    /**
     * 用户注册
     */
    public void saveUser(String phone,String password,String code);

    /**
     * 用户登陆
     * @param phone
     * @param password
     * @return
     */
    public UserModel login(String phone, String password);

    /**
     *  用户快速登陆
     * @param phone
     * @param code
     * @return
     */
    public UserModel quickLogin(String phone,String code);

    /**
     * 更新用户认证信息
     * @param realName
     * @param cardNo
     * @param userId
     * @param busiPassword 交易密码
     * @return
     */
    public ResultInfo updateBasUserSecurityInfo(String realName, String cardNo, Integer userId,String busiPassword);

    /**
     * 检查用户实名认证状态
     * @param userId
     * @return
     */
    public ResultInfo checkRealNameStatus(Integer userId);

    /**
     *
     * @param userId
     * @return
     */
    public  BasUser queryBasUserByUserId(Integer userId);

}
