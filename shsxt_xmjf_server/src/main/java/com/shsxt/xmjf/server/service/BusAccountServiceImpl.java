package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.api.service.IAccountService;
import com.shsxt.xmjf.server.db.dao.BusAccountMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangxuan
 * @date 2018/11/15
 * @time 23:16
 */

@Service
public class BusAccountServiceImpl implements IAccountService {

    @Resource
    private BusAccountMapper busAccountMapper;

    /**
     * 查用用户账户记录
     * 剩余金额，可用金额
     * @param userId
     * @return
     */
    @Override
    public BusAccount queryBusAccountByUserId(Integer userId) {
        return busAccountMapper.queryBusAccountByUserId(userId);
    }
}
