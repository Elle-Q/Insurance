package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: man liu
 * @Description: 合同还款VO
 * @Date: 2017/11/10 14:02
 */
public class ContractRepayVO implements Serializable {

    // 合同id
    private Integer contractId;

    // 合同月份
    private Integer month;

    // 车牌号
    private String carNumber;

    // 合同借款金额
    @FinanceDataPoint
    private Double borrowAmount;

    // 每期还款金额
    @FinanceDataPoint
    private Double eachRepayAmount;

    // 合同文件链接
    private String contractFileUrl;

    // 服务合同文件链接
    private String serviceContractFileUrl;

    // 合同名称
    private String contractName;

    //创建时间
    private Date createAt;

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Double getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(Double borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public Double getEachRepayAmount() {
        return eachRepayAmount;
    }

    public void setEachRepayAmount(Double eachRepayAmount) {
        this.eachRepayAmount = eachRepayAmount;
    }

    public String getContractFileUrl() {
        return contractFileUrl;
    }

    public void setContractFileUrl(String contractFileUrl) {
        this.contractFileUrl = contractFileUrl;
    }

    public String getServiceContractFileUrl() {
        return serviceContractFileUrl;
    }

    public void setServiceContractFileUrl(String serviceContractFileUrl) {
        this.serviceContractFileUrl = serviceContractFileUrl;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
