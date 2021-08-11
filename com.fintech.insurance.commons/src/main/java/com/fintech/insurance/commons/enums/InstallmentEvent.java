package com.fintech.insurance.commons.enums;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 分期(每期)还款事件
 *
 */
public enum InstallmentEvent {

    AutoRefundSuccessEvent("autorefundsuccess", "自动扣款成功"),
    AutoRefundFailedEvent("autorefundfailed", "自动扣款失败"),
    OverdueTolerantDateEvent("overduetolerantdate", "逾期"),
    ContractReturnedEvent("contractreturned", "确认合同已撤回"),
    PaymentSuccessEvent("paymentsuccess", "支付成功"),
    InitialPaymentSuccessEvent("initialpaymentsuccess", "期初还款支付成功"),
    ConfirmPaymentByCSEvent("confirmpaymentbycs", "线下确认支付成功");

    private String code;
    private String desc;

    private InstallmentEvent(String code, String desc) {
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
