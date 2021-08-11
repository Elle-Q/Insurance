package com.fintech.insurance.micro.vo.wechat;

import java.io.Serializable;
import java.util.List;

public class CountRequisitionByStatusVO implements Serializable {

    //数量
    private Integer num = 0;

    //申请单状态
    private String requisitionStatus;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getRequisitionStatus() {
        return requisitionStatus;
    }

    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }
}
