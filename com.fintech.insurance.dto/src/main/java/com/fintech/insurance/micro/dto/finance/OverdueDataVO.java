package com.fintech.insurance.micro.dto.finance;

import java.io.Serializable;

/**
 * @Description: 逾期信息
 * @Author: Nicholas
 * @Date: 2018/1/13 21:26
 */
public class OverdueDataVO implements Serializable {
    // 还款计划id
    private Integer repaymentPlanId;
    // 逾期天数
    private long overdueDays = 0;
    // 逾期罚息
    private Double overdueFines = 0.0;

    public Integer getRepaymentPlanId() {
        return repaymentPlanId;
    }

    public void setRepaymentPlanId(Integer repaymentPlanId) {
        this.repaymentPlanId = repaymentPlanId;
    }

    public long getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(long overdueDays) {
        this.overdueDays = overdueDays;
    }

    public Double getOverdueFines() {
        return overdueFines;
    }

    public void setOverdueFines(Double overdueFines) {
        this.overdueFines = overdueFines;
    }
}
