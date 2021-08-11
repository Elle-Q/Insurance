package com.fintech.insurance.micro.vo.wechat;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WeChatSimpleRequisitionDetailVO implements Serializable {

    //车辆id
    private Integer id;

    //车牌号
    private String carNumber;

    //保单数
    private Integer insuranceNumber;

    //是否可以用
    private Integer isCanUse;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Integer getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(Integer insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public Integer getIsCanUse() {
        return isCanUse;
    }

    public void setIsCanUse(Integer isCanUse) {
        this.isCanUse = isCanUse;
    }
}
