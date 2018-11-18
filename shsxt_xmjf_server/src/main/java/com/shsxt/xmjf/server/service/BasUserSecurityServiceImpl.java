package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.server.db.dao.BasUserSecurityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangxuan
 * @date 2018/11/17
 * @time 10:21
 */
@Service
public class BasUserSecurityServiceImpl implements IBasUserSecurityService {


    @Resource
    private BasUserSecurityMapper basUserSecurityMapper;
    /**
     * 根据用户id查询贷款用户的真实信息
     * @param userId
     * @return
     */
    @Override
    public BasUserSecurity queryBasUserSecurityByUserId(Integer userId) {
        return basUserSecurityMapper.queryBasUserSecurityByUserId(userId);
    }

    /**
     * 更新用户认证信息
     * @param basUserSecurity
     * @return
     */
    @Override
    public int updateBasUserSecurity(BasUserSecurity basUserSecurity) {
        return basUserSecurityMapper.update(basUserSecurity);
    }
}
