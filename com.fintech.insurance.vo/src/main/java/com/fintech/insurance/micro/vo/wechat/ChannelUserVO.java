package com.fintech.insurance.micro.vo.wechat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 渠道账户VO
 * @author qxy
 */
public class ChannelUserVO{

    //渠道用户id
    private Integer id;

    //渠道用户名称
    @NotNull(message = "106301")
    private String name;

    //是否冻结标志位
    private Integer isLocked;

    //手机号
    @NotNull(message = "106301")
    private String mobile;

    //是否为渠道管理员
    private Integer isChannelAdmin;

    //用户类型
    private String userType;

    //渠道编码
    private String channelCode;

    //token
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getIsChannelAdmin() {
        return isChannelAdmin;
    }

    public void setIsChannelAdmin(Integer isChannelAdmin) {
        this.isChannelAdmin = isChannelAdmin;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }
}
