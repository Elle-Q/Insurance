package com.fintech.insurance.micro.dto.support;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description: 银行信息VO
 * @Date: 2017/11/9 15:05
 */
public class BankInfoVO implements Serializable {
    // 银行id
    private Integer id;
    // 银行编码
    private String code;
    // 银行名称
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
