package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/4 16:55
 */
public enum RequestSourceType {

    ADMIN("admin", "运营管理平台"),
    MP("mp", "微信端"),
    FEIGN("feign", "Spring cloud Feign客户端");


    private String code;
    private String desc;

    private RequestSourceType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
