package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomerStatus {
    NORMAL("normal", "正常"),
    FREEZE("freeze", "冻结");

    private String code;
    private String description;

    CustomerStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static CustomerStatus codeOf(String code) {
        for (CustomerStatus status : CustomerStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalStateException("Not found the mapping TradeStatus for code:" + code);
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
