package com.fintech.insurance.commons.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 合同状态
 */
public enum ContractStatus {

    Init("init", "初始状态"),
    Refunding("refunding", "还款中"),
    InsuranceReturning("insreturning", "待退保"),
    Refunded("refunded", "已完成"),
    InsuranceReturned("insreturned", "已退保");

    private String code;
    private String desc;

    private ContractStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public ContractStatus transfer(ContractEvent event) {
        ContractStatus returnStatus = this;
        switch (this){
            case Init:
                if (event == ContractEvent.LoanConfirmedEvent){
                    returnStatus = Refunding;
                }
                break;
            case Refunding:
                if (event == ContractEvent.OverdueTolerantDateEvent || event == ContractEvent.OperationCSEvent){
                    returnStatus = InsuranceReturning;
                } else if (event == ContractEvent.RefundWholesEvent){
                    returnStatus = Refunded;
                }
                break;
            case InsuranceReturning:
                if (event == ContractEvent.FinanceConfirmedEvent){
                    returnStatus = InsuranceReturned;
                }
                break;
        }
        return returnStatus;
    }

    public static ContractStatus codeOf(String code) {
        for (ContractStatus status : ContractStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
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


    @Override
    public String toString() {
        return this.code;
    }
}
