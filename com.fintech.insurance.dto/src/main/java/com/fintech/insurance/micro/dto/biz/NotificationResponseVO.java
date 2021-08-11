package com.fintech.insurance.micro.dto.biz;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/27 0027 19:52
 */
public class NotificationResponseVO {

    /**
     * 业务流水，全局唯一标识
     */
    private String sequenceId;
    /**
     * 发送回执ID，可根据该ID查询具体的发送状态
     */
    private String bizId;

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
}
