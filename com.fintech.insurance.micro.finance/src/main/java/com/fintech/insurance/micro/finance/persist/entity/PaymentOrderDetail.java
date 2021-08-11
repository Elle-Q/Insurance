package com.fintech.insurance.micro.finance.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/18 15:05
 */
@Entity
@Table(name = "finance_payment_order_detail")
public class PaymentOrderDetail extends BaseEntity {
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private PaymentOrder paymentOrder;

    @Column(name = "item_type", columnDefinition = "varchar(16) not null comment '类型：服务费，其他费用等'")
    private String itemType;

    @Column(name = "price", columnDefinition = "bigint unsigned not null comment '单价，单位为分，在此应该直接计算为服务费金额'")
    private Double price;

    @Column(name = "quantity", columnDefinition = "int(10) unsigned not null comment '数量，针对服务则设置为1'")
    private Integer quantity;

    @Column(name = "sub_total", columnDefinition = "bigint not null comment '小计金额，单位为分'")
    private Double subTotal;

    @Column(name = "formula_text", columnDefinition = "text comment '计算公式，展示时可以使用'")
    private String formulaText;

    @Column(name = "remark", columnDefinition = "text comment '备注'")
    private String remark;

    public PaymentOrder getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
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
