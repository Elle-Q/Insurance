package com.fintech.insurance.micro.dto.thirdparty.sms;

import java.util.List;

/**
 * @Description: 短信查询详情响应
 * @Author: East
 * @Date: 2017/11/13 0013 17:06
 */
public class SMSQueryDetailsResponseVO {

    public static final String SUCCESS_CODE = "0";
    public static final String FAILURE_CODE = "0";

    /**
     * 状态码
     */
    private String code;
    /**
     * 状态码的描述
     */
    private String message;
    /**
     * 短信接收号码
     */
    private String phoneNumber;
    /**
     * 发送流水号
     */
    private String bizId;
    /**
     * 当前页码
     */
    private long currentPage;
    /**
     * 页大小
     */
    private long pageSize;
    /**
     * 发送总条数
     */
    private long totalCount;
    /**
     * 总页数
     */
    private long totalPage;
    /**
     * 发送明细
     */
    private List<SMSSendDetailVO> sendDetailVOList;

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

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public List<SMSSendDetailVO> getSendDetailVOList() {
        return sendDetailVOList;
    }

    public void setSendDetailVOList(List<SMSSendDetailVO> sendDetailVOList) {
        this.sendDetailVOList = sendDetailVOList;
    }
}
