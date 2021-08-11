package com.fintech.insurance.micro.dto.common;

import java.io.Serializable;

public class WechatQRCodeVO implements Serializable {

    private String url;

    private String base64Iamge;

    private long expireAt;

    private String entityType;

    private Integer entityId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(long expireAt) {
        this.expireAt = expireAt;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getBase64Iamge() {
        return base64Iamge;
    }

    public void setBase64Iamge(String base64Iamge) {
        this.base64Iamge = base64Iamge;
    }
}
