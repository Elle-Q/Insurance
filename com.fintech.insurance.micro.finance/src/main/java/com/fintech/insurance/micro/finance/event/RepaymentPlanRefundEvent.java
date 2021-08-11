package com.fintech.insurance.micro.finance.event;

import java.util.Date;

/**
 * 分期代扣事件，成功或者失败
 */
public class RepaymentPlanRefundEvent extends RepaymentPlanEvent {

    private Integer customerId;

    private boolean isSucccess = false;

    private double refundAmount;

    private Date refundDate;

    private String refundCardNumber;

    private String failReason;

    /**
     * @param customerId
     * @param isSucccess
     * @param refundAmount
     * @param refundDate
     * @param refundCardNumber
     * @param failReason
     */
    public RepaymentPlanRefundEvent(Integer customerId, boolean isSucccess, double refundAmount, Date refundDate, String refundCardNumber, String failReason) {
       super(customerId);
       this.customerId = customerId;
       this.isSucccess = isSucccess;
       this.refundAmount = refundAmount;
       this.refundDate = refundDate;
       this.refundCardNumber = refundCardNumber;
       this.failReason = failReason;
    }

    /**
     * 构造还款成功事件
     * @param customerId
     * @param refundAmount
     * @param refundDate
     * @param refundCardNumber
     * @return
     */
    public static RepaymentPlanRefundEvent refundSuccessEvent(Integer customerId, double refundAmount, Date refundDate, String refundCardNumber) {
        return new RepaymentPlanRefundEvent(customerId, true, refundAmount, refundDate, refundCardNumber, null);
    }

    /**
     * 构造还款失败事件
     * @param customerId
     * @param refundAmount
     * @param refundDate
     * @param failReason
     * @return
     */
    public static RepaymentPlanRefundEvent refundFailEvent(Integer customerId, double refundAmount, Date refundDate, String failReason) {
        return new RepaymentPlanRefundEvent(customerId, false, refundAmount, refundDate, null, failReason);
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public boolean isSucccess() {
        return isSucccess;
    }

    public void setSucccess(boolean succcess) {
        isSucccess = succcess;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public String getRefundCardNumber() {
        return refundCardNumber;
    }

    public void setRefundCardNumber(String refundCardNumber) {
        this.refundCardNumber = refundCardNumber;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
