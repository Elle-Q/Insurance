package com.fintech.insurance.commons.enums;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 *  资源类型
 */
public enum ResourceType{
    BUSI_INSURANCE("busi_insurance", "商业保单"),
    DRIVER_INSURANCE("drive_insurance", "交强险保单"),
    TAX_INSURANCE("tax_insurance", "车船税保单"),
    DRIVING_LICENSE("driving_license", "行驶证"),
    OTHER_MATERIAL("other_material", "其他材料");

    private String code;

    private String desc;

    private ResourceType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ResourceType codeOf(String code) {
        for (ResourceType type : ResourceType.values()) {
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
