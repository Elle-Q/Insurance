package com.fintech.insurance.micro.dto.thirdparty.sms;

import java.util.Date;

/**
 * @Description: 短信发送明细
 * @Author: East
 * @Date: 2017/11/13 0013 20:47
 */
public class SMSSendDetailVO {

    /**
     * 短信接收号码
     */
    private String phoneNum;
    /**
     * 发送状态 1：等待回执，2：发送失败，3：发送成功
     */
    private long sendStatus;
    /**
     * 运营商短信错误码
     */
    private String errCode;
    /**
     * 发送时间
     */
    private Date sendDate;
    /**
     * 接收时间
     */
    private Date receiveDate;
    /**
     * 业业务流水，全局唯一标识
     */
    private String sequenceId;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public long getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(long sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public String toString() {
        return "SMSSendDetailVO{" +
                "phoneNum='" + phoneNum + '\'' +
                ", sendStatus=" + sendStatus +
                ", errCode='" + errCode + '\'' +
                ", sendDate=" + sendDate +
                ", receiveDate=" + receiveDate +
                ", sequenceId='" + sequenceId + '\'' +
                '}';
    }
}
