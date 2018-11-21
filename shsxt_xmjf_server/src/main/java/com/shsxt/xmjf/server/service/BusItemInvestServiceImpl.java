package com.shsxt.xmjf.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.constants.ItemStatus;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.po.*;
import com.shsxt.xmjf.api.querys.ItemInvestQuery;
import com.shsxt.xmjf.api.service.*;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.api.utils.MD5;
import com.shsxt.xmjf.api.utils.RandomCodesUtils;
import com.shsxt.xmjf.server.db.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Set;

/**
 * @author zhangxuan
 * @date 2018/11/17
 * @time 12:37
 */
@Service
public class BusItemInvestServiceImpl implements IBusItemInvestService {

    @Resource
    private BusItemInvestMapper busItemInvestMapper;

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private BusAccountMapper busAccountMapper;

    @Resource
    private IBasItemService basItemService;

    @Resource
    private IUserService userService;

    @Resource
    private ISmsService smsService;

    @Resource
    private BusUserStatMapper busUserStatMapper;

    @Resource
    private BusAccountLogMapper busAccountLogMapper;

    @Resource
    private BusIncomeStatMapper busIncomeStatMapper;

    @Resource
    private BusUserIntegralMapper busUserIntegralMapper;

    @Resource
    private BusIntegralLogMapper busIntegralLogMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 查询用户投资记录
     *
     * @param itemInvestQuery 返回值
     * @return
     */
    @Override
    public PageInfo<Map<String, Object>> queryInvestItemsByItemId(ItemInvestQuery itemInvestQuery) {
        PageHelper.startPage(itemInvestQuery.getPageNum(), itemInvestQuery.getPageSize());
        List<Map<String, Object>> vals = busItemInvestMapper.queryInvestItemsByItemId(itemInvestQuery);
        return new PageInfo<Map<String, Object>>(vals);
    }

