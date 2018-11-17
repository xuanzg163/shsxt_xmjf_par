package com.shsxt.xmjf.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.po.BusItemInvest;
import com.shsxt.xmjf.api.querys.ItemInvestQuery;
import com.shsxt.xmjf.api.service.IBusItemInvestService;
import com.shsxt.xmjf.server.db.dao.BusItemInvestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zhangxuan
 * @date 2018/11/17
 * @time 12:37
 */
@Service
public class BusItemInvestServiceImpl implements IBusItemInvestService {
    @Resource
    private BusItemInvestMapper busItemInvestMapper;

    @Override
    public PageInfo<Map<String, Object>> queryInvestItemsByItemId(ItemInvestQuery itemInvestQuery) {
        PageHelper.startPage(itemInvestQuery.getPageNum(), itemInvestQuery.getPageSize());
        List<Map<String,Object>> vals =busItemInvestMapper.queryInvestItemsByItemId(itemInvestQuery);
        return new PageInfo<Map<String,Object>>(vals);
    }
}
