package com.fintech.insurance.micro.vo.wechat;

import javax.validation.constraints.NotNull;

public class LoginVO extends VerificationCodeVO{

    //登录手机号
    @NotNull(message = "106301")
    private String phoneNumber;

    @NotNull(message = "106301")
    private String appid;

    @NotNull(message = "106301")
    private String finsuranceMpUser;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getFinsuranceMpUser() {
        return finsuranceMpUser;
    }

    public void setFinsuranceMpUser(String finsuranceMpUser) {
        this.finsuranceMpUser = finsuranceMpUser;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
