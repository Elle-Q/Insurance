package com.fintech.insurance.micro.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description: 供参数仅为id，且用post传参时使用
 * @Date: 2017/11/10 10:11
 */
public class IdVO implements Serializable {

    public IdVO(Integer id) {
        this.id = id;
    }

    public IdVO() {
    }

    @NotNull(message = "102001")
    private Integer id;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