    /**
     * 添加用户投资记录
     *
     * @param itemId       项目编号
     * @param amount       金额
     * @param userId       用户id
     * @param busiPassword 交易密码
     */
    @Override
    public void addBusItemInvest(Integer itemId, BigDecimal amount, Integer userId, String busiPassword) {

        /**
         * 1.参数校验
         *      userId:用户必须实名状态
         *      busiPassword:非空  密码必须正确
         *      amount:投资金额   300
         *          非空 >0
         *          账户可用金额范围    <=账户可用金额
         *         最大投资
         *           存在
         *              <=项目最大投资
         *            不存在
         *              <小于项目剩余投资金额
         *         最小投资
         *            存在
         *               >=最小投资
         *            不存在
         *        itemId:项目记录必须存在
         *           非空  数据库中存在该记录
         *          项目不可投资情况
         *               项目必须为开放状态 open=10
         *               如果新手标项目  仅限首投
         *               项目进度-->100%  不可投资
         *               已结束项目  不可投资
         *               定向投资 暂不支持
         *               移动端项目  pc端不可投资
         *          10000     9980   20
         *              100   截标
         *              当项目可投剩余金额<最小投资   不可投资
         * 2.执行表数据更新操作
         *       bas_item	项目表
         bus_item_invest	项目投资表
         bus_user_stat	用户统计表
         bus_account	用户账户表
         bus_account_log	用户账户操作日志表
         bus_income_stat	用户收益信息表
         bus_user_integral	用户积分表
         bus_integral_log	积分操作日志表
         3.短信通知(邮件通知)
         */

        BasUserSecurity basUserSecurity = basUserSecurityService.queryBasUserSecurityByUserId(userId);
        AssertUtil.isTrue(basUserSecurity.getRealnameStatus() == 0, "当前用户未进行实名操作,请先执行实名操作!");
        AssertUtil.isTrue(StringUtils.isBlank(busiPassword), "请输入交易密码!");
        AssertUtil.isTrue(!(basUserSecurity.getPaymentPassword().equals(MD5.toMD5(busiPassword))), "交易密码错误!");
        AssertUtil.isTrue(null == amount || amount.compareTo(BigDecimal.ZERO) <= 0, "金额必须>0");

        BusAccount busAccount = busAccountMapper.queryBusAccountByUserId(userId);
        AssertUtil.isTrue(amount.compareTo(busAccount.getUsable()) == 1, "投资金额超出账户可用金额!");

        BasItem basItem = basItemService.queryBasItemByItemId(itemId);
        AssertUtil.isTrue(null == itemId || null == basItem, "待投资记录不存在!");
        AssertUtil.isTrue(basItem.getItemStatus() != ItemStatus.OPEN, "项目状态非法!");

        if(basItem.getItemIsnew()==1){
            AssertUtil.isTrue(busItemInvestMapper.countUserInvestNewItemByUserId(userId)==1,"新手标项目仅限首投!");
        }
        AssertUtil.isTrue(basItem.getItemScale().compareTo(BigDecimal.valueOf(100)) == 0, "当前项目已满标!");
        AssertUtil.isTrue(System.currentTimeMillis() - basItem.getEndTime().getTime() > 0, "项目投资时间已结束!");
        AssertUtil.isTrue(StringUtils.isNotBlank(basItem.getPassword()), "定向标暂不支持投资操作!");
        AssertUtil.isTrue(basItem.getMoveVip() == 1, "PC端不支持移动端投资!");
        BigDecimal singleMinInvestAmount = basItem.getItemSingleMinInvestment();
        BigDecimal syAmount = basItem.getItemAccount().subtract(basItem.getItemOngoingAccount());
        AssertUtil.isTrue(amount.compareTo(syAmount) == 1, "投资金额不能大于项目剩余金额!");

        if (singleMinInvestAmount.compareTo(BigDecimal.ZERO) == 1) {
            AssertUtil.isTrue(syAmount.compareTo(singleMinInvestAmount) == -1, "项目即将进行截标操作,不支持投资!");
            AssertUtil.isTrue(amount.compareTo(singleMinInvestAmount) == -1, "单笔投资不小于最低投资!");
        }

        BigDecimal singleMaxInvestAmount = basItem.getItemSingleMaxInvestment();
        if (singleMaxInvestAmount.compareTo(BigDecimal.ZERO) == 1) {
            AssertUtil.isTrue(amount.compareTo(singleMaxInvestAmount) == 1, "单笔投资不小于最大投资!");
        }

        // 表更新操作
        /**
         * bas_item
         *    进度值  item_scale
         *    进行中金额  item_ongoing_account
         *    投资次数  invest_times
         *    更新时间  update_time
         *    项目状态有可能变化  item_status
         *
         *
         */
        basItem.setInvestTimes(basItem.getInvestTimes() + 1);
        basItem.setUpdateTime(new Date());
        basItem.setItemOngoingAccount(basItem.getItemOngoingAccount().add(amount));
        if (basItem.getItemOngoingAccount().compareTo(basItem.getItemAccount()) == 0) {
            basItem.setItemScale(BigDecimal.valueOf(100));
            basItem.setItemStatus(ItemStatus.FULL_COMPLETE);
        } else {
            basItem.setItemScale(basItem.getItemOngoingAccount().divide(basItem.getItemAccount(), MathContext.DECIMAL32).divide(BigDecimal.valueOf(1), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)));
        }
        AssertUtil.isTrue(basItemService.update(basItem) < 1, XmjfConstant.OPS_FAILED_MSG);

        /**
         * bus_item_invest 投资记录添加
         */
        BusItemInvest busItemInvest = new BusItemInvest();
        busItemInvest.setUserId(userId);
        busItemInvest.setUpdatetime(new Date());
        busItemInvest.setItemId(itemId);

        // pc 端
        busItemInvest.setInvestType(1);
        busItemInvest.setInvestStatus(0);
        String investOrder = "XMJF_TZ_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + RandomCodesUtils.createRandom(false, 10);
        busItemInvest.setInvestOrder(investOrder);
        busItemInvest.setInvestDealAmount(amount);

