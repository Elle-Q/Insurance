package com.fintech.insurance.micro.finance.service.yjf.enums;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/9 16:48
 */
public enum SMSStatus {

    SMS_NOT_SEND("未发送"),
    SMS_SEND_FAIL("发送失败"),
    SMS_NOT_CHECK("发送成功未校验"),
    SMS_CHECK_FAIL("校验失败"),
    SMS_CHECK_PROCESSING("校验处理中"),
    SMS_CHECK_SUCCESS("校验成功");

    private String desc;

    private SMSStatus(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
