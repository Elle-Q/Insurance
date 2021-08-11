package com.fintech.insurance.micro.dto.biz;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class SaveRequisitionContractVO implements Serializable{

    private static final long serialVersionUID = -111461361835512417L;

    //业务单id
    @NotNull(message = "104107")
    private Integer id;

    //默认借款12期
    private Integer month = 12;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
}
