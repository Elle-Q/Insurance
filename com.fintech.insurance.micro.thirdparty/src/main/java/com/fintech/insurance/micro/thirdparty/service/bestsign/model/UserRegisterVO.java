package com.fintech.insurance.micro.thirdparty.service.bestsign.model;

import com.fintech.insurance.micro.thirdparty.service.bestsign.enums.UserType;

/**
 * @Description: 用户注册请求封装
 * @Author: Yong Li
 * @Date: 2017/11/20 19:21
 */
public class UserRegisterVO {

    /**
     * 用户帐号
     * 用户的唯一标识，可以是邮箱、手机号、证件号等不限，建议使用邮箱或手机号
     */
    private String account;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户类型: 1表示个人，2表示企业
     */
    private UserType userType;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 手机号
     */
    private String mobile;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
