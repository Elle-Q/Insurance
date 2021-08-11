package com.fintech.insurance.micro.thirdparty.service.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: 阿里云短信产品的配置
 * @Author: East
 * @Date: 2017/11/14 0014 14:16
 */
@Component
@ConfigurationProperties(prefix = "aliyuncs")
public class AliyuncsProperties {

    /**
     * 产品名称：云通信短信API产品,开发者无需替换
     */
    private String smsProduct;
    /**
     * 产品域名，开发者无需替换
     */
    private String smsDomain;
    /**
     * aliyun云服务所属的地域 ID，暂不支持region化
     */
    private String regionId;

    /**
     * 秘钥Key
     */
    private String accessKeyId;
    /**
     * 秘钥
     */
    private String accessKeySecret;

    public AliyuncsProperties() {
    }

    public String getSmsProduct() {
        return smsProduct;
    }

    public void setSmsProduct(String smsProduct) {
        this.smsProduct = smsProduct;
    }

    public String getSmsDomain() {
        return smsDomain;
    }

    public void setSmsDomain(String smsDomain) {
        this.smsDomain = smsDomain;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
