package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description: 合同签署时的用户， 主要有企业签， 出借人签
 * @Author: Yong Li
 * @Date: 2018/2/17 14:12
 */
public enum ContractSignUserType {

    ENTERPRISE("enterprise", "企业签署"),
    LENDER("lender", "出借人签署"),;

    private String code;
    private String desc;

    ContractSignUserType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ContractSignUserType codeOf(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (ContractSignUserType type : ContractSignUserType.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
