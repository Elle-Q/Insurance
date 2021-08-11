package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OperationType {

    AUDIT("audit", "审核"),
    PAID("paid", "确认已支付"),
    REFUND("refund", "确认还款"),
    CANCELED("canceled", "取消申请"),
    LOANED("loaned", "确认已放款");

    private String code;
    private String desc;

    public static OperationType codeOf(String code) {
        for (OperationType type : OperationType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalStateException("Not found the mapping RepayDayType for code:" + code);
    }

    private OperationType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return this.code;
    }

}
