package com.fintech.insurance.micro.vo.wechat;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Date;

public class RepaymentPlanSimpleVO implements Serializable {

    // id
    private Integer id;

    // 分期还款当期总额
    @FinanceDataPoint
    private Double repayTotalAmount;

    // 还款时间
    private Date repayDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
