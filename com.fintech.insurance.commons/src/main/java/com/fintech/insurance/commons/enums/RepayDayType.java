package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RepayDayType {

    INITIAL_PAYMENT("initial_payment", "期初还款"),
    FINAL_PAYMENT("final_payment", "期末还款");

    private String code;

    private String desc;

    private RepayDayType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RepayDayType codeOf(String code) {
        for (RepayDayType type : RepayDayType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalStateException("Not found the mapping RepayDayType for code:" + code);
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
