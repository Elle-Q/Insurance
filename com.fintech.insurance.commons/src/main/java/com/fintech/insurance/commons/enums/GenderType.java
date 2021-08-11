package com.fintech.insurance.commons.enums;


import com.fasterxml.jackson.annotation.JsonValue;

public enum GenderType {

    FEMALE("01", "女"),
    MALE("02", "男");

    private String code;

    private String desc;

    private GenderType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static GenderType codeOf(String code) {
        for (GenderType status : GenderType.values()) {
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

}
