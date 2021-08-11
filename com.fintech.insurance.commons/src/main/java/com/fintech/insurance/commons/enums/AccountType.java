package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/20 10:49
 */
public enum AccountType {
    LOAN("loan", "放款"), REFUND("refund", "还款"), SERVICEFEE("servicefee", "服务费");

    private String code;
    private String description;

    AccountType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static AccountType fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (AccountType type : AccountType.values()) {
            if (code.equals(type.getCode())) {
                return type;
            }
        }

        throw new IllegalArgumentException();
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
