package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2018/1/9 9:41
 */
public enum YjfDebtInfoPrefix {

    REQUSITION("Req:"),
    REPAYMENTPLAN("Plans:");

    private String code;

    private YjfDebtInfoPrefix(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getSuffix() {
        return this.code + ":";
    }
}
