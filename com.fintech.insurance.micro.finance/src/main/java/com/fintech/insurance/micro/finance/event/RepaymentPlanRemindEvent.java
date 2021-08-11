package com.fintech.insurance.micro.finance.event;

import com.fintech.insurance.micro.finance.persist.entity.RepaymentPlan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 还款计划还款提醒
 */
public class RepaymentPlanRemindEvent extends RepaymentPlanEvent {

    private List<RepaymentPlan> repaymentPlans = new ArrayList<>();

    /**
     * 还款日期
     */
    private Date refundDate;

    /**
     * 代扣卡号
     */
    private String bankAccountNumber;

    /**
     * @param repaymentPlans
     * @param refundDate
     * @param bankAccountNumber
     */
    public RepaymentPlanRemindEvent(List<RepaymentPlan> repaymentPlans, Date refundDate, String bankAccountNumber) {
        super(repaymentPlans);
        this.repaymentPlans = repaymentPlans;
        this.refundDate = refundDate;
        this.bankAccountNumber = bankAccountNumber;
    }

    public List<RepaymentPlan> getRepaymentPlans() {
        return repaymentPlans;
    }

    public void setRepaymentPlans(List<RepaymentPlan> repaymentPlans) {
        this.repaymentPlans = repaymentPlans;
    }

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }
}
