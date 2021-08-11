package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description: 放款信息VO
 * @Date: 2017/11/10 17:04
 */
public class LoanVO implements Serializable {
    // 业务单id
    private Integer requisitionId;
    // 业务单号
    private String requisitionNumber;
    // 渠道编号
    private String channelCode;
    // 渠道名称
    private String channelName;
    // 产品类型
    private String productType;
    // 客户名称
    private String customerName;
    // 放款总金额
    @FinanceDataPoint
    private Double loanAmount;
    // 服务费
    @FinanceDataPoint
    private Double serviceFee;
    // 订单状态
    private String requisitionStatus;
    // 支付凭证
    private RecordVO recordVO;

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getRequisitionStatus() {
        return requisitionStatus;
    }

    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    public RecordVO getRecordVO() {
        return recordVO;
    }

    public void setRecordVO(RecordVO recordVO) {
        this.recordVO = recordVO;
    }
}
