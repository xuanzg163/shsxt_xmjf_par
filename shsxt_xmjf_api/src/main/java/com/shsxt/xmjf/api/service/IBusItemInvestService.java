package com.shsxt.xmjf.api.service;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.po.BasItem;
import com.shsxt.xmjf.api.querys.ItemInvestQuery;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author zhangxuan
 * @date 2018/11/17
 * @time 12:34
 */

public interface IBusItemInvestService {

    /**
     * 查询用户投资记录，并分页
     *
     * @param itemInvestQuery 返回值
     * @return
     */
    public PageInfo<Map<String, Object>> queryInvestItemsByItemId(ItemInvestQuery itemInvestQuery);

    /**
     * 添加投资记录
     *
     * @param itemId       项目编号
     * @param amount       金额
     * @param userId       用户id
     * @param busiPassword 交易密码
     */
    public void addBusItemInvest(Integer itemId, BigDecimal amount, Integer userId, String busiPassword);
}
