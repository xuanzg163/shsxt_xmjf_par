package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusAccountLog;

public interface BusAccountLogMapper  {
    int deleteByPrimaryKey(Integer id);

    int insert(BusAccountLog record);

    int insertSelective(BusAccountLog record);

    BusAccountLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BusAccountLog record);

    int updateByPrimaryKeyWithBLOBs(BusAccountLog record);

    int updateByPrimaryKey(BusAccountLog record);
}