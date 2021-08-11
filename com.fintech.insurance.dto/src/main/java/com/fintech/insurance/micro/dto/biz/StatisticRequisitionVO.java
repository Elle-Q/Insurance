package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class StatisticRequisitionVO  implements Serializable {

    //业务单id
    private Integer id;

    //车辆数
    private Integer carNumber;

    //保险数
    private Integer insuranceNumber;

    //最小月份
    private Integer minMonth;

    //最大月份
    private Integer maxMonth;

    //可贷款金额
    private List<MonthMoneyVO> monthMoneyVOList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(Integer carNumber) {
        this.carNumber = carNumber;
    }

    public Integer getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(Integer insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public Integer getMinMonth() {
        return minMonth;
    }

    public void setMinMonth(Integer minMonth) {
        this.minMonth = minMonth;
    }

    public Integer getMaxMonth() {
        return maxMonth;
    }

    public void setMaxMonth(Integer maxMonth) {
        this.maxMonth = maxMonth;
    }

    public List<MonthMoneyVO> getMonthMoneyVOList() {
        return monthMoneyVOList;
    }

    public void setMonthMoneyVOList(List<MonthMoneyVO> monthMoneyVOList) {
        this.monthMoneyVOList = monthMoneyVOList;
    }
}
