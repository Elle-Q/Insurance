package com.fintech.insurance.commons.enums;



/**
 * 分期(每期)还款状态
 *
 *  请使用RefundStatus
 */
@Deprecated
public enum InstallmentStatus {

    WaitingRefund("waitrefund", "待还款"),
    RefundFailed("failrefund", "还款失败"),
    Overdued("overdued", "已逾期"),
    Refunded("refunded", "已还款"),
    InsuranceReturning("insreturning", "待退保"),
    InsuranceReturned("insreturned", "已退保");

    private String code;
    private String desc;

    private InstallmentStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

   public InstallmentStatus transfer(InstallmentEvent event) {
       InstallmentStatus returnStatus = this;
        switch (this){
            case WaitingRefund:
                if (event == InstallmentEvent.AutoRefundSuccessEvent){
                    returnStatus = Refunded;
                } else if (event == InstallmentEvent.AutoRefundFailedEvent){
                    returnStatus = RefundFailed;
                }
                break;
            case RefundFailed:
                if (event == InstallmentEvent.OverdueTolerantDateEvent){
                    returnStatus = Overdued;
                } else if (event == InstallmentEvent.ConfirmPaymentByCSEvent){
                    returnStatus = Refunded;
                }
                break;
            case Overdued:
                if (event == InstallmentEvent.ContractReturnedEvent){
                    returnStatus = InsuranceReturning;
                } else if (event == InstallmentEvent.ConfirmPaymentByCSEvent){
                    returnStatus = Refunded;
                }
                break;
            case InsuranceReturning:
                if (event == InstallmentEvent.ContractReturnedEvent){
                    returnStatus = InsuranceReturned;
                }
                break;
        }
        return returnStatus;
    }

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
