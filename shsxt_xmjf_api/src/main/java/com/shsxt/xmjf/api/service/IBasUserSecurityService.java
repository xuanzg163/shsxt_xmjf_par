package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.po.BasUserSecurity;

public interface IBasUserSecurityService {

    /**
     * 查询用户安全信息记录
     * 真实的用户姓名
     * 真实的用户身份证号
     */
    public BasUserSecurity queryBasUserSecurityByUserId(Integer userId);

}
