package com.fintech.insurance.micro.customer.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: Clayburn
 * @Description: 客户咨询信息表
 * @Date: 2017/11/11 10:41
 */
@Entity
@Table(name = "cust_consultation")
public class CustomerConsultation extends BaseEntity {
    // 客户手机号码
    @Column(name = "customer_mobile", columnDefinition = "varchar(15) not null comment '客户手机号码'")
    private String customerMobile;

    // 客户姓名
    @Column(name = "customer_name", columnDefinition = "varchar(64) not null comment '客户姓名'")
    private String customerName;

    // 咨询内容（富文本）
    @Column(name = "consult_content", columnDefinition = "text not null comment '咨询内容'")
    private String consultContent;

    // 处理状态
    @Column(name = "process_status", columnDefinition = "varchar(16) not null comment '处理状态'")
    private String processStatus;

    // 处理时间
    @Column(name = "process_time", columnDefinition = "datetime")
    private Date processTime;

    // 处理的备注 (富文本)
    @Column(name = "process_remark", columnDefinition = "text comment '处理的备注'")
    private String processRemark;

    // 授权类型
    @Column(name = "oauth_type", unique = true, columnDefinition = "varchar(16) not null comment '授权类型'")
    private String oauthType;

    // 第三方授权的应用id
    @Column(name = "oauth_app_id", unique = true, columnDefinition = "varchar(64) not null comment '第三方授权的应用id'")
    private String oauthAppId;

    // 第三方账号id
    @Column(name = "oauth_account", unique = true, columnDefinition = "varchar(64) not null comment '第三方账号id'")
    private String oauthAcount;

    // 微信授权后获取的unionid
    @Column(name = "wx_unionid", columnDefinition = "varchar(64) comment '微信授权后获取的unionid'")
    private String wxUnionid;

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getConsultContent() {
        return consultContent;
    }

    public void setConsultContent(String consultContent) {
        this.consultContent = consultContent;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public String getProcessRemark() {
        return processRemark;
    }

    public void setProcessRemark(String processRemark) {
        this.processRemark = processRemark;
    }

    public String getOauthType() {
        return oauthType;
    }

    public void setOauthType(String oauthType) {
        this.oauthType = oauthType;
    }

    public String getOauthAppId() {
        return oauthAppId;
    }

    public void setOauthAppId(String oauthAppId) {
        this.oauthAppId = oauthAppId;
    }

    public String getOauthAcount() {
        return oauthAcount;
    }

    public void setOauthAcount(String oauthAcount) {
        this.oauthAcount = oauthAcount;
    }

    public String getWxUnionid() {
        return wxUnionid;
    }

    public void setWxUnionid(String wxUnionid) {
        this.wxUnionid = wxUnionid;
    }
}
