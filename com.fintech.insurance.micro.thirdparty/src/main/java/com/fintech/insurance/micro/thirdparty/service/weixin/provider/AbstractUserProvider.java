package com.fintech.insurance.micro.thirdparty.service.weixin.provider;

import com.fintech.insurance.commons.beans.BaseTokenSignConfigBean;

import java.util.Date;

public abstract class AbstractUserProvider extends BaseTokenSignConfigBean implements UserProvider {

    public static final String TOKEN_TYPE = "jwt";


    /**
     * 获得token的过期时间
     * @param from
     * @return
     */
    protected Date getTokenExpireDate(Date from) {
        if (from == null) {
            from = new Date();
        }
        long expireTime = from.getTime() + this.mpTokenExpireSeconds * 1000;
        return new Date(expireTime);
    }
}
