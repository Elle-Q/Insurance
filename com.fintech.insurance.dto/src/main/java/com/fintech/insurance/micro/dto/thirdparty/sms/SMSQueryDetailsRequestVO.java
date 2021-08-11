package com.fintech.insurance.micro.dto.thirdparty.sms;

import java.util.Date;

/**
 * @Description: 短信查询详情请求
 * @Author: East
 * @Date: 2017/11/13 0013 20:29
 */
public class SMSQueryDetailsRequestVO {

    /**
     * 短信接收号码
     */
    private String phoneNumber;
    /**
     * 发送流水号,从调用发送接口返回值中获取
     */
    private String bizId;
    /**
     * 短信发送日期
     */
    private Date sendDate;
    /**
     * 页大小
     */
    private long pageSize;
    /**
     * 当前页码
     */
    private long currentPage;

    public SMSQueryDetailsRequestVO() {
    }

    public SMSQueryDetailsRequestVO(String phoneNumber, String bizId, Date sendDate, long pageSize, long currentPage) {
        this.phoneNumber = phoneNumber;
        this.bizId = bizId;
        this.sendDate = sendDate;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
