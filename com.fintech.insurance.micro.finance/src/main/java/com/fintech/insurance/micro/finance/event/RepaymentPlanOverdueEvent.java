package com.fintech.insurance.micro.finance.event;

import java.util.Date;

/**
 * 分期逾期事件
 */
public class RepaymentPlanOverdueEvent extends RepaymentPlanEvent {

    /**
     * 客户id
     */
    private Integer customerId;

    /**
     * 应还款日期
     */
    private Date refundDate;

    /**
     * 逾期天数
     */
    private int overdueDays;

    /**
     * 截至目前时间的总应还金额
     */
    private double totalAmountByToday;

    /**
     * 截至目前时间的总逾期罚金
     */
    private double overdueFeeByToday;

    /**
     * @param customerId
     * @param refundDate
     * @param overdueDays
     * @param totalAmountByToday
     * @param overdueFeeByToday
     */
    public RepaymentPlanOverdueEvent(Integer customerId, Date refundDate, int overdueDays, double totalAmountByToday, double overdueFeeByToday) {
        super(customerId);
        this.customerId = customerId;
        this.refundDate = refundDate;
        this.overdueDays = overdueDays;
        this.totalAmountByToday = totalAmountByToday;
        this.overdueFeeByToday = overdueFeeByToday;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }

    public double getTotalAmountByToday() {
        return totalAmountByToday;
    }

    public void setTotalAmountByToday(double totalAmountByToday) {
        this.totalAmountByToday = totalAmountByToday;
    }

    public double getOverdueFeeByToday() {
        return overdueFeeByToday;
    }

    public void setOverdueFeeByToday(double overdueFeeByToday) {
        this.overdueFeeByToday = overdueFeeByToday;
    }
}
