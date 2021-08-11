package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 实体类型
 */
public enum EntityType {

    REQUISITION("requisition", "申请单");

    private String code;

    private String desc;

    private EntityType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static EntityType codeOf(String code) {
        for (EntityType status : EntityType.values()) {
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
