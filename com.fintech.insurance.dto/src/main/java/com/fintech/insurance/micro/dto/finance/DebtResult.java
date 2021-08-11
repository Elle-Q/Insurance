package com.fintech.insurance.micro.dto.finance;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

/**
 * @Description: 代扣服务结果
 * @Author: Yong Li
 * @Date: 2017/12/13 9:59
 */
public class DebtResult extends BaseBankResult {

    @FinanceDataPoint
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
