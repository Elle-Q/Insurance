package com.fintech.insurance.micro.customer.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;

/**
 * @Author: Clayburn
 * @Description: 客户帐号授权信息表
 * @Date: 2017/11/13 15:02
 */
@Entity
@Table(name = "cust_account_oauth")
public class CustomerAccountOauth extends BaseEntity {
    @Column(name = "oauth_type", columnDefinition = "varchar(16) not null comment '授权类型，见用户授权信息表描述'")
    private String oauthType;

    @Column(name = "oauth_app_id", columnDefinition = "varchar(64) not null comment '第三方应用id'")
    private String oauthAppId;

    @Column(name = "oauth_account", columnDefinition = "varchar(64) not null comment '第三方帐号id'")
    private String oauthAccount;

    @Column(name = "nick_name", columnDefinition = "varchar(32) comment '第三方帐号信息中的昵称'")
    private String nickName;

    @Column(name = "gender", columnDefinition = "varchar(16) comment '第三方帐号信息中的性别'")
    private String gender;

    @Column(name = "header_image", columnDefinition = "tinytext comment '第三方帐号信息中的头像'")
    private String headerImage;

    @Column(name = "wx_unionid", columnDefinition = "varchar(64) comment '见用户授权信息表描述'")
    private String wxUnionId;

    @Column(name = "oauth_content", columnDefinition = "text comment '完整的第三方授权信息'")
    private String oauthContent;

    @JoinColumn(name = "account_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private CustomerAccount customerAccount;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }

    public String getOauthContent() {
        return oauthContent;
    }

    public void setOauthContent(String oauthContent) {
        this.oauthContent = oauthContent;
    }

    public CustomerAccount getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount customerAccount) {
        this.customerAccount = customerAccount;
    }
}
