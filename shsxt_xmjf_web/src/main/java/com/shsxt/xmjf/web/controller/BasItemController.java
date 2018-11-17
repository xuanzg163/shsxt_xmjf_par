package com.shsxt.xmjf.web.controller;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.BasItem;
import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.api.querys.BasItemQuery;
import com.shsxt.xmjf.api.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
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

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private IBusItemLoanService busItemLoanService;

    @Resource
    private ISysPictureService sysPictureService;

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
            //获取贷款用户id
            Integer loanUserId = basItem.getItemUserId();

            //查询贷款人安全信息（真实信息，身份证、身份证号码）
            BasUserSecurity basUserSecurity = basUserSecurityService.queryBasUserSecurityByUserId(loanUserId);

            //将用户真实身份证、姓名，转换成 X***模式
            basUserSecurity.setRealname(basUserSecurity.getRealname().substring(0,1)+replaceAll(basUserSecurity.getRealname().substring(1)));
            basUserSecurity.setIdentifyCard(basUserSecurity.getIdentifyCard().substring(0,4)+replaceAll(basUserSecurity.getIdentifyCard().substring(4,14))+basUserSecurity.getIdentifyCard().substring(14));

            model.addAttribute("loanUser",basUserSecurity);

            //查询贷款人的车辆信息
            model.addAttribute("busItemLoan",busItemLoanService.queryBusItemLoanByItemId(basItem.getId()));

            //获取安全审核的图片
            List<Map<String,Object>> pics = sysPictureService.querySysPicturesByItemId(itemId);
            model.addAttribute("pics",pics);
            return "details";

        }
        return "details";
    }

    /**
     * 处理字符，隐藏真实字符
     * @param substring
     * @return
     */
    public static String replaceAll(String substring) {
        StringBuffer stringBuffer = new StringBuffer();
        if (StringUtils.isBlank(substring)){
            return null;
        }
        for (int i = 0; i <substring.length() ; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
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
