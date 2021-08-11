package com.fintech.insurance.micro.dto.biz;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/15 11:39
 */
public class RequisitionStatusVO implements Serializable {
    // 申请单id
    @NotNull(message = "102001")
    private Integer requisitionId;
    // 需要更改的目标状态
    @NotNull(message = "102001")
    private String requisitionStatus;


    public RequisitionStatusVO(Integer requisitionId, String requisitionStatus) {
        this.requisitionId = requisitionId;
        this.requisitionStatus = requisitionStatus;
    }

    public RequisitionStatusVO() {
    }

    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }

    public String getRequisitionStatus() {
        return requisitionStatus;
    }

    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }
}
