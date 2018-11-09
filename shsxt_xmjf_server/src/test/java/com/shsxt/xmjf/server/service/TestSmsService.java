package com.shsxt.xmjf.server.service;

import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.service.ISmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author zhangxuan
 * @date 2018/11/8
 * @time 21:01
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring.xml"
})
public class TestSmsService {

    @Resource
    private ISmsService smsService;

    @Test
    public void test01() {
        smsService.sendSms("17512580668",
                XmjfConstant.SMS_REGISTER_TYPE);
    }
}
