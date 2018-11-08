package com.shsxt.xmjf.api.utils;

import java.util.regex.Pattern;

public class PhoneUtil {
    public static final String REGEX_MOBILE="^((17[0-9])|(14[0-9])|(13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
    public  static  Boolean checkPhone(String phone){
        return Pattern.matches(REGEX_MOBILE, phone);
    }

    /**
     * 测试方法
     * @param args
     *//*
    public static void main(String[] args) {
        System.out.println(checkPhone("18411111111"));
    }*/
}
