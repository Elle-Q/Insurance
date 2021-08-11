package com.fintech.insurance.commons.enums;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

//@JsonFormat(shape = JsonFormat.Shape.OBJECT)  不必要
public enum ProductType {

    POLICY_LOANS("policy_loans","DK", "保单贷款"),
    CAR_INSTALMENTS("car_instalments","CK", "车贷分期");

    private String code;
    private String abbreviate;//简写
    private String desc;

    private ProductType(String code, String abbreviate, String desc) {
        this.code = code;
        this.abbreviate = abbreviate;
        this.desc = desc;
    }

    //@JSONCreator 不必要
    public static ProductType codeOf(String code) {
        for (ProductType status : ProductType.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalStateException("Not found the mapping ProductType for code:" + code);
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

    public String getAbbreviate() {
        return abbreviate;
    }

    public void setAbbreviate(String abbreviate) {
        this.abbreviate = abbreviate;
    }

}
