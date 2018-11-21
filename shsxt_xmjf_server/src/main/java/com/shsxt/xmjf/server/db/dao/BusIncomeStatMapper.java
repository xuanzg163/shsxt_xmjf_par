package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusIncomeStat;
import com.shsxt.xmjf.server.base.BaseMapper;

public interface BusIncomeStatMapper extends BaseMapper<BusIncomeStat> {

    /**
     * 查询用户收益表
     * @param userId
     * @return
     */
    public  BusIncomeStat queryBusIncomeStatByUserId(Integer userId);
}