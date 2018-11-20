package com.shsxt.xmjf.server.service;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.BusiException;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.*;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.api.service.ISmsService;
import com.shsxt.xmjf.api.service.IUserService;
import com.shsxt.xmjf.api.utils.*;
import com.shsxt.xmjf.server.db.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @Resource(name="redisTemplate")
    private ValueOperations valueOperations;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private IBasUserSecurityService basUserSecurityService;


    /**
     * 根据用户id查询用户
     * 添加redis增加访问速度，优化用户体验
     * @param userId
     * @return
     */
    @Override
    public User queryUserByUserId(Integer userId) {
        /**
         * redis的实现思路
            //先到redis中查询
            //存在获取数据
            //不存在，到数据库中查询，
            //存在，存数据到redis
            //不存在，不处理
         */

        //获取redis的key
        String key = "user::userId::"+userId;

        //获取存到redis中对应key的user
        User user = (User) valueOperations.get(key);

        //判断user
        if (null == user){
            //根据id查询数据库中的user
            user = userDao.queryUserByUserId(userId);
            if (null != user){
                //数据库中存在user，将对应user存值到redis
                valueOperations.set(key,user);
            }
        }

        return user;
    }

    /**
     *  验证手机号的唯一性
     *  利用手机号码，查询用户
     *  加入redis缓存
     * @param phone
     * @return
     */
    @Override
    public BasUser queryBasUserByPhone(String phone) {
        String key = "user::phone::" + phone;
        BasUser basUser = (BasUser) valueOperations.get(key);
        if (null == basUser) {
           basUser = basUserMapper.queryBasUserByPhone(phone);
           if (basUser != null){
               valueOperations.set(key,basUser);
           }
        }
        return basUser;
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
     * 更新用户认证信息
     * @param realName
     * @param cardNo
     * @param userId
     * @param busiPassword 交易密码
     * @return
     */
    @Override
    public ResultInfo updateBasUserSecurityInfo(String realName, String cardNo, Integer userId, String busiPassword) {
        ResultInfo resultInfo = new ResultInfo();

        try {
            //参数验证
            checkParamsRealInfo(realName,cardNo,busiPassword);

            BasUserSecurity basUserSecurity = basUserSecurityService.queryBasUserSecurityByUserId(userId);
            AssertUtil.isTrue(basUserSecurity.getRealnameStatus() == 1 ,"当前用户已实名");

            //调用第三方接口
            doAuth(realName,cardNo);

            //更新用户信息安全表
            basUserSecurity.setIdentifyCard(cardNo);
            basUserSecurity.setRealname(realName);
            basUserSecurity.setRealnameStatus(1);
            basUserSecurity.setVerifyTime(new Date());
            basUserSecurity.setPaymentPassword(MD5.toMD5(busiPassword));

            AssertUtil.isTrue(basUserSecurityService.updateBasUserSecurity(basUserSecurity) < 1,
                    XmjfConstant.OPS_FAILED_MSG);

        } catch (BusiException e) {
            e.printStackTrace();
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("认证未通过!");
        }
        return resultInfo;
    }

    /**
     * 检查用户实名认证状态
     * @param userId
     * @return
     */
    @Override
    public ResultInfo checkRealNameStatus(Integer userId) {
        BasUserSecurity basUserSecurity = basUserSecurityService.queryBasUserSecurityByUserId(userId);
        ResultInfo resultInfo = new ResultInfo("已实名");
        if (basUserSecurity.getRealnameStatus() != 1){
            resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
            resultInfo.setMsg("用户未实名");
        }
        return resultInfo;
    }


    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    @Override
    public BasUser queryBasUserByUserId(Integer userId) {
        return basUserMapper.queryById(userId);
    }

    /**
     * 第三方身份证认证接口
     * @param realName
     * @param cardNo
     */
    private void doAuth(String realName, String cardNo) throws Exception {
        String host = XmjfConstant.SM_HOST;
        String path = XmjfConstant.SM_PATH;
        String method = XmjfConstant.SM_REQUEST_TYPE;
        String appcode = XmjfConstant.SM_CODE;
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("cardNo", cardNo);
        bodys.put("realName", realName);
        HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        System.out.println(response.toString());

        //转换认证信息为JsonOBJ
        JSONObject jsonObject=JSON.parseObject(EntityUtils.toString(response.getEntity()));
        AssertUtil.isTrue(jsonObject.getInteger("error_code")!=0,jsonObject.getString("reason"));

    }

    /**
     * 用户认证参数校验
     * @param realName
     * @param cardNo
     * @param busiPassword
     */
    private void checkParamsRealInfo(String realName, String cardNo, String busiPassword) {
    AssertUtil.isTrue(StringUtils.isBlank(realName),"请输入真实姓名");
    AssertUtil.isTrue(StringUtils.isBlank(cardNo),"请输入身份证号");
    AssertUtil.isTrue(cardNo.length() != 18,"身份证号长度不合法");
    AssertUtil.isTrue(StringUtils.isBlank(busiPassword),"请输入交易密码");

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
