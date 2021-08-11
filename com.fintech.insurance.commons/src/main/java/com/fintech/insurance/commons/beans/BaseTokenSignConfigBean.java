package com.fintech.insurance.commons.beans;

import org.springframework.beans.factory.annotation.Value;

public abstract class BaseTokenSignConfigBean {

    /**
     * token的加密
     */
    @Value("${fintech.insurance.security.mp-token-sign-key:pX9a5WTiCRvx1cE6REa0kCq86uFewHWYhYOlejW2h9F3trijLu1yEh3NqqQxgaLODkTNVAoYMtvzoBF9vJc8AI15eZuKY17yqAcywqFa6fcU6IlIiZ4rYpzgDSoEkVfc}")
    protected String mpTokenSignKey;

    /**
     * token的过期时间
     */
    @Value("${fintech.insurance.security.mp-token-expire-seconds:604800}")
    protected int mpTokenExpireSeconds;

    /**
     * 证书签发者
     */
    @Value("${fintech.insurance.security.mp-token-issuer:xinlebao}")
    protected String mpTokenIssuer;

    public String getMpTokenSignKey() {
        return mpTokenSignKey;
    }

    public void setMpTokenSignKey(String mpTokenSignKey) {
        this.mpTokenSignKey = mpTokenSignKey;
    }

    public int getMpTokenExpireSeconds() {
        return mpTokenExpireSeconds;
    }

    public void setMpTokenExpireSeconds(int mpTokenExpireSeconds) {
        this.mpTokenExpireSeconds = mpTokenExpireSeconds;
    }

    public String getMpTokenIssuer() {
        return mpTokenIssuer;
    }

    public void setMpTokenIssuer(String mpTokenIssuer) {
        this.mpTokenIssuer = mpTokenIssuer;
    }
}
