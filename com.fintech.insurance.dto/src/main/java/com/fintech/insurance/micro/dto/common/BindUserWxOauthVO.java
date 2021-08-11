package com.fintech.insurance.micro.dto.common;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

public class BindUserWxOauthVO {

    private Integer userId;

    private String appid;

    private WxMpUser wxMpUser;

    public BindUserWxOauthVO(Integer userId, String appid, WxMpUser wxMpUser) {
        this.userId = userId;
        this.appid = appid;
        this.wxMpUser = wxMpUser;
    }

    public BindUserWxOauthVO() {}

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public WxMpUser getWxMpUser() {
        return wxMpUser;
    }

    public void setWxMpUser(WxMpUser wxMpUser) {
        this.wxMpUser = wxMpUser;
    }
}
