package com.fintech.insurance.commons.enums;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 申请单事件
 *
 */
public enum RequisitionEvent {

    SubmmitEvent("submmit", "提交"),
    EditEvent("edit", "编辑"),
    TimeoutEvent("timeout", "超时"),
    UserConfirmEvent("userconfirm", "用户确认"),
    AuditFailedEvent("auditfailed", "审核驳回"),
    AuditPassEvent("auditpass", "审核通过"),
    PaymentFailedEvent("paymentfailed", "支付失败"),
    PaymentSuccessEvent("paymentsuccess", "支付成功"),
    LoanConfirmedEvent("loanconfirmed", "确认放款"),
    CanceledByCustomerEvent("canceledbycustomer", "客户取消"),
    ConfirmPaymentByCSEvent("confirmpaymentbycs", "线下确认支付成功");


    private String code;
    private String desc;

    private RequisitionEvent(String code, String desc) {
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
