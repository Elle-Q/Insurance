package com.fintech.insurance.micro.vo.wechat;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Clayburn
 * @Description: 分期信息VO
 * @Date: 2017/12/8 14:06
 */
public class InstalmentVO implements Serializable {
    // 合同id， 列表唯一
    private Integer contractId;
    // 合同编号，列表唯一
    private String contractNumber;
    // 车牌号
    private String carNumber;
    // 渠道名称
    private String channelName;
    // 还款状态
    private String refundStatus;
    // 总期数
    private Integer totalInstalment;
    // 当前期数
    private Integer currentInstalment;
    // 当前应还金额
    @FinanceDataPoint
    private Double repayTotalAmount;
    // 还款日
    private Date repayDate;
    // 是否逾期，true逾期，false没逾期
    private Boolean overdueFlag;
    // 逾期天数
    private long overdueDays = 0;

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Integer getTotalInstalment() {
        return totalInstalment;
    }

    public void setTotalInstalment(Integer totalInstalment) {
        this.totalInstalment = totalInstalment;
    }

    public Integer getCurrentInstalment() {
        return currentInstalment;
    }

    public void setCurrentInstalment(Integer currentInstalment) {
        this.currentInstalment = currentInstalment;
    }

    public Double getRepayTotalAmount() {
        return repayTotalAmount;
    }

    public void setRepayTotalAmount(Double repayTotalAmount) {
        this.repayTotalAmount = repayTotalAmount;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public Boolean getOverdueFlag() {
        return overdueFlag;
    }

    public void setOverdueFlag(Boolean overdueFlag) {
        this.overdueFlag = overdueFlag;
    }

    public long getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(long overdueDays) {
        this.overdueDays = overdueDays;
    }
}
