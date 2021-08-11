package com.fintech.insurance.micro.finance.persist.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 易极付验证用户四要素的记录
 * @Author: Nicholas
 * @Date: 2018/1/19
 */
@Entity
@Table(name = "finance_bankcard_verify_record")
public class BankcardVerifyRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "int(10) not null auto_increment comment '主键'")
    private Integer id;

    @Column(name = "user_name", columnDefinition = "varchar(64) not null comment '用户名称'")
    private String userName;

    @Column(name = "id_number", columnDefinition = "varchar(32) DEFAULT NULL COMMENT '身份证号'")
    private String idNumber;

    @Column(name = "bank_card_number", columnDefinition = "varchar(32) DEFAULT NULL COMMENT '银行卡号'")
    private String bankCardNumber;

    @Column(name = "reserved_mobile", columnDefinition = "varchar(15) NOT NULL COMMENT '预留手机号'")
    private String reservedMobile;

    @Column(name = "bank_code", columnDefinition = "varchar(10) DEFAULT NULL COMMENT '银行code'")
    private String bankCode;

    @Column(name = "bank_name", columnDefinition = "varchar(64) DEFAULT NULL COMMENT '银行名称'")
    private String bankName;

    @Column(name = "platform_order_number", columnDefinition = "varchar(64) DEFAULT NULL COMMENT '验卡订单号'")
    private String platformOrderNumber;

    @Column(name = "verification_status", columnDefinition = "varchar(32) DEFAULT NULL COMMENT '验卡状态'")
    private String verificationStatus;

    @Column(name = "verification_time", columnDefinition = "datetime DEFAULT NULL COMMENT '验卡时间'")
    private Date verificationTime;

    @Column(name = "remarks", columnDefinition = "varchar(128) DEFAULT NULL COMMENT '验卡结果备注'")
    private String remarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getReservedMobile() {
        return reservedMobile;
    }

    public void setReservedMobile(String reservedMobile) {
        this.reservedMobile = reservedMobile;
    }

    public String getPlatformOrderNumber() {
        return platformOrderNumber;
    }

    public void setPlatformOrderNumber(String platformOrderNumber) {
        this.platformOrderNumber = platformOrderNumber;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Date getVerificationTime() {
        return verificationTime;
    }

    public void setVerificationTime(Date verificationTime) {
        this.verificationTime = verificationTime;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
