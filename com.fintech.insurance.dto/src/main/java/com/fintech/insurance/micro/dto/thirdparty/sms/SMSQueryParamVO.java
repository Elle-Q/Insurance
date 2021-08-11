package com.fintech.insurance.micro.dto.thirdparty.sms;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/21 0021 15:48
 */
public class SMSQueryParamVO {
    /**
     * 短信接收号码
     */
    @NotNull(message = "106123")
    private String phoneNumber;
    /**
     * 发送流水号,从调用发送接口返回值中获取
     */
    private String bizId;
    /**
     * 短信发送日期
     */
    @NotNull(message = "106124")
    private Date sendDate;
    /**
     * 分页大小
     */
    @NotNull(message = "106125")
    @Max(value = 50, message = "106125")
    @Min(value = 1, message = "106125")
    private Long pageSize;
    /**
     * 当前页码
     */
    @NotNull(message = "106126")
    private Long currentPage;

    public SMSQueryParamVO() {
    }

    public SMSQueryParamVO(String phoneNumber, String bizId, Date sendDate, Long pageSize, Long currentPage) {
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

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public String toString() {
        return "SMSQueryParamVO{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", bizId='" + bizId + '\'' +
                ", sendDate=" + sendDate +
                ", pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                '}';
    }
}
