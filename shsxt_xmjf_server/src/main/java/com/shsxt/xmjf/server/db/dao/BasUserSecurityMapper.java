package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.server.base.BaseMapper;

public interface BasUserSecurityMapper extends BaseMapper<BasUserSecurity> {

    /**
     * 根据用户id查询贷款用户信息
     * @param userId
     * @return
     */
    public BasUserSecurity queryBasUserSecurityByUserId(Integer userId);
}