package com.fintech.insurance.micro.thirdparty.service.bestsign.enums;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/20 19:23
 */
public enum UserType {

    PERSONAL("1"),
    ENTERPRISE("2");

    private String code;

    private UserType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
