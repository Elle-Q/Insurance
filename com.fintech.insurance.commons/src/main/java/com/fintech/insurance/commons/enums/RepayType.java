package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RepayType {
    PRINCIPAL_INTEREST("principal_interest", "等本等息");
    private String code;

    private String desc;

    RepayType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RepayType codeOf(String code) {
        for (RepayType type : RepayType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalStateException("Not found the mapping RepayType for code:" + code);
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
