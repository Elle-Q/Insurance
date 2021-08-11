package com.fintech.insurance.commons.enums;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 用户状态
 */
public enum SysUserStatus {
    NORMAL("01", "正常"),
    FROZEN("02", "冻结");

    private String code;

    private String desc;

    private SysUserStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SysUserStatus codeOf(String code) {
        for (SysUserStatus status : SysUserStatus.values()) {
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
