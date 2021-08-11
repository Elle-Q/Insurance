package com.fintech.insurance.micro.dto.finance;

/**
 * @Description:  合同模板数据 - 还款计划
 * @Author: Yong Li
 * @Date: 2018/1/5 12:02
 */
public class RepaymentPlanWordVO {

    public RepaymentPlanWordVO(String instalment, String payDay, String payCapital, String payInterest, String total, String leavingCapital) {
        this.instalment = instalment;
        this.payDay = payDay;
        this.payCapital = payCapital;
        this.payInterest = payInterest;
        this.total = total;
        this.leavingCapital = leavingCapital;
    }

    public RepaymentPlanWordVO() {
    }

    private String instalment; // 当前期数

    private String payDay; // 还款日

    private String payCapital; // 应还本金

    private String payInterest; // 应还利息

    private String total; // 应还本金+ 应还利息

    private String leavingCapital; // 剩余本金

    public String getInstalment() {
        return instalment;
    }

    public void setInstalment(String instalment) {
        this.instalment = instalment;
    }

    public String getPayDay() {
        return payDay;
    }

    public void setPayDay(String payDay) {
        this.payDay = payDay;
    }

    public String getPayCapital() {
        return payCapital;
    }

    public void setPayCapital(String payCapital) {
        this.payCapital = payCapital;
    }

    public String getPayInterest() {
        return payInterest;
    }

    public void setPayInterest(String payInterest) {
        this.payInterest = payInterest;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLeavingCapital() {
        return leavingCapital;
    }

    public void setLeavingCapital(String leavingCapital) {
        this.leavingCapital = leavingCapital;
    }
}
