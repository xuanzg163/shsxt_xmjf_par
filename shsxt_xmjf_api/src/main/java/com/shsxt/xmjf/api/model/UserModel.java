package com.shsxt.xmjf.api.model;

import java.io.Serializable;

/**
 * @author zhangxuan
 * @date 2018/11/9
 * @time 20:22
 */

public class UserModel implements Serializable {

    private static final long serialVersionUID = 7766287363830533697L;
    private Integer userId;
    private String userName;
    private String mobile;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
