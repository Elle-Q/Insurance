package com.fintech.insurance.micro.thirdparty.service.bestsign.enums;


import org.apache.commons.lang3.NotImplementedException;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/20 20:30
 */
public enum IdentityType {

    RESIDENT_ID_CARD("0", "居民身份证"),
    TEMP_ID_CARD("F", "临时居民身份证"),
    PASSPORT("1", "护照"),
    HK_MACAO_ID_CARD("B", "港澳居民往来内地通行证"),
    TAIWAN_ID_CARD("C", "台湾居民来往大陆通行证"),
    FOREIGN("P", "外国人永久居留证"),
    OTHERS("Z", "其他");

    private String code;
    private String desc;

    private IdentityType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static IdentityType getByCode(String code) {
        for (IdentityType typeEnum : IdentityType.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        throw new NotImplementedException("not implemention for enum code: " + code);
    }

    @Override
    public String toString() {
        return this.code;
    }
}
