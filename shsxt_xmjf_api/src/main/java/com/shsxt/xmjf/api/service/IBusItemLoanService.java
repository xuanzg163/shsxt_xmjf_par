package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.po.BusItemLoan;

public interface IBusItemLoanService {
    /**
     * 查询贷款人的车辆信息
     * @param itemId
     * @return
     */
    public BusItemLoan queryBusItemLoanByItemId(Integer itemId);
}
