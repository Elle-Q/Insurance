package com.fintech.insurance.micro.finance.event;

import com.fintech.insurance.micro.finance.persist.entity.RepaymentPlan;
import org.springframework.context.ApplicationEvent;

/**
 * 还款计划生命周期事件
 */
public class RepaymentPlanLifeCycleEvent extends RepaymentPlanEvent {

    private RepaymentPlan repaymentPlan;

    private String oldStatus;

    private String newStatus;

    /**
     * @param repaymentPlan
     * @param oldStatus
     * @param newStatus
     */
    public RepaymentPlanLifeCycleEvent(RepaymentPlan repaymentPlan, String oldStatus, String newStatus) {
        super(repaymentPlan);
        this.repaymentPlan = repaymentPlan;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    public RepaymentPlan getRepaymentPlan() {
        return repaymentPlan;
    }

    public void setRepaymentPlan(RepaymentPlan repaymentPlan) {
        this.repaymentPlan = repaymentPlan;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}
