package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusAccountRecharge;
import com.shsxt.xmjf.api.querys.BusAccountRechargeQuery;
import com.shsxt.xmjf.server.base.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BusAccountRechargeMapper extends BaseMapper<BusAccountRecharge> {

    /**
     * 用户充值
     * @param userId
     * @param amount
     * @param busiPassword
     * @return
     */
    public String addBusAccountRecharge(Integer userId, BigDecimal amount, String busiPassword);

    /**
     * 更新用户充值记录
     * @param orderNo
     * @return
     */
    public BusAccountRecharge queryBusAccountRechargeByOrderNo(@Param("orderNo") String orderNo);


    /**
     * 查询用户充值记录
     * @param busAccountRechargeQuery
     * @return
     */
    public List<Map<String,Object>> queryRechargesByUserId(BusAccountRechargeQuery busAccountRechargeQuery);


}