package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.po.BusAccount;

/**
 * @author zhangxuan
 * @date 2018/11/15
 * @time 23:13
 */

public interface IAccountService {
    /**
     * 查询用户账户记录
     * 剩余金额，可用金额
     * @param userId
     * @return
     */
    public BusAccount queryBusAccountByUserId(Integer userId );
}
