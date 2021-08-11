package com.fintech.insurance.micro.dto.common;

import java.io.Serializable;

/**
 * 微信授权或者成功绑定帐户之后返回的token信息
 */
public class ClientTokenVO implements Serializable {

    private String token;

    private long tokenExpireSeconds = 0;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenExpireSeconds() {
        return tokenExpireSeconds;
    }

    public void setTokenExpireSeconds(long tokenExpireSeconds) {
        this.tokenExpireSeconds = tokenExpireSeconds;
    }
}
