package com.fintech.insurance.commons.beans;

/**
 * 微信模版消息配置
 */
public class WeixinTemplateMessagesConfgBean {

    public static final String TEMPLATE_REPAYMENT_REMINDER = "repaymentReminder";
    public static final String TEMPLATE_REPAYMENT_FAILURE = "repaymentFailure";
    public static final String TEMPLATE_LOAN_SUCCESS = "loanSuccess";
    public static final String TEMPLATE_REQUISITION_PASS = "requisitionPass";
    public static final String TEMPLATE_REPAYMENT_OVERDUE = "repaymentOverdue";
    public static final String TEMPLATE_WITHHOLD_SUCCESS = "withholdSuccess";

    /**
     * 还款提醒
     */
    private String repaymentReminder;

    /**
     * 还款失败
     */
    private String repaymentFailure;

    /**
     * 放款成功
     */
    private String loanSuccess;

    /**
     * 申请单通过
     */
    private String requisitionPass;

    /**
     * 还款逾期
     */
    private String repaymentOverdue;

    /**
     * 代扣成功
     */
    private String withholdSuccess;

    public String getRepaymentReminder() {
        return repaymentReminder;
    }

    public void setRepaymentReminder(String repaymentReminder) {
        this.repaymentReminder = repaymentReminder;
    }

    public String getRepaymentFailure() {
        return repaymentFailure;
    }

    public void setRepaymentFailure(String repaymentFailure) {
        this.repaymentFailure = repaymentFailure;
    }

    public String getLoanSuccess() {
        return loanSuccess;
    }

    public void setLoanSuccess(String loanSuccess) {
        this.loanSuccess = loanSuccess;
    }

    public String getRequisitionPass() {
        return requisitionPass;
    }

    public void setRequisitionPass(String requisitionPass) {
        this.requisitionPass = requisitionPass;
    }

    public String getRepaymentOverdue() {
        return repaymentOverdue;
    }

    public void setRepaymentOverdue(String repaymentOverdue) {
        this.repaymentOverdue = repaymentOverdue;
    }

    public String getWithholdSuccess() {
        return withholdSuccess;
    }

    public void setWithholdSuccess(String withholdSuccess) {
        this.withholdSuccess = withholdSuccess;
    }
}
