package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusUserIntegral;
import com.shsxt.xmjf.server.base.BaseMapper;
import org.springframework.data.repository.query.Param;

public interface BusUserIntegralMapper extends BaseMapper<BusUserIntegral> {
    /**
     *  查询用户积分信息
     * @param userId
     * @return
     */
    public  BusUserIntegral queryBusUserIntegralByUserId(@Param("userId") Integer userId);
}