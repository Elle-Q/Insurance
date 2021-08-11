package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 16:51
 */
public enum SMSTemplateParams {

    REQUISITION_NUMBER("requisitionNumber","申请单号"),
    CONTRACT_NUMBER("contractNumber", "合同编号"),
    CHANNEL_NAME("channelName", "渠道名称"),
    CUSTOMER_NAME("customerName","客户名称"),
    REPAYMENT_INSTALMENT("repaymentInstalment", "还款分期"),
    REPAY_DATE("repayDate", "还款日期"),
    REPAY_TOTAL_AMOUNT("repayTotalAmount", "还款总金额");

    private String code;
    private String desc;

    private SMSTemplateParams(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static SMSTemplateParams codeOf(String code) {
        for (SMSTemplateParams event : SMSTemplateParams.values()) {
            if (event.code.equals(code)) {
                return event;
            }
        }

        throw new IllegalStateException("Not found the mapping SMSEvent for code:" + code);
    }

    @Override
    public String toString() {
        return this.code;
    }
}
