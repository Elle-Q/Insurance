package com.fintech.insurance.commons.beans;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信模版消息配置bean，主要配置模版消息的内容
 */
@ConfigurationProperties(prefix = "weixin.template-messages.templates")
public class TemplateMessageConfigBean {

    /**
     * 申请单审核通过后渠道用户通知标题
     */
    private String requisitionPassChannelNtfTitle;

    /**
     * 申请单审核通过后客户通知标题
     */
    private String requisitionPassCustomerNtfTitle;

    /**
     * 申请单放款之后渠道用户通知标题
     */
    private String requisitionLoanedChannelNtfTitle;

    /**
     * 申请单放款之后渠道用户通知备注
     */
    private String requisitionLoanedChannelNtfRemark;

    /**
     * 申请单放款之后客户通知标题
     */
    private String requisitionLoanedCustomerNtfTitle;

    /**
     * 申请单放款之后客户通知备注
     */
    private String requisitionLoanedCustomerNtfRemark;

    /**
     * 金额
     */
    private String amount;

    /**
     * 天数
     */
    private String days;

    /**
     * 银行卡尾号
     */
    private String bankcardSuffix;

    /**
     * 还款提醒客户通知标题
     */
    private String repaymentReminderCustomerNtfTitle;

    /**
     * 服务费代扣成功提醒客户通知标题
     */
    private String serviceWithholdSuccessCustomerNtfTitle;

    /**
     * 分期还款代扣成功客户通知标题
     */
    private String repaymentWithholdSuccessCustomerNtfTitle;

    /**
     * 分期还款代扣失败客户通知标题
     */
    private String repaymentWithholdFailCustomerNtfTitle;

    /**
     * 分期还款失败客户通知备注
     */
    private String repaymentWithholdFailCustomerNtfRemark;

    /**
     * 分期还款逾期提醒客户通知标题
     */
    private String repaymentOverdueCustomerNtfTitle;

    /**
     * 分期还款逾期提醒客户通知备注
     */
    private String repaymentOverdueCustomerNtfRemark;

    /**
     * 分期还款逾期金额
     */
    private String repaymentOverdueAmount;

    public String getRequisitionPassChannelNtfTitle() {
        return requisitionPassChannelNtfTitle;
    }

    public void setRequisitionPassChannelNtfTitle(String requisitionPassChannelNtfTitle) {
        this.requisitionPassChannelNtfTitle = requisitionPassChannelNtfTitle;
    }

    public String getRequisitionPassCustomerNtfTitle() {
        return requisitionPassCustomerNtfTitle;
    }

    public void setRequisitionPassCustomerNtfTitle(String requisitionPassCustomerNtfTitle) {
        this.requisitionPassCustomerNtfTitle = requisitionPassCustomerNtfTitle;
    }

    public String getRequisitionLoanedChannelNtfTitle() {
        return requisitionLoanedChannelNtfTitle;
    }

    public void setRequisitionLoanedChannelNtfTitle(String requisitionLoanedChannelNtfTitle) {
        this.requisitionLoanedChannelNtfTitle = requisitionLoanedChannelNtfTitle;
    }

    public String getRequisitionLoanedChannelNtfRemark() {
        return requisitionLoanedChannelNtfRemark;
    }

    public void setRequisitionLoanedChannelNtfRemark(String requisitionLoanedChannelNtfRemark) {
        this.requisitionLoanedChannelNtfRemark = requisitionLoanedChannelNtfRemark;
    }

    public String getRequisitionLoanedCustomerNtfTitle() {
        return requisitionLoanedCustomerNtfTitle;
    }

    public void setRequisitionLoanedCustomerNtfTitle(String requisitionLoanedCustomerNtfTitle) {
        this.requisitionLoanedCustomerNtfTitle = requisitionLoanedCustomerNtfTitle;
    }

    public String getRequisitionLoanedCustomerNtfRemark() {
        return requisitionLoanedCustomerNtfRemark;
    }

    public void setRequisitionLoanedCustomerNtfRemark(String requisitionLoanedCustomerNtfRemark) {
        this.requisitionLoanedCustomerNtfRemark = requisitionLoanedCustomerNtfRemark;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBankcardSuffix() {
        return bankcardSuffix;
    }

    public void setBankcardSuffix(String bankcardSuffix) {
        this.bankcardSuffix = bankcardSuffix;
    }

    public String getRepaymentReminderCustomerNtfTitle() {
        return repaymentReminderCustomerNtfTitle;
    }

    public void setRepaymentReminderCustomerNtfTitle(String repaymentReminderCustomerNtfTitle) {
        this.repaymentReminderCustomerNtfTitle = repaymentReminderCustomerNtfTitle;
    }

    public String getServiceWithholdSuccessCustomerNtfTitle() {
        return serviceWithholdSuccessCustomerNtfTitle;
    }

    public void setServiceWithholdSuccessCustomerNtfTitle(String serviceWithholdSuccessCustomerNtfTitle) {
        this.serviceWithholdSuccessCustomerNtfTitle = serviceWithholdSuccessCustomerNtfTitle;
    }

    public String getRepaymentWithholdSuccessCustomerNtfTitle() {
        return repaymentWithholdSuccessCustomerNtfTitle;
    }

    public void setRepaymentWithholdSuccessCustomerNtfTitle(String repaymentWithholdSuccessCustomerNtfTitle) {
        this.repaymentWithholdSuccessCustomerNtfTitle = repaymentWithholdSuccessCustomerNtfTitle;
    }

    public String getRepaymentWithholdFailCustomerNtfTitle() {
        return repaymentWithholdFailCustomerNtfTitle;
    }

    public void setRepaymentWithholdFailCustomerNtfTitle(String repaymentWithholdFailCustomerNtfTitle) {
        this.repaymentWithholdFailCustomerNtfTitle = repaymentWithholdFailCustomerNtfTitle;
    }

    public String getRepaymentWithholdFailCustomerNtfRemark() {
        return repaymentWithholdFailCustomerNtfRemark;
    }

    public void setRepaymentWithholdFailCustomerNtfRemark(String repaymentWithholdFailCustomerNtfRemark) {
        this.repaymentWithholdFailCustomerNtfRemark = repaymentWithholdFailCustomerNtfRemark;
    }

    public String getRepaymentOverdueCustomerNtfTitle() {
        return repaymentOverdueCustomerNtfTitle;
    }

    public void setRepaymentOverdueCustomerNtfTitle(String repaymentOverdueCustomerNtfTitle) {
        this.repaymentOverdueCustomerNtfTitle = repaymentOverdueCustomerNtfTitle;
    }

    public String getRepaymentOverdueCustomerNtfRemark() {
        return repaymentOverdueCustomerNtfRemark;
    }

    public void setRepaymentOverdueCustomerNtfRemark(String repaymentOverdueCustomerNtfRemark) {
        this.repaymentOverdueCustomerNtfRemark = repaymentOverdueCustomerNtfRemark;
    }

    public String getRepaymentOverdueAmount() {
        return repaymentOverdueAmount;
    }

    public void setRepaymentOverdueAmount(String repaymentOverdueAmount) {
        this.repaymentOverdueAmount = repaymentOverdueAmount;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
