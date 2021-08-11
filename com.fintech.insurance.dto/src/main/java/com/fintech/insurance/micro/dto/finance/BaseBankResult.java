package com.fintech.insurance.micro.dto.finance;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/13 10:15
 */
public class BaseBankResult {

    /**
     * 验卡状态: 是否通过
     * 对于扣款：第三方支付是否受理扣款成功， 并非扣款成功
     */
    protected Boolean isSuccess;

    /**
     *  易极付服务请求的订单号/序列号
     */
    protected String requestSerialNum;

    /**
     * 验卡失败提示信息, 只有验卡失败该字段才有数据
     */
    protected String failedMessage;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getRequestSerialNum() {
        return requestSerialNum;
    }

    public void setRequestSerialNum(String requestSerialNum) {
        this.requestSerialNum = requestSerialNum;
    }

    public String getFailedMessage() {
        return failedMessage;
    }

    public void setFailedMessage(String failedMessage) {
        this.failedMessage = failedMessage;
    }
}