        // 定期投资
        busItemInvest.setInvestCurrent(1);
        busItemInvest.setInvestAmount(amount);
        busItemInvest.setCollectPrincipal(amount);
        BigDecimal lx=amount.multiply(basItem.getItemRate().add(basItem.getItemAddRate())).divide(BigDecimal.valueOf(365),MathContext.DECIMAL128).multiply(BigDecimal.valueOf(basItem.getItemCycle())).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_HALF_UP);
        busItemInvest.setCollectAmount(amount.add(lx));
        busItemInvest.setCollectInterest(lx);
        busItemInvest.setAddtime(new Date());
        busItemInvest.setActualUncollectPrincipal(amount.add(lx));
        busItemInvest.setActualUncollectInterest(lx);
        busItemInvest.setActualUncollectAmount(amount);
        busItemInvest.setActualCollectPrincipal(BigDecimal.ZERO);
        busItemInvest.setActualCollectInterest(BigDecimal.ZERO);
        busItemInvest.setActualCollectAmount(BigDecimal.ZERO);
        busItemInvest.setAdditionalRateAmount(BigDecimal.ZERO);
        AssertUtil.isTrue(busItemInvestMapper.insert(busItemInvest) < 1, XmjfConstant.OPS_FAILED_MSG);


        /**
         *  bus_user_stat	用户统计表
         bus_account	用户账户表
         bus_account_log	用户账户操作日志表
         bus_income_stat	用户收益信息表
         bus_user_integral	用户积分表
         bus_integral_log	积分操作日志表
         */
        BusUserStat busUserStat = busUserStatMapper.queryBusUserStatByUserId(userId);
        busUserStat.setInvestCount(busUserStat.getInvestCount() + 1);
        busUserStat.setInvestAmount(busUserStat.getInvestAmount().add(amount));

        AssertUtil.isTrue(busUserStatMapper.update(busUserStat) < 1, XmjfConstant.OPS_FAILED_MSG);

        /**
         * 更新账户金额信息
         */
        busAccount.setUsable(busAccount.getUsable().subtract(amount));
        busAccount.setCash(busAccount.getCash().subtract(amount));
        // 可用+冻结+利息    (本金+利息)
        busAccount.setTotal(busAccount.getTotal().add(lx));
        busAccount.setFrozen(busAccount.getFrozen().add(amount));
        ///代收本金
        busAccount.setWait(busAccount.getWait().add(amount));

        AssertUtil.isTrue(busAccountMapper.update(busAccount) < 1, XmjfConstant.OPS_FAILED_MSG);

        /**
         * 日志记录添加
         */
        BusAccountLog busAccountLog = new BusAccountLog();
        busAccountLog.setWait(busAccount.getWait());
        busAccountLog.setCash(busAccount.getCash());
        busAccountLog.setAddtime(new Date());
        busAccountLog.setFrozen(busAccount.getFrozen());
        busAccountLog.setOperMoney(amount);
        busAccountLog.setRemark("用户PC端投资");
        busAccountLog.setRepay(busAccount.getRepay());

        //支出
        busAccountLog.setBudgetType(2);
        busAccountLog.setOperType("用户投资");
        busAccountLog.setTotal(busAccount.getTotal());
        busAccountLog.setUserId(userId);
        busAccountLog.setUsable(busAccount.getUsable());
        AssertUtil.isTrue(busAccountLogMapper.insert(busAccountLog) < 1, XmjfConstant.OPS_FAILED_MSG);

        /**
         * bus_income_stat	用户收益信息表
         */
        BusIncomeStat busIncomeStat = busIncomeStatMapper.queryBusIncomeStatByUserId(userId);
        busIncomeStat.setTotalIncome(busIncomeStat.getTotalIncome().add(lx));
        busIncomeStat.setWaitIncome(busIncomeStat.getWaitIncome().add(lx));
        AssertUtil.isTrue(busIncomeStatMapper.update(busIncomeStat) < 1, XmjfConstant.OPS_FAILED_MSG);


        // 更新用户积分
        BusUserIntegral busUserIntegral = busUserIntegralMapper.queryBusUserIntegralByUserId(userId);
        busUserIntegral.setTotal(busUserIntegral.getTotal() + 100);
        busUserIntegral.setUsable(busUserIntegral.getUsable() + 100);

        AssertUtil.isTrue(busUserIntegralMapper.update(busUserIntegral) < 1, XmjfConstant.OPS_FAILED_MSG);

        // 添加积分变动日志信息
        BusIntegralLog busIntegralLog = new BusIntegralLog();
        busIntegralLog.setAddtime(new Date());
        busIntegralLog.setIntegral(100);
        busIntegralLog.setStatus(0);
        busIntegralLog.setUserId(userId);
        busIntegralLog.setWay("用户投资");

        AssertUtil.isTrue(busIntegralLogMapper.insert(busIntegralLog) < 1, XmjfConstant.OPS_FAILED_MSG);

        /**
         * 清除缓存
         */
        Set<String> keys=redisTemplate.keys("itemList*");
        keys.add("basItem::itemId::"+itemId);
        redisTemplate.delete(keys);

        /**
         * 短信通知
         */
        BasUser basUser = userService.queryBasUserByUserId(userId);
        // smsService.sendSms(basUser.getMobile(),XmjfConstant.SMS_INVEST_SUCCESS_NOTIFY_TYPE);
    }
}
