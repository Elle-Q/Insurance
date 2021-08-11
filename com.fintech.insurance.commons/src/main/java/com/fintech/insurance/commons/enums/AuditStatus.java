package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 审核状态
 */
public enum AuditStatus {
    PENDING("pending", "审核中"),
    PASS("pass", "审核通过"),
    REJECTED("rejected", "审核驳回"),
    CANCEL("cancel", "取消");

    private String code;
    private String desc;

    private AuditStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AuditStatus codeOf(String code) {
        for (AuditStatus type : AuditStatus.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalStateException("Not found the mapping AuditStatus for code:" + code);
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
