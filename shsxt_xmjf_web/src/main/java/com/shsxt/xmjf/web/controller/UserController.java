package com.shsxt.xmjf.web.controller;

import com.shsxt.xmjf.api.constants.XmjfConstant;
import com.shsxt.xmjf.api.exceptions.BusiException;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.po.User;
import com.shsxt.xmjf.api.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author zhangxuan
 * @date 2018/11/8
 * @time 13:59
 */

@Controller
public class UserController {

    @Resource
    private IUserService userService;

    @GetMapping("user/{userId}")
    @ResponseBody
    public User queryUserByUserId(@PathVariable Integer userId) {
        return userService.queryUserByUserId(userId);
    }

    /**
     * 用户注册
     * @param phone
     * @param password
     * @param code
     * @return
     */
    @RequestMapping("user/register")
    @ResponseBody
    public ResultInfo register(String phone,String password,String code) {
        ResultInfo resultInfo = new ResultInfo();

        try {
            userService.saveUser(phone,password,code);
        } catch (BusiException e) {
            e.printStackTrace();
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(XmjfConstant.OPS_FAILED_MSG);
        }

        return resultInfo;
    }

    @RequestMapping("user/quickLogin")
    @ResponseBody
    public ResultInfo quickLogin(String phone,String code) {
        ResultInfo resultInfo = new ResultInfo();
        try {
            userService.quickLogin(phone,code);
        } catch (BusiException e) {
            e.printStackTrace();
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(XmjfConstant.OPS_FAILED_CODE);
            resultInfo.setMsg(XmjfConstant.OPS_FAILED_MSG);
        }
        return resultInfo;
    }
}
