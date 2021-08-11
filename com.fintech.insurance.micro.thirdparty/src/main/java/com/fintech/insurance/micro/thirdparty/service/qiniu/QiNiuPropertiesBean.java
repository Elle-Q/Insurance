package com.fintech.insurance.micro.thirdparty.service.qiniu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: (some words)
 * @Author: Administrator
 * @Date: 2017/11/10 0010 16:00
 */
@Component
@ConfigurationProperties(prefix = "qiniu")
public class QiNiuPropertiesBean {
    private String accessKey;//账户名
    private String secretKey;//密码
    private String publicBucket;//公有的空间
    private String publicBucketHost;//公有的访问主机
    private String privateBucket;//私有的空间
    private String privateBucketHost;//私有的访问主机
    private Long tokenExpireMinute;//koken超时时间
    private Long privateAccessExpireMinutes;//私有的认证超时时间

    @Value("${qiniu.narrowImage.width}")
    private String narrowImageWidth;
    @Value("${qiniu.narrowImage.height}")
    private String narrowImageHeight;
    @Value("${qiniu.narrowImage.model}")
    private String narrowImageModel;

    public QiNiuPropertiesBean() {
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPublicBucket() {
        return publicBucket;
    }

    public void setPublicBucket(String publicBucket) {
        this.publicBucket = publicBucket;
    }

    public String getPublicBucketHost() {
        return publicBucketHost;
    }

    public void setPublicBucketHost(String publicBucketHost) {
        this.publicBucketHost = publicBucketHost;
    }

    public String getPrivateBucket() {
        return privateBucket;
    }

    public void setPrivateBucket(String privateBucket) {
        this.privateBucket = privateBucket;
    }

    public String getPrivateBucketHost() {
        return privateBucketHost;
    }

    public void setPrivateBucketHost(String privateBucketHost) {
        this.privateBucketHost = privateBucketHost;
    }

    public Long getTokenExpireMinute() {
        return tokenExpireMinute;
    }

    public void setTokenExpireMinute(Long tokenExpireMinute) {
        this.tokenExpireMinute = tokenExpireMinute;
    }

    public Long getPrivateAccessExpireMinutes() {
        return privateAccessExpireMinutes;
    }

    public void setPrivateAccessExpireMinutes(Long privateAccessExpireMinutes) {
        this.privateAccessExpireMinutes = privateAccessExpireMinutes;
    }

    public String getNarrowImageWidth() {
        return narrowImageWidth;
    }

    public void setNarrowImageWidth(String narrowImageWidth) {
        this.narrowImageWidth = narrowImageWidth;
    }

    public String getNarrowImageHeight() {
        return narrowImageHeight;
    }

    public void setNarrowImageHeight(String narrowImageHeight) {
        this.narrowImageHeight = narrowImageHeight;
    }

    public String getNarrowImageModel() {
        return narrowImageModel;
    }

    public void setNarrowImageModel(String narrowImageModel) {
        this.narrowImageModel = narrowImageModel;
    }
}
