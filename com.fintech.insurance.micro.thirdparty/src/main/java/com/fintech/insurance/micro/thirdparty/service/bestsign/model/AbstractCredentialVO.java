package com.fintech.insurance.micro.thirdparty.service.bestsign.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/20 20:55
 */
public class AbstractCredentialVO {

    /**
     * 用户帐号
     *
     * 为哪个用户设置证件信息就填该用户的帐号
     */
    @JSONField(name = "account")
    protected String account;

    /**
     * 姓名 需要和证件上登记的姓名一致
     * 或者
     * 企业名称 需要和证件上登记的企业名称一致
     */
    @JSONField(name = "name")
    private String name;

    /**
     * 省份
     */
    @JSONField(name = "province")
    protected String province;

    /**
     * 城市
     */
    @JSONField(name = "city")
    protected String city;

    /**
     * 地址
     */
    @JSONField(name = "address")
    protected String address;

    /**
     * 联系邮箱
     */
    @JSONField(name = "contactMail")
    protected String contactMail;

    /**
     * 联系手机
     */
    @JSONField(name = "contactMobile")
    private String contactMobile;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactMail() {
        return contactMail;
    }

    public void setContactMail(String contactMail) {
        this.contactMail = contactMail;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
