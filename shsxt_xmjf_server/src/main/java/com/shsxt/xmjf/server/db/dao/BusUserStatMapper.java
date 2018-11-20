package com.shsxt.xmjf.server.db.dao;

import com.shsxt.xmjf.api.po.BusUserStat;
import com.shsxt.xmjf.api.po.BusUserStatKey;
import com.shsxt.xmjf.server.base.BaseMapper;
import org.springframework.data.repository.query.Param;

public interface BusUserStatMapper extends BaseMapper<BusUserStat> {
    /**
     * 查询用户统计信息
     * @param userId
     * @return
     */
    public  BusUserStat queryBusUserStatByUserId(@Param("userId") Integer userId);
}