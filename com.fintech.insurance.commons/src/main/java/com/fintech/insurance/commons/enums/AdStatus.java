package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/15 16:30
 */
public enum AdStatus {
    UNDERWAY("underway", "进行中"), FINISHED("finished", "已完成"), NOT_STARTED("not_start", "未开始"), UNKNOWN("unknow", "未知");
    private String code;
    private String description;
    private AdStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static AdStatus fromType(String code) throws IllegalArgumentException {
        if (code != null) {
            for (AdStatus type : values()) {
                if (StringUtils.equals(type.code, code)) {
                    return type;
                }
            }
        }
        throw new IllegalArgumentException();
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
