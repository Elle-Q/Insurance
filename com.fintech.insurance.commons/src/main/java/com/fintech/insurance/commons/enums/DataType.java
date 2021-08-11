package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author: Clayburn
 * @Description: 数据类型
 * @Date: 2017/11/16 11:40
 */
public enum DataType {
    STRING("string", "字符串"),
    NUMBER("number", "数值"),
    DATE("date", "日期"),
    DATETIME("datetime", "日期时间"),
    RATE("rate", "利率");

    private String code;
    private String description;

    DataType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
