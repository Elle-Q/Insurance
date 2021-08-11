package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Description: 当前系统的开发环境profile
 * @Author: Yong Li
 * @Date: 2017/12/20 8:54
 */
public enum SystemProfile {

    DEFAULT("", "本地开发"),
    JUNIT("junit", "单元测试"),
    DEV("dev", "联调环境"),
    TEST("test", "测试环境"),
    PROD("product", "生产环境");

    private String code;

    private String desc;

    private SystemProfile(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SystemProfile codeOf(String code) {
        for (SystemProfile p : SystemProfile.values()) {
            if (p.code.equals(code)) {
                return p;
            }
        }
        return null;
        //throw new IllegalStateException("Can not covert the profile:" + code + " to correct SystemProfile.");
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return this.code;
    }

}
