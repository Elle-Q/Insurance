package com.fintech.insurance.micro.dto.finance;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;

/**
 * @Description: 银行VO，用于代扣时获取当日以及单笔限额
 * @Author: qxy
 * @Date: 2017/12/12 9:55
 */
public class BankVO implements Serializable{

    @FinanceDataPoint
    private Integer singleLimit;

    @FinanceDataPoint
    private Integer dailyLimit;

    public Integer getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(Integer singleLimit) {
        this.singleLimit = singleLimit;
    }

    public Integer getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(Integer dailyLimit) {
        this.dailyLimit = dailyLimit;
    }
}
