package com.fintech.insurance.commons.algorithm;

import java.io.Serializable;

/**
 * 用于算法计算
 */
public class ArithmeticVO implements Serializable {

    //实体id
    private Integer entityId;

    //参与计算的值
    private Long number;

    public ArithmeticVO(Integer entityId, Long number) {
        this.entityId = entityId;
        this.number = number;
    }

    public ArithmeticVO() {
    }

    @Override
    public String toString() {
        return "ArithmeticVO{" +
                "entityId=" + entityId +
                ", number=" + number +
                '}';
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}
