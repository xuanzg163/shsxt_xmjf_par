package com.shsxt.xmjf.server.service;


import com.shsxt.xmjf.api.po.BasUser;
import com.shsxt.xmjf.api.po.User;
import com.shsxt.xmjf.api.service.IUserService;
import com.shsxt.xmjf.server.db.dao.BasUserMapper;
import com.shsxt.xmjf.server.db.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    @Resource
    private BasUserMapper basUserMapper;

    @Override
    public User queryUserByUserId(Integer userId) {
        return userDao.queryUserByUserId(userId);
    }

    @Override
    public BasUser queryBasUserByPhone(String phone) {
        return basUserMapper.queryBasUserByPhone(phone);
    }
}
