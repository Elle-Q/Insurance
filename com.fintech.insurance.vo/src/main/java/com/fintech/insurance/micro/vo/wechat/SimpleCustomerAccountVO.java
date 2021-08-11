package com.fintech.insurance.micro.vo.wechat;

import javax.validation.constraints.NotNull;

public class SimpleCustomerAccountVO {

    //客户账户id
    private Integer id;

    //客户名称
    private String name;

    private String idNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }
}
