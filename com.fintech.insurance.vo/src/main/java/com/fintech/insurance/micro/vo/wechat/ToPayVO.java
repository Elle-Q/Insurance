package com.fintech.insurance.micro.vo.wechat;

import java.io.Serializable;
import java.util.List;

public class ToPayVO implements Serializable {

    //合同id
    private Integer id;

    //合同id
    private Integer contractId;

    //车牌号
    private String carNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
}
