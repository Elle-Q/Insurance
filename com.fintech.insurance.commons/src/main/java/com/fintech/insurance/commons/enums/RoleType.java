package com.fintech.insurance.commons.enums;


import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleType {
    ADMIN("admin", "管理员"),
    AUDITOR("auditor", "审核员"),
    OPERATOR("operator", "运营"),
    RISKER("risker", "风控"),
    TREASURER("treasurer", "财务"),
    LEADER("leader", "领导");

    private String code;

    private String desc;

    private RoleType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RoleType codeOf(String code) {
        for (RoleType type : RoleType.values()) {
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
