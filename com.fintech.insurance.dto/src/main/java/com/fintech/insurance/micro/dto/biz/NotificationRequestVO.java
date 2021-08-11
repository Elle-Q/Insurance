package com.fintech.insurance.micro.dto.biz;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/27 0027 19:46
 */
public class NotificationRequestVO {

    /**
     * 业务申请ID
     */
    private Integer requisitionId;
    /**
     * 合同ID
     */
    private Integer contractId;
    /**
     * 还款计划ID
     */
    private Integer repaymentPlanId;

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getRepaymentPlanId() {
        return repaymentPlanId;
    }

    public void setRepaymentPlanId(Integer repaymentPlanId) {
        this.repaymentPlanId = repaymentPlanId;
    }

    @Override
    public String toString() {
        return "SMSNotificationParamVO{" +
                "requisitionId=" + requisitionId +
                ", contractId=" + contractId +
                ", repaymentPlanId=" + repaymentPlanId +
                '}';
    }
}
