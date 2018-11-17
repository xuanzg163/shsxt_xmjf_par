package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusItemLoan;
import com.shsxt.xmjf.server.base.BaseMapper;

public interface BusItemLoanMapper extends BaseMapper<BusItemLoan> {
    public BusItemLoan queryBusItemLoanByItemId(Integer itemId);
}