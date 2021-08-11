package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/13 17:14
 */
public class WeChatRequisitionVO implements Serializable {
    // 申请单id
    private Integer requisitionId;
    // 申请单状态
    private String requisitionStatus;
    // 申请总金额
    @FinanceDataPoint
    private Double requisitionAmount;
    // 服务费
    @FinanceDataPoint
    private Double serviceFee;
    // 第一期还款金额
    @FinanceDataPoint
    private Double repaymentAmountFirst;
    // 合计应付金额
    @FinanceDataPoint
    private Double totalAmount;
    // 企业名称
    private String enterpriseName;
    // 车辆数目
    private Integer carCount;
    // 保单数目
    private Integer insuranceCount;
    // 还款方式
    private String repayType;
    // 还款日类型
    private String repayDayType;
    // 产品类型
    private String productType;
    // 申请单号
    private String requisitionNumber;
    // 渠道编号
    private String channelCode;
    // 创建时间
    private Date createAt;
    // 备注信息
    private String remark;
    // 保证金
    @FinanceDataPoint
    private Double assureMoney;
    // 支付状态
    private String debtStatus;
    // 该申请单所属客户是否绑卡（0：未绑卡；1：已绑卡）
    private int hasBindCard;
    // 申请单对应的客户id
    private Integer customerId;
    // 剩余支付时间
    private Date deadlinePaymentTime;
    // 是否为渠道人员录单
    private Integer isChannelApplication = 0;

    private Integer canEdit = 0;

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

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }

    public String getRequisitionStatus() {
        return requisitionStatus;
    }

    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    public Double getRequisitionAmount() {
        return requisitionAmount;
    }

    public void setRequisitionAmount(Double requisitionAmount) {
        this.requisitionAmount = requisitionAmount;
    }

    public Double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Double getRepaymentAmountFirst() {
        return repaymentAmountFirst;
    }

    public void setRepaymentAmountFirst(Double repaymentAmountFirst) {
        this.repaymentAmountFirst = repaymentAmountFirst;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
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

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getRepayDayType() {
        return repayDayType;
    }

    public void setRepayDayType(String repayDayType) {
        this.repayDayType = repayDayType;
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getAssureMoney() {
        return assureMoney;
    }

    public void setAssureMoney(Double assureMoney) {
        this.assureMoney = assureMoney;
    }

    public String getDebtStatus() {
        return debtStatus;
    }

    public void setDebtStatus(String debtStatus) {
        this.debtStatus = debtStatus;
    }

    public int getHasBindCard() {
        return hasBindCard;
    }

    public void setHasBindCard(int hasBindCard) {
        this.hasBindCard = hasBindCard;
    }

    public Date getDeadlinePaymentTime() {
        return deadlinePaymentTime;
    }

    public void setDeadlinePaymentTime(Date deadlinePaymentTime) {
        this.deadlinePaymentTime = deadlinePaymentTime;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getIsChannelApplication() {
        return isChannelApplication;
    }

    public void setIsChannelApplication(Integer isChannelApplication) {
        this.isChannelApplication = isChannelApplication;
    }
}
