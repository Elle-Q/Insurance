package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fintech.insurance.commons.annotations.MicroEnum;

@MicroEnum
public enum  TimerStatus {

    PROCESSING("processing", "正在执行"),
    ERROR("error", "执行出错"),
    END("end", "执行结束");

    private String code;

    private String desc;

    private TimerStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TimerStatus codeOf(String code) {
        for (TimerStatus status : TimerStatus.values()) {
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
