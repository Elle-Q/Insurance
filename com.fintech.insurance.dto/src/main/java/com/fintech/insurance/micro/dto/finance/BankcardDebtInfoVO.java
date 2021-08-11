package com.fintech.insurance.micro.dto.finance;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

/**
 * @Description: 银行扣款在本地的记录信息
 * @Author: Yong Li
 * @Date: 2018/1/19 20:36
 */
public class BankcardDebtInfoVO {

    // 银行卡号
    private String bankcardNum;

    //每日扣款次数
    private int totalDebtTimes;

    //当日已用扣款次数
    private int usedDebtTimes;

    //当日已用扣款次数
    private Double debtedAmount;

    //银行单笔限额
    @FinanceDataPoint
    private Long singleLimit;

    //银行单日限额
    @FinanceDataPoint
    private Long dailyLimit;

    public String getBankcardNum() {
        return bankcardNum;
    }

    public void setBankcardNum(String bankcardNum) {
        this.bankcardNum = bankcardNum;
    }

    public int getTotalDebtTimes() {
        return totalDebtTimes;
    }

    public void setTotalDebtTimes(int totalDebtTimes) {
        this.totalDebtTimes = totalDebtTimes;
    }

    public int getUsedDebtTimes() {
        return usedDebtTimes;
    }

    public void setUsedDebtTimes(int usedDebtTimes) {
        this.usedDebtTimes = usedDebtTimes;
    }

    public Long getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(Long singleLimit) {
        this.singleLimit = singleLimit;
    }

    public Long getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(Long dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public Double getDebtedAmount() {
        return debtedAmount;
    }

    public void setDebtedAmount(Double debtedAmount) {
        this.debtedAmount = debtedAmount;
    }
}
