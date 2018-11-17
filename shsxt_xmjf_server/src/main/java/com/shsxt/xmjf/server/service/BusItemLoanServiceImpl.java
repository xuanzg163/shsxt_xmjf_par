package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.po.BusItemLoan;
import com.shsxt.xmjf.api.service.IBusItemLoanService;
import com.shsxt.xmjf.server.db.dao.BusItemLoanMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangxuan
 * @date 2018/11/17
 * @time 11:04
 */

@Service
public class BusItemLoanServiceImpl implements IBusItemLoanService {

    @Resource
    private BusItemLoanMapper busItemLoanMapper;

    /**
     * 查询贷款人的车辆信息
     * @param itemId
     * @return
     */
    @Override
    public BusItemLoan queryBusItemLoanByItemId(Integer itemId) {
        return busItemLoanMapper.queryBusItemLoanByItemId(itemId);
    }
}
