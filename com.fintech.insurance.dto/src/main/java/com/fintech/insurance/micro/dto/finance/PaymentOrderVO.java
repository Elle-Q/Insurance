package com.fintech.insurance.micro.dto.finance;

import com.fintech.insurance.micro.dto.biz.BaseVO;

public class PaymentOrderVO extends BaseVO{
    //客户id
    private Integer customerId;

    //支付状态
    private String paymentStatus;

    //订单号
    private String orderNumber;

    //订单金额，折前金额
    private Double orderAmount;

    //折扣金额
    private Double discountAmount;

    //订单总金额
    private Double totalAmount;

    //支付金额
    private Double paymentAmount;

    //支付账户号
    private String paymentAccountNumer;

    //人工操作标记
    private Boolean manualFlag;

    public Boolean getManualFlag() {
        return manualFlag;
    }

    public void setManualFlag(Boolean manualFlag) {
        this.manualFlag = manualFlag;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentAccountNumer() {
        return paymentAccountNumer;
    }

    public void setPaymentAccountNumer(String paymentAccountNumer) {
        this.paymentAccountNumer = paymentAccountNumer;
    }
}
