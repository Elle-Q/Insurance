package com.fintech.insurance.micro.dto.thirdparty.sms;

import java.util.Date;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/15 0015 11:21
 */
public class SMSSendResultVO {

    /**
     * 业务流水，全局唯一标识
     */
    private String sequenceId;

    /**
     * 发送回执ID，可根据该ID查询具体的发送状态
     */
    private String bizId;

    /**
     * 过期时间
     */
    private Date expireTime;

    public SMSSendResultVO() {
    }

    public SMSSendResultVO(String sequenceId, String bizId, Date expireTime) {
        this.sequenceId = sequenceId;
        this.bizId = bizId;
        this.expireTime = expireTime;
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

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
