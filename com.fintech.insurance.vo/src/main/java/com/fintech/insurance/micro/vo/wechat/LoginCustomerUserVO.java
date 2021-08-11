package com.fintech.insurance.micro.vo.wechat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class LoginCustomerUserVO implements Serializable {

    //渠道用户名称
    private String name;

    //是否冻结标志位
    private Integer isLocked;

    //身份证
    private String IdNum;

    //用户头像
    private String headImgUrl;

    //客户是否已经绑过银行卡
    private Integer hasBindCard;

    public Integer getHasBindCard() {
        return hasBindCard;
    }

    public void setHasBindCard(Integer hasBindCard) {
        this.hasBindCard = hasBindCard;
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

    public String getIdNum() {
        return IdNum;
    }

    public void setIdNum(String idNum) {
        IdNum = idNum;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}
