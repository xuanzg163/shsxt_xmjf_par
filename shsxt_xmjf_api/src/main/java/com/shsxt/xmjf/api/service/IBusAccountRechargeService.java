package com.shsxt.xmjf.api.service;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.querys.BusAccountRechargeQuery;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author zhangxuan
 * @date 2018/11/19
 * @time 22:54
 */

public interface IBusAccountRechargeService {

    /**
     * 用户充值
     *
     * @param userId
     * @param amount
     * @param busiPassword
     * @return
     */
    public String addBusAccountRecharge(Integer userId, BigDecimal amount, String busiPassword);

    /**
     * 更新充值记录
     *
     * @param orderNo
     * @param amount
     * @param sellerId
     * @param appId
     * @param busiNo
     */
    public void updateBusAccountRechargeInfo(String orderNo
            , BigDecimal amount
            , String sellerId
            , String appId
            , String busiNo);

    /**
     * 查询用户充值记录
     *
     * @param busAccountRechargeQuery
     * @return
     */
    public PageInfo<Map<String, Object>> queryRechargesByUserId(BusAccountRechargeQuery busAccountRechargeQuery);
}
