package com.fintech.insurance.micro.thirdparty.service.bestsign.enums;

import org.apache.commons.lang3.NotImplementedException;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/21 20:27
 */
public enum FileType {
    PDF("PDF"),
    DOC("doc"),
    PNG("png");

    private String code;

    private FileType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return this.code;
    }

    public static FileType getByCode(String code) {
        for (FileType typeEnum : FileType.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        throw new NotImplementedException("not implemention for enum code: " + code);
    }
}
