package com.fintech.insurance.micro.customer.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * @Author: Clayburn
 * @Description: 客户账户表
 * @Date: 2017/11/11 13:48
 */
@Entity
@Table(name = "cust_account")
public class CustomerAccount extends BaseEntity {
    @Column(name = "id_number", unique = true, columnDefinition = "varchar(32) not null comment '身份证号码，全局唯一'")
    private String idNumber;

    @Column(name = "certification_id", columnDefinition = "varchar(32) not null comment '证书id'")
    private String certificationId;

    @Column(name = "id_front", columnDefinition = "varchar(32) not null comment '身份证正面照片'")
    private String idFront;

    @Column(name = "id_back", columnDefinition = "varchar(32) not null comment '身份证反面照片'")
    private String idBack;

    @Column(name = "is_locked", columnDefinition = "boolean not null default 0 comment '是否冻结标志位'")
    private Boolean lockedTag;

    @Column(name = "locked_at", columnDefinition = "timestamp comment '冻结时间'")
    private Date lockedAt;

    @OneToMany(mappedBy = "customerAccount", fetch = FetchType.EAGER)
    private Set<CustomerAccountInfo> customerAccountInfos;

    @OneToMany(mappedBy = "customerAccount", fetch = FetchType.EAGER)
    private Set<CustomerBankCard> customerBankCards;

    @OneToMany(mappedBy = "customerAccount", fetch = FetchType.EAGER)
    private Set<CustomerAccountOauth> customerAccountOauths;

    public String getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(String certificationId) {
        this.certificationId = certificationId;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdFront() {
        return idFront;
    }

    public void setIdFront(String idFront) {
        this.idFront = idFront;
    }

    public String getIdBack() {
        return idBack;
    }

    public void setIdBack(String idBack) {
        this.idBack = idBack;
    }

    public Set<CustomerBankCard> getCustomerBankCards() {
        return customerBankCards;
    }

    public void setCustomerBankCards(Set<CustomerBankCard> customerBankCards) {
        this.customerBankCards = customerBankCards;
    }

    public Set<CustomerAccountOauth> getCustomerAccountOauths() {
        return customerAccountOauths;
    }

    public void setCustomerAccountOauths(Set<CustomerAccountOauth> customerAccountOauths) {
        this.customerAccountOauths = customerAccountOauths;
    }

    public Boolean getLockedTag() {
        return lockedTag;
    }

    public void setLockedTag(Boolean lockedTag) {
        this.lockedTag = lockedTag;
    }

    public Date getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(Date lockedAt) {
        this.lockedAt = lockedAt;
    }

    public Set<CustomerAccountInfo> getCustomerAccountInfos() {
        return customerAccountInfos;
    }

    public void setCustomerAccountInfos(Set<CustomerAccountInfo> customerAccountInfos) {
        this.customerAccountInfos = customerAccountInfos;
    }
}
