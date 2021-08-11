package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InterestType {

    INTEREST_BY_DAYS("by_days", "按日计息"),
    INTEREST_BY_MONTHS("by_months", "按月计息");

    private String code;

    private String desc;

    private InterestType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static InterestType codeOf(String code) {
        for (InterestType status : InterestType.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalStateException("Not found the mapping InterestType for code:" + code);
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
