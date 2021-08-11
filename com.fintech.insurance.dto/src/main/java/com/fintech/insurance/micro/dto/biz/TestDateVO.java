package com.fintech.insurance.micro.dto.biz;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.NotificationEvent;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/10 13:15
 */
public class TestDateVO implements Serializable{

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private java.util.Date testDate;

    private ContractStatus contractStatus;

    private NotificationEvent event;

    @FinanceDataPoint
    private Double myRatio;

    @FinanceDataPoint
    private Long financeMoney;

    private TestDateVO subvo;

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    /*@JSONField(name = "contractStatus")
    public String getContractStatusByCode() {
        return this.contractStatus == null ? null : this.contractStatus.getCode();
    }

    @JSONField(name = "contractStatus")
    public ContractStatus setCContractStatusByCode(String code) {
        return ContractStatus.codeOf(code);
    }*/

    /*@JSONField(serialize = false)
    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    @JSONField(deserialize = false)
    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }*/

    public NotificationEvent getEvent() {
        return event;
    }

    public void setEvent(NotificationEvent event) {
        this.event = event;
    }

    public Double getMyRatio() {
        return myRatio;
    }

    public void setMyRatio(Double myRatio) {
        this.myRatio = myRatio;
    }

    public Long getFinanceMoney() {
        return financeMoney;
    }

    public void setFinanceMoney(Long financeMoney) {
        this.financeMoney = financeMoney;
    }

    public TestDateVO getSubvo() {
        return subvo;
    }

    public void setSubvo(TestDateVO subvo) {
        this.subvo = subvo;
    }
}
