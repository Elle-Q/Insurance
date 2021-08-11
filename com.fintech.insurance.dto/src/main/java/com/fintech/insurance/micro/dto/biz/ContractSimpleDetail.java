package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/15 9:06
 */
public class ContractSimpleDetail implements Serializable {
    // 合同id
    private Integer contractId;
    // 总分期
    private Integer totalInstalment;
    // 车牌号
    private String carNumber;
    // 借款金额
    @FinanceDataPoint
    private Double contractAmount;
    // 每月应还金额
    @FinanceDataPoint
    private Double interestAmount;
    // 合同Url
    private String contractUrl;
    // 服务合同url
    private String serviceUrl;

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getTotalInstalment() {
        return totalInstalment;
    }

    public void setTotalInstalment(Integer totalInstalment) {
        this.totalInstalment = totalInstalment;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Double getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(Double contractAmount) {
        this.contractAmount = contractAmount;
    }

    public Double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(Double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }
}
