package com.shsxt.xmjf.api.service;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.querys.BasItemQuery;

import java.util.Map;

/**
 * @author zhangxuan
 * @date 2018/11/14
 * @time 18:55
 */

public interface IBasItemService {
    /**
     * 投资列表、投资接口
     * @param basItemQuery
     * @return
     */
    public PageInfo<Map<String,Object>> queryItemsByParams(BasItemQuery basItemQuery);

    /**
     * 更新投资状态
     * @param itemId
     */
    void updateBasItemStatusToOpen(Integer itemId);
}
