package com.shsxt.xmjf.web.controller;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.querys.BasItemQuery;
import com.shsxt.xmjf.api.service.IBasItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author zhangxuan
 * @date 2018/11/14
 * @time 19:20
 */
@Controller
@RequestMapping("item")
public class BasItemController extends BaseControl {

    @Resource
    private IBasItemService basItemService;

    /**
     * 查询投资列表
     * @param basItemQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public PageInfo<Map<String,Object>> queryItemsByParams(BasItemQuery basItemQuery){
        return basItemService.queryItemsByParams(basItemQuery);
    }

    /**
     * 显示投资列表
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "invest_list";
    }
}
