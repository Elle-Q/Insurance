package com.fintech.insurance.micro.finance.model;

import com.fintech.insurance.commons.enums.DebtStatus;

import java.io.Serializable;

/**
 * @Description: 银行卡代扣信息记录及及状态
 * @Author: Yong Li
 * @Date: 2017/12/22 14:08
 */
public class UserDebtInfoRedisVO implements Serializable {

    public UserDebtInfoRedisVO(String bankcardNum, String debtOrderNum, Double amount, DebtStatus debtStatus) {
        this.bankcardNum = bankcardNum;
        this.debtOrderNum = debtOrderNum;
        this.amount = amount;
        this.debtStatus = debtStatus;
    }

    public UserDebtInfoRedisVO() {
    }

    /**
     * 扣款银行卡号
     */
    private String bankcardNum;

    /**
     * 扣款订单号
     */
    private String debtOrderNum;

    /**
     * 扣款金额
     */
    private Double amount;

    /**
     * 扣款状态
     */
    private DebtStatus debtStatus;


    public String getBankcardNum() {
        return bankcardNum;
    }

    public void setBankcardNum(String bankcardNum) {
        this.bankcardNum = bankcardNum;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public DebtStatus getDebtStatus() {
        return debtStatus;
    }

    public void setDebtStatus(DebtStatus debtStatus) {
        this.debtStatus = debtStatus;
    }

    public String getDebtOrderNum() {
        return debtOrderNum;
    }

    public void setDebtOrderNum(String debtOrderNum) {
        this.debtOrderNum = debtOrderNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDebtInfoRedisVO that = (UserDebtInfoRedisVO) o;

        if (bankcardNum != null ? !bankcardNum.equals(that.bankcardNum) : that.bankcardNum != null) return false;
        return debtOrderNum != null ? debtOrderNum.equals(that.debtOrderNum) : that.debtOrderNum == null;
    }

    @Override
    public int hashCode() {
        int result = bankcardNum != null ? bankcardNum.hashCode() : 0;
        result = 31 * result + (debtOrderNum != null ? debtOrderNum.hashCode() : 0);
        return result;
    }
}
