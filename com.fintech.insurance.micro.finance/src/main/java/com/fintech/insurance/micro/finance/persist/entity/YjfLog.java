package com.fintech.insurance.micro.finance.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description: 易极付请求响应数据
 * @Author: Yong Li
 * @Date: 2017/12/12 16:16
 */
@Entity
@Table(name = "finance_yijifu_log")
public class YjfLog extends BaseEntity {

    @Column(name = "type", columnDefinition = "varchar(32) not null comment '类型: request, response, notification'")
    private String type;

    @Column(name = "service_code", columnDefinition = "varchar(32) comment '易极付服务代码'")
    private String serviceCode;

    @Column(name = "message_id", columnDefinition = "varchar(64) comment '消息标识'")
    private String messageId;

    /*@Column(name = "ref_type", columnDefinition = "varchar(64) comment '关联类型'")
    private String refType;

    @Column(name = "ref_id", columnDefinition = "varchar(64) comment '关联标识'")
    private String refId;*/

    @Column(name = "is_error", columnDefinition = "tinyint(1) comment '是否出错'")
    private Boolean isError = false;

    @Column(name = "error_message", columnDefinition = "varchar(255) comment '错误消息'")
    private String errorMessage;

    @Column(name = "result_code", columnDefinition = "varchar(255) comment '请求结果代码'")
    private String resultCode;

    @Column(name = "content", columnDefinition = "varchar(1024) comment '请求/响应内容'")
    private String content;

    public static YjfLog createRequestLog(String serviceCode, String messageId, String content) {
        YjfLog log = new YjfLog();
        log.setType("request");
        log.setServiceCode(serviceCode);
        log.setMessageId(messageId);
        log.setContent(content);
        return log;
    }

    public static YjfLog createResponseLog(String serviceCode, String messageId, String content, String resultCode, Boolean isError, String errorMessage) {
        YjfLog log = new YjfLog();
        log.setType("response");
        log.setServiceCode(serviceCode);
        log.setMessageId(messageId);
        log.setContent(content);
        log.setIsError(isError);
        log.setResultCode(resultCode);
        log.setErrorMessage(errorMessage);
        return log;
    }

    public static YjfLog createNotificationLog(String serviceCode, String messageId, String content) {
        YjfLog log = new YjfLog();
        log.setType("notification");
        log.setServiceCode(serviceCode);
        log.setMessageId(messageId);
        log.setContent(content);
        return log;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Boolean getIsError() {
        return isError;
    }

    public void setIsError(Boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
