package com.fintech.insurance.micro.dto.common;

import java.io.Serializable;

public class OauthAccountMissingVO implements Serializable {

    private String appid;

    private String userValidationUrl;

    private String finsuranceMpUser;

    public String getUserValidationUrl() {
        return userValidationUrl;
    }

    public void setUserValidationUrl(String userValidationUrl) {
        this.userValidationUrl = userValidationUrl;
    }

    public String getFinsuranceMpUser() {
        return finsuranceMpUser;
    }

    public void setFinsuranceMpUser(String finsuranceMpUser) {
        this.finsuranceMpUser = finsuranceMpUser;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
