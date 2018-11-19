package com.shsxt.xmjf.api.service;

import java.math.BigDecimal;

/**
 * @author zhangxuan
 * @date 2018/11/19
 * @time 22:54
 */

public interface IBusAccountRechargeService {

    /**
     * 用户充值
     * @param userId
     * @param amount
     * @param busiPassword
     * @return
     */
    public String addBusAccountRecharge(Integer userId, BigDecimal amount,String busiPassword);
}
