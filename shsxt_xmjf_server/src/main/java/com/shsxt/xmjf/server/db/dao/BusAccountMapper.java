package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.server.base.BaseMapper;
import org.springframework.data.repository.query.Param;

public interface BusAccountMapper extends BaseMapper<BusAccount> {
    /**
     * 查询用户的账户记录
     * 剩余金额，可用金额
     * @param userId
     * @return
     */
    BusAccount queryBusAccountByUserId(@Param("userId") Integer userId);
}