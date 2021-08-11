package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Date;

/** 确认申请vo
 * @Author: liu man
 * @Description:
 * @Date: 2017/1/9 17:14
 */
public class WeChatApplyRequisitionVO implements Serializable {
    // 申请单id
    private Integer requisitionId;

    // 申请总金额
    @FinanceDataPoint
    private Double requisitionAmount;

    // 企业名称
    private String enterpriseName;
    // 车辆数目
    private Integer carCount;
    // 保单数目
    private Integer insuranceCount;

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }

    public Double getRequisitionAmount() {
        return requisitionAmount;
    }

    public void setRequisitionAmount(Double requisitionAmount) {
        this.requisitionAmount = requisitionAmount;
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

}
