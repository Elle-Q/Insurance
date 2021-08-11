package com.fintech.insurance.micro.dto.customer;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: qxy
 * @Description: 客户意向咨询VO
 * @Date: 2017/11/20 16:51
 */
public class CustomerConsultationVO implements Serializable {

    //id
    private Integer id;

    //客户手机号
    private String mobile;

    //客户姓名
    private String name;

    //提交时间
    private Date submmitTime;

    //借款原因
    private String borrowReason;

    //授权类型
    private String oauthType;

    //app_id
    private String oauthAppId;

    //授权账户
    private String oauthAccount;

    private String wxUnioinId;

    public String getOauthType() {
        return oauthType;
    }

    public void setOauthType(String oauthType) {
        this.oauthType = oauthType;
    }

    public String getOauthAppId() {
        return oauthAppId;
    }

    public void setOauthAppId(String oauthAppId) {
        this.oauthAppId = oauthAppId;
    }

    public String getOauthAccount() {
        return oauthAccount;
    }

    public void setOauthAccount(String oauthAccount) {
        this.oauthAccount = oauthAccount;
    }

    public String getWxUnioinId() {
        return wxUnioinId;
    }

    public void setWxUnioinId(String wxUnioinId) {
        this.wxUnioinId = wxUnioinId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getSubmmitTime() {
        return submmitTime;
    }

    public void setSubmmitTime(Date submmitTime) {
        this.submmitTime = submmitTime;
    }

    public String getBorrowReason() {
        return borrowReason;
    }

    public void setBorrowReason(String borrowReason) {
        this.borrowReason = borrowReason;
    }
}
