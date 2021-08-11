package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/20 10:49
 */
public enum LoanAccountType {
    COMPAEY_LOAN_TYPE("company", "公司放款"),BRANCH_LOAN_TYPE("branch", "支部放款"), PERSONAL_LOAN_TYPE("personal", "个人放款");

    private String code;
    private String description;

    LoanAccountType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static LoanAccountType fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (LoanAccountType type : LoanAccountType.values()) {
            if (code.equals(type.getCode())) {
                return type;
            }
        }

        throw new IllegalArgumentException();
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.code;
    }

}
