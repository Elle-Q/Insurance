package com.fintech.insurance.commons.enums;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author: Clayburn
 * @Description: 还款方式
 * @Date: 2017/11/13 10:10
 */
public enum RefundStatus {

    INIT_REFUND("init_refund", "初始还款"),
    WAITING_REFUND("waiting_refund", "待还款"),
    HAS_REFUND("has_refund", "已还款"),
    FAIL_REFUND("fail_refund", "还款失败"),
    OVERDUE("overdue", "已逾期"),
    WAITING_WITHDRAW("waiting_withdraw", "待退保"),
    HAS_WITHDRAW("has_withdraw", "已退保");

    private String code;
    private String description;

    RefundStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static RefundStatus codeOf(String code) {
        for (RefundStatus obj : RefundStatus.values()) {
            if (obj.code.equals(code)) {
                return obj;
            }
        }

        throw new IllegalStateException("Not found the mapping RefundStatus for code:" + code);
    }

    public RefundStatus transfer(InstallmentEvent event) {
        RefundStatus returnStatus = this;
        switch (this){
            case WAITING_REFUND:
                if (event == InstallmentEvent.AutoRefundSuccessEvent || event == InstallmentEvent.InitialPaymentSuccessEvent
                        || event == InstallmentEvent.ConfirmPaymentByCSEvent){
                    returnStatus = HAS_REFUND;
                } else if (event == InstallmentEvent.AutoRefundFailedEvent){
                    returnStatus = FAIL_REFUND;
                } else if (event == InstallmentEvent.OverdueTolerantDateEvent){
                    returnStatus = OVERDUE;
                } else if (event == InstallmentEvent.ContractReturnedEvent){
                    returnStatus = HAS_WITHDRAW;
                }
                break;
            case FAIL_REFUND:
                if (event == InstallmentEvent.OverdueTolerantDateEvent){
                    returnStatus = OVERDUE;
                } else if (event == InstallmentEvent.ConfirmPaymentByCSEvent || event == InstallmentEvent.AutoRefundSuccessEvent){
                    returnStatus = HAS_REFUND;
                } else if (event == InstallmentEvent.ContractReturnedEvent){
                    returnStatus = HAS_WITHDRAW;
                }
                break;
            case OVERDUE:
                if (event == InstallmentEvent.ContractReturnedEvent){
                    returnStatus = WAITING_WITHDRAW;
                } else if (event == InstallmentEvent.ConfirmPaymentByCSEvent || event == InstallmentEvent.AutoRefundSuccessEvent){
                    returnStatus = HAS_REFUND;
                }
                break;
            case WAITING_WITHDRAW:
                if (event == InstallmentEvent.ContractReturnedEvent){
                    returnStatus = HAS_WITHDRAW;
                }else if (event == InstallmentEvent.ConfirmPaymentByCSEvent || event == InstallmentEvent.AutoRefundSuccessEvent){
                    returnStatus = HAS_REFUND;
                }
                break;
        }
        return returnStatus;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
