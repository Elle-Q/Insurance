package com.fintech.insurance.micro.dto.biz;

import java.util.Date;

/**
 * @Author: Clayburn
 * @Description: 合同基本信息详情VO
 * @Date: 2017/11/10 14:19
 */
public class ContractBaseDetailVO extends ContractVO {
    public static final String CONTRACT_POLICY_LOANS_NAME = "个月保单贷款合同";
    public static final String CONTRACT_CAR_INSTALMENTS_NAME = "个月车险分期合同";
    public static final String SERVICE_CONTRACT_POLICY_LOANS_NAME = "个月保单贷款服务合同";
    public static final String SERVICE_CONTRACT_CAR_INSTALMENTS_NAME = "个月车险分期服务合同";

    // 车辆数目
    private Integer carCount;
    // 保单数目
    private Integer insuranceCount;
    // 渠道编号
    private String channelCode;
    // 借款开始时间
    private Date borrowBeginDate;
    // 借款结束时间
    private Date borrowEndDate;
    // (交强险+车船税)/总保额 * 100
    private Double rate;

    // 借款合同名称
    private String contractName;
    // 借款合同跳转Url
    private String contractUrl;
    // 服务合同名称
    private String serviceContractName;
    // 服务合同跳转Url
    private String serviceContractUrl;

    // 车牌号
    private String carNumber;
    // 行驶证号
    private String drivingLicense;

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }

    public Integer getCarCount() {
        return carCount;
    }

    public void setCarCount(Integer carCount) {
        this.carCount = carCount;
    }

    public Integer getInsuranceCount() {
        return insuranceCount;
    }

    public void setInsuranceCount(Integer insuranceCount) {
        this.insuranceCount = insuranceCount;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public Date getBorrowBeginDate() {
        return borrowBeginDate;
    }

    public void setBorrowBeginDate(Date borrowBeginDate) {
        this.borrowBeginDate = borrowBeginDate;
    }

    public Date getBorrowEndDate() {
        return borrowEndDate;
    }

    public void setBorrowEndDate(Date borrowEndDate) {
        this.borrowEndDate = borrowEndDate;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getServiceContractName() {
        return serviceContractName;
    }

    public void setServiceContractName(String serviceContractName) {
        this.serviceContractName = serviceContractName;
    }

    public String getServiceContractUrl() {
        return serviceContractUrl;
    }

    public void setServiceContractUrl(String serviceContractUrl) {
        this.serviceContractUrl = serviceContractUrl;
    }

    public static String getContractPolicyLoansName() {
        return CONTRACT_POLICY_LOANS_NAME;
    }

    @Override
    public String getCarNumber() {
        return carNumber;
    }

    @Override
    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }
}
