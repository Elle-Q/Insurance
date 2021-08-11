package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 用户类型
 */
public enum UserType {
    CHANNEL("channel", "渠道"),
    STAFF("staff", "员工"),
    CUSTOMER("customer", "客户");

    private String code;

    private String desc;

    private UserType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserType codeOf(String code) {
        for (UserType type : UserType.values()) {
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
