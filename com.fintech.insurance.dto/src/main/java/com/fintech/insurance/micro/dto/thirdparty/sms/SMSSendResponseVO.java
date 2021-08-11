package com.fintech.insurance.micro.dto.thirdparty.sms;

import java.util.Date;

/**
 * @Description: 短信发送响应
 * @Author: East
 * @Date: 2017/11/13 0013 10:47
 */
public class SMSSendResponseVO {

    public static final String SUCCESS_CODE = "0";
    public static final String FAILURE_CODE = "1";

    /**
     * 状态码
     */
    private String code;
    /**
     * 状态码的描述
     */
    private String message;

    /**
     * 业务流水，全局唯一标识
     */
    private String sequenceId;

    /**
     * 发送回执ID，可根据该ID查询具体的发送状态
     */
    private String bizId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    @Override
    public String toString() {
        return "SMSSendResponseVO{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", sequenceId='" + sequenceId + '\'' +
                ", bizId='" + bizId + '\'' +
                '}';
    }
}
