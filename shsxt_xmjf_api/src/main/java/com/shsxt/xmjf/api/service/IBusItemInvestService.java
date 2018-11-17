package com.shsxt.xmjf.api.service;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.querys.ItemInvestQuery;

import java.util.Map;

/**
 * @author zhangxuan
 * @date 2018/11/17
 * @time 12:34
 */

public interface IBusItemInvestService {
    public PageInfo<Map<String,Object>> queryInvestItemsByItemId(ItemInvestQuery itemInvestQuery);
}
