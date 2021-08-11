package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: man liu
 * @Description: 待提交合同信息VO
 * @Date: 2017/11/10 14:02
 */
public class ContractInfoVO implements Serializable {

    // 借款金额
    @FinanceDataPoint
    private Double brrowAmount;

    // 服务费
    @FinanceDataPoint
    private Double serviceFee;

    // 第一期还款金额
    @FinanceDataPoint
    private Double firstRepayMoney;

    // 保证金
    @FinanceDataPoint
    private Double assureMoney;

    // 合同还款金额
    private List<ContractRepayVO> contractRepayVOList;

    public Double getBrrowAmount() {
        return brrowAmount;
    }

    public void setBrrowAmount(Double brrowAmount) {
        this.brrowAmount = brrowAmount;
    }

    public Double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Double getFirstRepayMoney() {
        return firstRepayMoney;
    }

    public void setFirstRepayMoney(Double firstRepayMoney) {
        this.firstRepayMoney = firstRepayMoney;
    }

    public Double getAssureMoney() {
        return assureMoney;
    }

    public void setAssureMoney(Double assureMoney) {
        this.assureMoney = assureMoney;
    }

    public List<ContractRepayVO> getContractRepayVOList() {
        return contractRepayVOList;
    }

    public void setContractRepayVOList(List<ContractRepayVO> contractRepayVOList) {
        this.contractRepayVOList = contractRepayVOList;
    }
}
