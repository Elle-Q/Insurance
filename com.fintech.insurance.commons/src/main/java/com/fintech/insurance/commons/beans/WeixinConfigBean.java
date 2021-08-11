package com.fintech.insurance.commons.beans;


/**
 * 微信配置Bean
 */
public class WeixinConfigBean {

    /**
     * 微信的appid
     */
    private String appid;

    /**
     * 微信的app secret
     */
    private String appSecret;

    /**
     * 微信接入设置的token
     */
    private String token;

    /**
     * 微信消息加密盐
     */
    private String encodingAesKey;

    /**
     * 微信消息加密方式
     */
    private String encodingType;

    /**
     * 默认授权后跳转的地址
     */
    private String indexUrl;

    /**
     * 用户验证手机号码的页面
     */
    private String userValidationUrl;

    /**
     * 授权完成后的回调中转地址
     */
    private String callbackUrl;

    /**
     * 授权完成后的前端回调地址
     */
    private String redirectCallbackUrl;

    /**
     * 临时二维码过期时间
     */
    private int tmpQrcodeExpireSecond = 30;

    /**
     * 申请单详情页面地址
     */
    private String requisitionDetailUrl;

    /**
     * 我的分期列表
     */
    private String mpContractListUrl;

    /**
     * 我的页面地址
     */
    private String userCenterUrl;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("app id = ").append(this.appid).append(", ");
        sb.append("app secret = ").append(this.appSecret).append(", ");
        sb.append("token = ").append(this.token).append(", ");
        sb.append("encoding aes key = ").append(this.encodingAesKey).append(", ");
        sb.append("encoding type = ").append(this.encodingType).append(", ");
        sb.append("index url = ").append(this.indexUrl).append(", ");
        sb.append("user validation url = ").append(this.userValidationUrl).append(", ");
        sb.append("callback url = ").append(this.callbackUrl).append(", ");
        sb.append("redirect callback url = ").append(this.redirectCallbackUrl).append(", ");
        sb.append("tmp qrcode expire second = ").append(this.tmpQrcodeExpireSecond).append(", ");
        return sb.toString();
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getIndexUrl() {
        return this.indexUrl;
    }

    public void setUserValidationUrl(String userValidationUrl) {
        this.userValidationUrl = userValidationUrl;
    }

    public String getUserValidationUrl() {
        return this.userValidationUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getRedirectCallbackUrl() {
        return redirectCallbackUrl;
    }

    public void setRedirectCallbackUrl(String redirectCallbackUrl) {
        this.redirectCallbackUrl = redirectCallbackUrl;
    }

    public int getTmpQrcodeExpireSecond() {
        return tmpQrcodeExpireSecond;
    }

    public void setTmpQrcodeExpireSecond(int tmpQrcodeExpireSecond) {
        this.tmpQrcodeExpireSecond = tmpQrcodeExpireSecond;
    }

    public String getRequisitionDetailUrl() {
        return requisitionDetailUrl;
    }

    public void setRequisitionDetailUrl(String requisitionDetailUrl) {
        this.requisitionDetailUrl = requisitionDetailUrl;
    }

    public String getMpContractListUrl() {
        return mpContractListUrl;
    }

    public void setMpContractListUrl(String mpContractListUrl) {
        this.mpContractListUrl = mpContractListUrl;
    }

    public String getUserCenterUrl() {
        return userCenterUrl;
    }

    public void setUserCenterUrl(String userCenterUrl) {
        this.userCenterUrl = userCenterUrl;
    }
}
