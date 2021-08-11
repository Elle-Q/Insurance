package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

public enum ItemType {
    GUARANTEE("guarantee", "保证金"),
    INITIAL_PAYMENT_FEE("initial_payment_fee", "期初款"),
    SERVICEFEE("servicefee", "服务费");

    private String code;
    private String description;

    ItemType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ItemType fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (ItemType type : ItemType.values()) {
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
