package com.fintech.insurance.micro.dto.retrieval;

import java.io.Serializable;

/**
 * @Description: 某一期还款计划算出的剩余本金VO
 * @Author: Nicholas
 * @Date: 2018/1/13 20:49
 */
public class SurplusCapitalVO implements Serializable {

    // 还款计划id
    private Integer repaymentPlanId;
    // 剩余本金
    private Double surplusCapitalAmount;

    public Double getSurplusCapitalAmount() {
        return surplusCapitalAmount;
    }

    public void setSurplusCapitalAmount(Double surplusCapitalAmount) {
        this.surplusCapitalAmount = surplusCapitalAmount;
    }

    public Integer getRepaymentPlanId() {
        return repaymentPlanId;
    }

    public void setRepaymentPlanId(Integer repaymentPlanId) {
        this.repaymentPlanId = repaymentPlanId;
    }
}
