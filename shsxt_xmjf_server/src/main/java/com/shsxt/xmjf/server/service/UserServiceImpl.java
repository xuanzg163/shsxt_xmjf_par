package com.shsxt.xmjf.server.service;


import cn.hutool.core.date.DateUtil;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.*;
import com.shsxt.xmjf.api.service.ISmsService;
import com.shsxt.xmjf.api.service.IUserService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.api.utils.MD5;
import com.shsxt.xmjf.api.utils.PhoneUtil;
import com.shsxt.xmjf.api.utils.RandomCodesUtils;
import com.shsxt.xmjf.server.db.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    @Resource
    private BasUserMapper basUserMapper;

    @Resource
    private BasUserInfoMapper basUserInfoMapper;

    @Resource
    private BasExperiencedGoldMapper basExperiencedGoldMapper;

    @Resource
    private SysLogMapper sysLogMapper;

    @Resource
    private BasUserSecurityMapper basUserSecurityMapper;

    @Resource
    private BusAccountMapper busAccountMapper;

    @Resource
    private BusUserIntegralMapper busUserIntegralMapper;

    @Resource
    private BusIncomeStatMapper busIncomeStatMapper;

    @Resource
    private BusUserStatMapper busUserStatMapper;

    @Resource
    private ISmsService smsService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    public User queryUserByUserId(Integer userId) {
        return userDao.queryUserByUserId(userId);
    }

    /**
     *  验证手机号的唯一性
     *  利用手机号码，查询用户
     * @param phone
     * @return
     */
    @Override
    public BasUser queryBasUserByPhone(String phone) {
        return basUserMapper.queryBasUserByPhone(phone);
    }

    /**
     * 用户注册
     * @param phone
     * @param password
     * @param code 2 注册，1登陆
     */
    @Override
    public void saveUser(String phone, String password,String code) {

        /**
         * 1.参数校验
         *     phone:非空  唯一
         *     密码:非空 长度 至少六位
         *     code:有效  值与缓存值相等
         * 2.表数据初始化
         *       bas_user	用户基本信息
         bas_user_info	用户信息扩展表记录添加


         bas_user_security	用户安全信息表
         bus_account	用户账户表记录信息
         bus_user_integral	用户积分记录
         bus_income_stat	用户收益表记录
         bus_user_stat	用户统计表



         bas_experienced_gold	注册体验金表
         sys_log     系统日志
         3.短信通知
         */

        //参数校验
        checkParams(phone,password,code);

        //表数据初始化
        int userId= initBasUser(phone,password);

        initBasUserInfo(userId,phone);

        initBasExperiencedGold(userId);

        initSysLog(userId);

        initBasUserSecurity(userId);

        initBusAccount(userId);

        initBusUserIntegral(userId);

        initBusIncomeStat(userId);

        initBusUserStat(userId);

//        //发送短信
//        smsService.sendSms(phone,XmjfConstant.SMS_REGISTER_SUCCESS_NOTIFY_TYPE);


    }

    /**
     * 用户登陆
     * @param phone
     * @param password
     * @return
     */
    @Override
    public UserModel login(String phone, String password) {
        UserModel userModel = new UserModel();
        AssertUtil.isTrue(StringUtils.isBlank(phone),"请输入手机号");
        AssertUtil.isTrue(!(PhoneUtil.checkPhone(phone)),"手机号不合法");
        AssertUtil.isTrue(StringUtils.isBlank(password),"请输入密码");

        //依据手机号查询用户
        BasUser basUser = queryBasUserByPhone(phone);

        //获取用户的盐值
        String salt = basUser.getSalt();
        AssertUtil.isTrue(null == basUser,"手机号为注册，请先行注册");
        AssertUtil.isTrue(!(basUser.getPassword().equals(MD5.toMD5(password+salt)))
                ,"用户密码不正确");
        userModel.setUserName(basUser.getUsername());
        userModel.setUserId(basUser.getId());
        userModel.setMobile(basUser.getMobile());

        return userModel;
    }

    /**
     * 用户快速登陆
     * @param phone
     * @param code
     * @return
     */
    @Override
    public UserModel quickLogin(String phone, String code) {
        AssertUtil.isTrue(StringUtils.isBlank(phone),"请输入手机号");
        AssertUtil.isTrue(!(PhoneUtil.checkPhone(phone)),"手机号不合法!");
        AssertUtil.isTrue(StringUtils.isBlank(code),"请输验证码");

        //存储在redis上的key值
        String key="phone::"+phone+"::type::"+ XmjfConstant.SMS_LOGIN_TYPE;
        AssertUtil.isTrue(!(redisTemplate.hasKey(key)),"验证码不存在或已过期!");
        AssertUtil.isTrue(!(redisTemplate.opsForValue().get(key).toString().equals(code)),"验证码不正确!");
        BasUser basUser=queryBasUserByPhone(phone);
        AssertUtil.isTrue(null==basUser,"该手机号未注册,请先执行注册!");
        UserModel userModel=new UserModel();
        userModel.setUserName(basUser.getUsername());
        userModel.setUserId(basUser.getId());
        userModel.setMobile(basUser.getMobile());
        /**
         *  积分功能
         *    连续登录 3天
         *       奖励 100积分
         *    连续登录5天
         *       奖励 200积分
         *    连续登录 7
         *        500积分
         *    每天均登陆
         *       1000积分
         *    积分达到
         *        1500-2000   设置勋章  1星
         *        2000-2500   设置      1个月
         */
        return userModel;
    }

    /**
     * 初始化 用户统计表
     * @param userId
     */
    private void initBusUserStat(int userId) {
        BusUserStat busUserStat = new BusUserStat();
        busUserStat.setUserId(userId);

        AssertUtil.isTrue(busUserStatMapper.insert(busUserStat) < 1,
                XmjfConstant.OPS_FAILED_MSG );
    }

    /**
     *  初始化 用户收益表记录
     * @param userId
     */
    private void initBusIncomeStat(int userId) {
        BusIncomeStat busIncomeStat = new BusIncomeStat();

        busIncomeStat.setUserId(userId);

        AssertUtil.isTrue(busIncomeStatMapper.insert(busIncomeStat) < 1,
                XmjfConstant.OPS_FAILED_MSG);

    }

    /**
     *  初始化 用户积分记录
     * @param userId
     */
    private void initBusUserIntegral(int userId) {
        BusUserIntegral busUserIntegral = new BusUserIntegral();

        busUserIntegral.setUserId(userId);
        busUserIntegral.setTotal(0);
        busUserIntegral.setUsable(0);

        AssertUtil.isTrue(busUserIntegralMapper.insert(busUserIntegral) < 1,
                XmjfConstant.OPS_FAILED_MSG);

    }

    /**
     * 初始化 用户账户表记录信息
     * @param userId
     */
    private void initBusAccount(int userId) {
        BusAccount busAccount = new BusAccount();
        busAccount.setUserId(userId);
        busAccount.setTotal(BigDecimal.valueOf(0));
        busAccount.setUsable(BigDecimal.valueOf(0));
        busAccount.setCash(BigDecimal.valueOf(0));
        busAccount.setFrozen(BigDecimal.valueOf(0));
        busAccount.setWait(BigDecimal.valueOf(0));
        busAccount.setRepay(BigDecimal.valueOf(0));

        AssertUtil.isTrue(busAccountMapper.insert(busAccount) < 1,
                XmjfConstant.OPS_FAILED_MSG);
    }

    /**
     * 初始化 用户安全信息表
     * @param userId
     */
    private void initBasUserSecurity(int userId) {
        BasUserSecurity basUserSecurity = new BasUserSecurity();
        basUserSecurity.setUserId(userId);
        //手机是否验证
        basUserSecurity.setPhoneStatus(0);
        //邮箱认证
        basUserSecurity.setEmailStatus(0);
        //实名认证
        basUserSecurity.setRealnameStatus(0);

        AssertUtil.isTrue(basUserSecurityMapper.insert(basUserSecurity) < 1,
                XmjfConstant.OPS_FAILED_MSG);

    }

    /**
     * 初始化系统日志
     * @param userId
     */
    private void initSysLog(int userId) {
        SysLog sysLog = new SysLog();
        sysLog.setAddtime(new Date());
        sysLog.setCode("REGISTER");
        sysLog.setOperating("用户注册");
        sysLog.setResult(1);
        // 注册
        sysLog.setType(4);
        sysLog.setUserId(userId);

        AssertUtil.isTrue(sysLogMapper.insert(sysLog) < 1,
                XmjfConstant.OPS_FAILED_MSG);
    }

    /**
     *  初始化注册体验金表
     * @param userId
     */
    private void initBasExperiencedGold(int userId) {
        BasExperiencedGold basExperiencedGold = new BasExperiencedGold();
        basExperiencedGold.setWay("注册获取");
        basExperiencedGold.setUserId(userId);
        // 15天有效
        basExperiencedGold.setUsefulLife(15);
        basExperiencedGold.setStatus(2);
        basExperiencedGold.setGoldName("注册体验金");
        basExperiencedGold.setExpiredTime(DateUtil.offsetDay(new Date(),15));
        basExperiencedGold.setAmount(BigDecimal.valueOf(2888));
        basExperiencedGold.setAddtime(new Date());

        AssertUtil.isTrue(basExperiencedGoldMapper.insert(basExperiencedGold) < 1,
                XmjfConstant.OPS_FAILED_MSG);
    }

    /**
     * 初始化 用户信息扩展表记录添加
     * @param userId
     * @param phone
     */
    private void initBasUserInfo(int userId, String phone) {
        BasUserInfo basUserInfo = new BasUserInfo();
        basUserInfo.setUserId(userId);
        basUserInfo.setCustomerType(0);
        basUserInfo.setInviteCode(phone);
        AssertUtil.isTrue(basUserInfoMapper.insert(basUserInfo) < 1,
                XmjfConstant.OPS_FAILED_MSG);
    }

    /**
     * 基本表初始化
     * @param phone
     * @param password
     * @return
     */
    private int initBasUser(String phone, String password) {
        BasUser basUser = new BasUser();
        basUser.setMobile(phone);
        String salt = RandomCodesUtils.createRandom(false,6);
        basUser.setSalt(salt);
        basUser.setPassword(MD5.toMD5(password+salt));
        basUser.setReferer("PC");
        basUser.setTime(new Date());
        basUser.setAddtime(new Date());
        basUser.setType(1);
        basUser.setStatus(1);
        basUser.setUsername(phone);
        AssertUtil.isTrue(basUserMapper.insert(basUser)<1,XmjfConstant.OPS_FAILED_MSG);

        return basUser.getId();
    }

    /**
     * 参数校验
     *
     * @param phone
     * @param password
     * @param code
     */
    private void checkParams(String phone, String password, String code) {
        AssertUtil.isTrue(StringUtils.isBlank(phone),"请输入手机号");
        AssertUtil.isTrue(!(PhoneUtil.checkPhone(phone)),"手机号不合法");
        AssertUtil.isTrue(null != queryBasUserByPhone(phone),"该手机号已注册");
        AssertUtil.isTrue(StringUtils.isBlank(password),"请输入密码");
        AssertUtil.isTrue(password.length()<6,"密码长度不能小于6");
        AssertUtil.isTrue(StringUtils.isBlank(code),"请输入短信验证码");

        //获取存入redis的key
        String key="phone::"+phone+"::type::"+ XmjfConstant.SMS_REGISTER_TYPE;
        AssertUtil.isTrue(!((redisTemplate.opsForValue().get(key).toString().equals(code))),
                "验证码不正确!");
    }
}
