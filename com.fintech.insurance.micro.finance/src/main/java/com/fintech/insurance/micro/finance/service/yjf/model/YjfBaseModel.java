package com.fintech.insurance.micro.finance.service.yjf.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/6 14:40
 */
public class YjfBaseModel {

    /**
     * 协议类型: 报文协议格式, httpPost(默认)
     *
     */
    protected String protocol = "HTTP_FORM_JSON";

    /**
     * 服务代码:  接口服务代码, 请根据接口服务定义填写
     */
    @JSONField(name = "service")
    protected String serviceCode;

    /**
     * 服务版本: 默认1.0
     */
    protected String version = "1.0";

    /**
     * 商户ID: 签约的服务平台账号对应的合作方ID（由笨熊金服提供），定长20字符
     */
    protected String partnerId;

    /**
     * 请求流水号: 服务平台合作商户网站唯一流水号。长度：20-40字符
     */
    @JSONField(name = "orderNo")
    protected String platformSerialNum;

    /**
     * 签名方式: 目前支持MD5
     */
    protected String signType = "MD5";

    /**
     * 签名: 签名串
     */
    protected String sign;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPlatformSerialNum() {
        return platformSerialNum;
    }

    public void setPlatformSerialNum(String platformSerialNum) {
        this.platformSerialNum = platformSerialNum;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
