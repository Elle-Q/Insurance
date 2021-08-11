package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

/**
 *  申请单状态
 */
public enum RequisitionStatus {

    Draft("draft", "待提交"),
    Submitted("submitted", "待确认"),
    Auditing("auditing", "待审核"),
    WaitingPayment("waitpayment", "待支付"),
    FailedPayment("failpayment", "支付失败"),
    Rejected("rejected", "已退回"),
    Canceled("canceled", "已取消"),
    WaitingLoan("waitloan", "待放款"),
    Loaned("loaned", "已放款");

    private String code;
    private String desc;

    RequisitionStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RequisitionStatus codeOf(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (RequisitionStatus type : RequisitionStatus.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
        }
        throw new IllegalStateException("Not found the mapping RequisitionStatus for code:" + code);
    }

    public RequisitionStatus transfer(RequisitionEvent event) {
        RequisitionStatus returnStatus = this;
        switch (this){
            case Draft:
                if (event == RequisitionEvent.SubmmitEvent){
                    returnStatus = Submitted;
                }
                break;
            case Submitted:
                if (event == RequisitionEvent.UserConfirmEvent){
                    returnStatus = Auditing;
                } else if (event == RequisitionEvent.TimeoutEvent){
                    returnStatus = Canceled;
                }
                break;
            case Auditing:
                if (event == RequisitionEvent.AuditPassEvent){
                    returnStatus = WaitingPayment;
                } else if (event == RequisitionEvent.AuditFailedEvent){
                    returnStatus = Rejected;
                }
                break;
            case Rejected:
                if (event == RequisitionEvent.TimeoutEvent){
                    returnStatus = Canceled;
                } else if (event == RequisitionEvent.EditEvent){
                    returnStatus = Draft;
                }
                break;
            case WaitingPayment:
                if (event == RequisitionEvent.PaymentSuccessEvent){
                    returnStatus = WaitingLoan;
                } else if (event == RequisitionEvent.PaymentFailedEvent){
                    returnStatus = FailedPayment;
                }
                break;
            case FailedPayment:
                if (event == RequisitionEvent.TimeoutEvent || event == RequisitionEvent.CanceledByCustomerEvent){
                    returnStatus = Canceled;
                } else if (event == RequisitionEvent.ConfirmPaymentByCSEvent){
                    returnStatus = WaitingLoan;
                }
                break;
            case WaitingLoan:
                if (event == RequisitionEvent.LoanConfirmedEvent){
                    returnStatus = Loaned;
                }
                break;
        }
        return returnStatus;
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
