package com.fintech.insurance.micro.vo.wechat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class LoginChannelUserVO implements Serializable {

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

    //渠道编码
    private String channelCode;

    //用户头像
    private String headImgUrl;

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
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

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }
}
