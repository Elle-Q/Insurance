package com.fintech.insurance.commons.enums;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Description: 通知事件
 * @Author: East
 * @Date: 2017/11/13 0013 11:39
 */
public enum NotificationEvent {

    DEFAULT_WELCOME("default_welcome", "默认欢迎通知"),
    DEFAULT_VERIFICATION("default_verification", "默认验证"),
    WX_CHANNEL_LOGIN_AUTH("wx_channel_login_auth", "微信渠道端登录认证"),
    WX_CUSTOMER_LOGIN_AUTH("wx_customer_login_auth", "微信客户端登录认证"),
    BIND_CARD("bind_card", "绑卡"),
    AUDIT_NOTIFICATION_FOR_CUSTOMER("audit_notification_for_customer", "客户审核通知 ，运营、风控、领导审核通过后，提醒支付"),
    AUDIT_NOTIFICATION_FOR_CHANNEL("audit_notification_for_channel", "渠道审核通知，运营、风控、领导审核通过后，提醒支付"),
    LOAN_CONFIRM_NOTIFICATION_FOR_CUSTOMER("loan_confirm_notification_for_customer", "客户放款确认通知，放款提醒"),
    LOAN_CONFIRM_NOTIFICATION_FOR_CHANNEL("loan_confirm_notification_for_channel", "渠道放款确认通知，放款提醒"),
    REPAYMENT_REMIND_NOTIFICATION("repayment_remind_notification", "还款提醒通知，还款日前XXX日提醒"),
    OVERDUE_REMIND_NOTIFICATION("overdue_remind_notification", "逾期提醒通知，过了还款日没过最大逾期天数期间扣款失败提醒"),
    REPAY_SUCCESS_NOTIFICATION("repay_success_notification", "还款成功通知，还款日扣款成功提醒/过了还款日没过最大逾期天数期间扣款成功提醒"),
    REPAY_FAILURE_NOTIFICATION_FOR_CUSTOMER("repay_failure_notification_for_customer", "客户还款失败通知，还款日扣款失败提醒"),
    REPAY_FAILURE_NOTIFICATION_FOR_CHANNEL("repay_failure_notification_for_channel", "渠道还款失败通知，还款日扣款失败提醒"),
    OVERDUE_REPAY_FAILURE_NOTIFICATION_FOR_CUSTOMER("overdue_repay_failure_notification_for_customer", "客户逾期还款失败通知"),
    OVERDUE_REPAY_FAILURE_NOTIFICATION_FOR_CHANNEL("overdue_repay_failure_notification_for_channel", "渠道逾期还款失败通知"),
    CONTRACT_COMPLETION_NOTIFICATION("contract_completion_notification", "合同完成通知，最后一期还款完成时提醒");

    private String code;
    private String desc;

    NotificationEvent(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static NotificationEvent codeOf(String code) {
        for (NotificationEvent event : NotificationEvent.values()) {
            if (event.code.equals(code)) {
                return event;
            }
        }

        throw new IllegalStateException("Not found the mapping SMSEvent for code:" + code);
    }


    @Override
    public String toString() {
        return this.code;
    }
}
