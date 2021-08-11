package com.fintech.insurance.commons.enums;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 合同事件
 *
 */
public enum ContractEvent {

    LoanConfirmedEvent("loanconfirmed", "确认放款"),
    OverdueTolerantDateEvent("overduetolerantdate", "逾期"),
    RefundWholesEvent("refundwholes", "全部还款完成"),
    OperationCSEvent("operationcs", "人工处理"),
    FinanceConfirmedEvent("financeconfirmed", "财务确认");

    private String code;
    private String desc;

    private ContractEvent(String code, String desc) {
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

    @Override
    public String toString() {
        return this.code;
    }
}
