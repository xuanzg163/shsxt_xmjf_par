package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.service.IUserService;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author zhangxuan
 * @date 2018/11/9
 * @time 16:49
 */

public class TestUserService extends TestBase{

    @Resource
    private IUserService userService;

    @Test
    public void test01(){
        userService.saveUser("17512580668","123456","3333");
    }
}
