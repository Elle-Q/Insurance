package com.fintech.insurance.micro.vo.wechat;

import java.io.Serializable;

public class WeChatMonthMoneyVO implements Serializable {

    //月份
    private Integer month;

    //金额
    private Double money;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
