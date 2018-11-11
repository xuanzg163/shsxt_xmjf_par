package com.shsxt.xmjf.api.exceptions;

import com.shsxt.xmjf.api.constants.XmjfConstant;

/**
 * 用户未登陆异常
 * @author zhangxuan
 * @date 2018/11/11
 * @time 21:37
 */
public class NoLoginException extends RuntimeException {
    private Integer code = XmjfConstant.NO_LOGIN_CODE;
    private String msg = XmjfConstant.NO_LOGIN_MSG;

    public NoLoginException() {
        super(XmjfConstant.NO_LOGIN_MSG);
    }

    public NoLoginException(Integer code) {
        super(XmjfConstant.NO_LOGIN_MSG);
        this.code = code;
    }

    public NoLoginException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public NoLoginException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "NoLoginException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
