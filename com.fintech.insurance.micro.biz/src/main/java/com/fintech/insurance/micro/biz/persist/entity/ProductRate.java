package com.fintech.insurance.micro.biz.persist.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品利率表
 *
 * @author liu man
 * @version 1.1.0
 * @since 2017-08-18 17:04:30
 */
@Entity
@Table(name = "busi_product_rate")
public class ProductRate implements Serializable {

    private static final long serialVersionUID = -8820461361835512413L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "int(11) not null auto_increment comment '主键'")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", columnDefinition = "int(11) not null comment '关联产品id'")
    private Product product;

    @Column(name = "business_duration", columnDefinition = "int comment '业务期数'")
    private Integer businessDuration;

    //计息类型，按月或者按日计息")
    @Column(name = "interest_type", length = 16, columnDefinition = "varchar(16) comment '计息类型，按月或者按日计息'")
    private String interestType;

    @Column(name = "interest_rate", columnDefinition = "double comment '利率点（万倍）'")
    private Double interestRate;

    @Column(name = "loan_ratio", columnDefinition = "double comment '可借比例（万倍）'")
    private Double loanRatio;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getBusinessDuration() {
        return businessDuration;
    }

    public void setBusinessDuration(Integer businessDuration) {
        this.businessDuration = businessDuration;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getLoanRatio() {
        return loanRatio;
    }

    public void setLoanRatio(Double loanRatio) {
        this.loanRatio = loanRatio;
    }
}
