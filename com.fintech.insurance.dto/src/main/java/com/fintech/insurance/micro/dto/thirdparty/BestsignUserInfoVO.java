package com.fintech.insurance.micro.dto.thirdparty;

import javax.validation.constraints.NotNull;

/**
 * @Description: 封装个人上上签账户注册数据
 * @Author: Yong Li
 * @Date: 2017/11/25 11:11
 */
public class BestsignUserInfoVO {

    /**
     * 用户姓名
     */
    @NotNull(message="106301")
    private String userName;

    /**
     * 用户身份证号码
     */
    @NotNull(message="106301")
    private String userIdentityNum;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 用户所在省份
     */
    private String provinceOfAddress;

    /**
     * 用户所在城市
     */
    private String cityOfAddress;

    /**
     * 用户所在详细地址
     */
    private String detailAddress;

    @Override
    public String toString() {
        return "BestsignUserInfoVO{" +
                "userName='" + userName + '\'' +
                ", userIdentityNum='" + userIdentityNum + '\'' +
                ", mobile='" + mobile + '\'' +
                ", mail='" + mail + '\'' +
                ", provinceOfAddress='" + provinceOfAddress + '\'' +
                ", cityOfAddress='" + cityOfAddress + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIdentityNum() {
        return userIdentityNum;
    }

    public void setUserIdentityNum(String userIdentityNum) {
        this.userIdentityNum = userIdentityNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getProvinceOfAddress() {
        return provinceOfAddress;
    }

    public void setProvinceOfAddress(String provinceOfAddress) {
        this.provinceOfAddress = provinceOfAddress;
    }

    public String getCityOfAddress() {
        return cityOfAddress;
    }

    public void setCityOfAddress(String cityOfAddress) {
        this.cityOfAddress = cityOfAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
}
