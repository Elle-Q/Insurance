package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;


import java.util.Date;

/**
 * @Description: 业务报表视图
 * @Author: Yong Li
 * @Date: 2017/11/10 11:39
 */
public class BizReportVO {
    // 合同编号
    private String contractNumber;

    // 渠道编号
    private String channelCode;

    // 渠道名称
    private String channelName;

    // 渠道所属公司名称
    private String companyName;

    // 申请单号
    private String requisitionNumber;

    // 客户名称
    private String customerName;

    // 产品类型代码
    private String productTypeCode;

    // 借款金额(元)
    private String borrowAmount;

    // 创建时间
    private java.util.Date createTime;

    // 月利率
    private String interestRate;

    // 服务费率
    private String serviceFeeRate;

    // 其他费用率
    private String otherFeeRate;

    // 借款服务费
    private String borrowFeeAmount;

    // 合同状态代码
    private String contractStatusCode;

    // 车牌号
    private String carNumber;

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductTypeCode() {
        return productTypeCode;
    }

    public void setProductTypeCode(String productTypeCode) {
        this.productTypeCode = productTypeCode;
    }

    public String getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(String borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getServiceFeeRate() {
        return serviceFeeRate;
    }

    public void setServiceFeeRate(String serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
    }

    public String getOtherFeeRate() {
        return otherFeeRate;
    }

    public void setOtherFeeRate(String otherFeeRate) {
        this.otherFeeRate = otherFeeRate;
    }

    public String getBorrowFeeAmount() {
        return borrowFeeAmount;
    }

    public void setBorrowFeeAmount(String borrowFeeAmount) {
        this.borrowFeeAmount = borrowFeeAmount;
    }

    public String getContractStatusCode() {
        return contractStatusCode;
    }

    public void setContractStatusCode(String contractStatusCode) {
        this.contractStatusCode = contractStatusCode;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
}
