package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Clayburn
 * @Description: 应用类型
 * @Date: 2017/12/26 13:48
 */
public enum ApplicationType {
    DEBT("DEBT", "扣款"),
    VERIFY("VERIFY", "验证");

    private String code;
    private String description;

    ApplicationType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ApplicationType codeOf(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (ApplicationType type : values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        throw new IllegalArgumentException("Not found the mapping AuditStatus for code" + code);
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
