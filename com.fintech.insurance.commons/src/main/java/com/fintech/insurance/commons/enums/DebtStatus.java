package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.type.DeclaredType;

/**
 * @Description: 分期还款的扣款状态枚举
 * @Author: Yong Li
 * @Date: 2017/12/13 16:00
 */
public enum DebtStatus {

    PROCESSING("processing", "扣款已经向第三方代扣发起成功，并被成功受理（仅适用于自动扣款）"),
    CONFIRMED("confirmed", "扣款成功（适用于自动扣款, 人工扣款）"),
    FAILED("failed", "扣款失败（适用于自动扣款, 人工扣款）"),
    WAITINGDEBT("waitingdebt", "待扣款"),
    SETTLED("settled", "结算成功, 扣款已经转移到企业账户，可疑提现（仅适用于自动扣款）");

    private String code;

    private String desc;

    private DebtStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DebtStatus codeOf(String code) {
        for (DebtStatus status : DebtStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("参数异常");
    }

    public Boolean isDebtSuccess() {
        return this == DebtStatus.CONFIRMED || this == DebtStatus.SETTLED;
    }

    public Boolean isNotFailed() {
        return this.isDebtSuccess() || this == DebtStatus.PROCESSING;
    }


    @JsonValue
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
