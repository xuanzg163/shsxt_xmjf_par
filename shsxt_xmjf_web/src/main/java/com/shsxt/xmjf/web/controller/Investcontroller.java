package com.shsxt.xmjf.web.controller;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.querys.ItemInvestQuery;
import com.shsxt.xmjf.api.service.IBusItemInvestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author zhangxuan
 * @date 2018/11/17
 * @time 13:49
 */
@Controller
@RequestMapping("invest")
public class Investcontroller {

    @Resource
    private IBusItemInvestService busItemInvestService;

    @RequestMapping("list")
    @ResponseBody
    public PageInfo<Map<String,Object>> queryInvestItemsByItemId(ItemInvestQuery itemInvestQuery){
        return busItemInvestService.queryInvestItemsByItemId(itemInvestQuery);
    }
}
