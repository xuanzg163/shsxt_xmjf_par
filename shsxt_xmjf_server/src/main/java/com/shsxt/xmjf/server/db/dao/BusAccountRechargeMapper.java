package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusAccountRecharge;
import com.shsxt.xmjf.server.base.BaseMapper;

import java.math.BigDecimal;

public interface BusAccountRechargeMapper extends BaseMapper<BusAccountRecharge> {

    /**
     * 用户充值
     * @param userId
     * @param amount
     * @param busiPassword
     * @return
     */
    public String addBusAccountRecharge(Integer userId, BigDecimal amount, String busiPassword);

}