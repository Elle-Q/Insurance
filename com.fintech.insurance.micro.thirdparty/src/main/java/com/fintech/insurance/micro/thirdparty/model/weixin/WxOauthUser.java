package com.fintech.insurance.micro.thirdparty.model.weixin;

import java.io.Serializable;

public class WxOauthUser implements Serializable {

    private Integer id;

    private String userType;

    private boolean foundByOpenid = false;

    private boolean foundByUnionid = false;

    private boolean isLocked = false;

    private String channelCode;

    private Integer organizationId;

    private boolean isChannelAdmin = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isFoundByOpenid() {
        return foundByOpenid;
    }

    public void setFoundByOpenid(boolean foundByOpenid) {
        this.foundByOpenid = foundByOpenid;
    }

    public boolean isFoundByUnionid() {
        return foundByUnionid;
    }

    public void setFoundByUnionid(boolean foundByUnionid) {
        this.foundByUnionid = foundByUnionid;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return this.userType;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public boolean isChannelAdmin() {
        return isChannelAdmin;
    }

    public void setChannelAdmin(boolean channelAdmin) {
        isChannelAdmin = channelAdmin;
    }
}
