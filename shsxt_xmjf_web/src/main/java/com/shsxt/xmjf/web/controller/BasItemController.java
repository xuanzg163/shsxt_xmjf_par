package com.shsxt.xmjf.web.controller;

import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.BasItem;
import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.api.querys.BasItemQuery;
import com.shsxt.xmjf.api.service.IAccountService;
import com.shsxt.xmjf.api.service.IBasItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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

    @Resource
    private IAccountService accountService;

    /**
     * 显示投资项目详情
     * @return
     */
    @RequestMapping("details")
    public String details(Integer itemId, Model model, HttpSession session) {
        BasItem basItem = basItemService.queryBasItemByItemId(itemId);
        model.addAttribute("item",basItem);

        /**
         * 当用户登陆是 usermodel 非空
         */
        UserModel userModel= (UserModel) session.getAttribute(XmjfConstant.SESSION_USER);
        if(null !=userModel){
            /**
             * 查询用户账户信息
             * 剩余金额，可用金额
             */
            BusAccount busAccount=accountService.queryBusAccountByUserId(userModel.getUserId());
            model.addAttribute("account",busAccount);

            /**
             * 查询贷款人的基本信息
             */
            Integer loanUserId = basItem.getItemUserId();

        }
        return "details";
    }

    /**
     * 更新投资状态
     * @param itemId
     * @return
     */
    @RequestMapping("updateBasItemStatusToOpen")
    @ResponseBody
    public ResultInfo updateBasItemStatusToOpen(Integer itemId) {
        basItemService.updateBasItemStatusToOpen(itemId);
        return new ResultInfo("更新成功");
    }
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
