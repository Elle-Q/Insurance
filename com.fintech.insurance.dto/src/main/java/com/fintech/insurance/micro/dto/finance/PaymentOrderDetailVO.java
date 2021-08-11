package com.fintech.insurance.micro.dto.finance;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.micro.dto.biz.BaseVO;

/**
 * 支付订单明细VO
 */
public class PaymentOrderDetailVO extends BaseVO{

    //支付订单
    private PaymentOrderVO paymentOrderVO;

    //费用类型
    private String itemType;

    //单价
    @FinanceDataPoint
    private Double price;

    //数量
    private Integer quantity;

    //小计金额
    @FinanceDataPoint
    private Double subTotal;

    //计算公式
    private String formulaText;

    //备注信息
    private String remark;

    public PaymentOrderVO getPaymentOrderVO() {
        return paymentOrderVO;
    }

    public void setPaymentOrderVO(PaymentOrderVO paymentOrderVO) {
        this.paymentOrderVO = paymentOrderVO;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public String getFormulaText() {
        return formulaText;
    }

    public void setFormulaText(String formulaText) {
        this.formulaText = formulaText;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
