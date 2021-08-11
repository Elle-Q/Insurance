package com.fintech.insurance.micro.vo.wechat;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: liu man
 * @Description: 服务合同
 * @Date: 2017/12/8 14:06
 */
public class ServiceContractVO implements Serializable {
    // 合同id
    private Integer contractId;
    // 合同号
    private String contractNumber;
    // 客户姓名
    private String customerName;
    // 地址
    private String address;
    // 客户手机号
    private String customerMobile;
    // 车牌号
    private String carNumber;
    //车架号
    private String carFrameNumber;
    //商业保险金额，单位为分
    private Integer commercialInsuranceAmount = 0;
    //总申请借款金额，单位为分
    private Integer totalApplyAmount = 0;

    //服务费率
    private Double serviceRate = 0.0;

    //服务费
    private Double serviceFee = 0.0;

    //月费率
    private Double interestRate;

    //月服务费金额
    private Double interestAmount;

    //每月还款日
    private Integer monthRepayDay;

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarFrameNumber() {
        return carFrameNumber;
    }

    public void setCarFrameNumber(String carFrameNumber) {
        this.carFrameNumber = carFrameNumber;
    }

    public Integer getCommercialInsuranceAmount() {
        return commercialInsuranceAmount;
    }

    public void setCommercialInsuranceAmount(Integer commercialInsuranceAmount) {
        this.commercialInsuranceAmount = commercialInsuranceAmount;
    }

    public Integer getTotalApplyAmount() {
        return totalApplyAmount;
    }

    public void setTotalApplyAmount(Integer totalApplyAmount) {
        this.totalApplyAmount = totalApplyAmount;
    }

    public Double getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(Double serviceRate) {
        this.serviceRate = serviceRate;
    }

    public Double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(Double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public Integer getMonthRepayDay() {
        return monthRepayDay;
    }

    public void setMonthRepayDay(Integer monthRepayDay) {
        this.monthRepayDay = monthRepayDay;
    }
}
