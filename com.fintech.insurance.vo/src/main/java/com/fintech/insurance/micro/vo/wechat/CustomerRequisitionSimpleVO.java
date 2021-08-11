package com.fintech.insurance.micro.vo.wechat;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;

/**
 * 客户申请单
 */
public class CustomerRequisitionSimpleVO implements Serializable {

    //客户id
    private Integer requisitionId;

    //企业名称
    private String companyName;

    //车辆数
    private Integer carNum;

    //保单数
    private Integer insuranceNum;

    //其他材料
    private String requisitionStatus;

    //总申请金额
    @FinanceDataPoint
    private Double totalApplyAmount;

    //该申请单是否可以被当前登录用户操作，谁创建谁可操作
    private Integer canEdit = 0;

    //产品类型
    private String productType;

    //申请单退回原因
    private String rejectedRemark;

    //服务费用
    @FinanceDataPoint
    private Double serviceFee;

    //支付状态
    private String debtStatus;

    //客户是否绑卡
    private Integer hasBindCard = 0;

    //是否为渠道人员录单
    private Integer isChannelApplication = 0;

    //剩余支付时间
    private Long deadlinePaymentTime;

    // 合计应付金额
    @FinanceDataPoint
    private Double totalPayAmount;

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCarNum() {
        return carNum;
    }

    public void setCarNum(Integer carNum) {
        this.carNum = carNum;
    }

    public Integer getInsuranceNum() {
        return insuranceNum;
    }

    public void setInsuranceNum(Integer insuranceNum) {
        this.insuranceNum = insuranceNum;
    }

    public String getRequisitionStatus() {
        return requisitionStatus;
    }

    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    public Double getTotalApplyAmount() {
        return totalApplyAmount;
    }

    public void setTotalApplyAmount(Double totalApplyAmount) {
        this.totalApplyAmount = totalApplyAmount;
    }

    public Integer getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Integer canEdit) {
        this.canEdit = canEdit;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getRejectedRemark() {
        return rejectedRemark;
    }

    public void setRejectedRemark(String rejectedRemark) {
        this.rejectedRemark = rejectedRemark;
    }

    public Double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getDebtStatus() {
        return debtStatus;
    }

    public void setDebtStatus(String debtStatus) {
        this.debtStatus = debtStatus;
    }

    public Integer getHasBindCard() {
        return hasBindCard;
    }

    public void setHasBindCard(Integer hasBindCard) {
        this.hasBindCard = hasBindCard;
    }

    public Integer getIsChannelApplication() {
        return isChannelApplication;
    }

    public void setIsChannelApplication(Integer isChannelApplication) {
        this.isChannelApplication = isChannelApplication;
    }

    public Long getDeadlinePaymentTime() {
        return deadlinePaymentTime;
    }

    public void setDeadlinePaymentTime(Long deadlinePaymentTime) {
        this.deadlinePaymentTime = deadlinePaymentTime;
    }

    public Double getTotalPayAmount() {
        return totalPayAmount;
    }

    public void setTotalPayAmount(Double totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }
}
