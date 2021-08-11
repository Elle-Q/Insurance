package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProcessStatus {
    PENDING("pending", "处理中");

    private String code;
    private String description;

    ProcessStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProcessStatus codeOf(String code) {
        for (ProcessStatus obj : ProcessStatus.values()) {
            if (obj.code.equals(code)) {
                return obj;
            }
        }

        throw new IllegalStateException("Not found the mapping RefundStatus for code:" + code);
    }

    @Override
    public String toString() {
        return this.code;
    }
}
