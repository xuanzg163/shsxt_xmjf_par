package com.shsxt.xmjf.api.service;


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

}
