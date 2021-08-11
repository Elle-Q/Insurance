package com.fintech.insurance.micro.api.biz;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.NotificationRequestVO;
import com.fintech.insurance.micro.dto.biz.NotificationResponseVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/15 0015 17:23
 */
@RequestMapping(path = "/biz/notification", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface BizNotificationServiceAPI {

    /**
     * 发送审批通知，运营、风控、领导审核通过后，提醒支付
     *
     * @param notificationRequestVO 通知请求参数
     * @return
     */
    @RequestMapping(path = "/send-approval-notification")
    FintechResponse<NotificationResponseVO> sendAuditNotification(@RequestBody(required = true) NotificationRequestVO notificationRequestVO);

    /**
     * 发送放款通知，放款提醒
     *
     * @param notificationRequestVO 通知请求参数
     * @return
     */
    @RequestMapping(path = "/send-loan-confirm-notification")
    FintechResponse<NotificationResponseVO> sendLoanConfirmNotification(@RequestBody(required = true) NotificationRequestVO notificationRequestVO);


    /**
     * 发送还款提醒通知，还款日前XXX日提醒
     *
     * @param notificationRequestVO 通知请求参数
     * @return
     */
    @RequestMapping(path = "/send-repay-remind-notification")
    FintechResponse<NotificationResponseVO> sendRepaymentRemindNotification(@RequestBody(required = true) NotificationRequestVO notificationRequestVO);

    /**
     * 发送逾期提醒通知，逾期提醒通知，过了还款日没过最大逾期天数期间扣款失败提醒
     *
     * @param notificationRequestVO 通知请求参数
     * @return
     */
    @RequestMapping(path = "/send-overdue-remind-notification")
    FintechResponse<NotificationResponseVO> sendOverdueRemindNotification(@RequestBody(required = true) NotificationRequestVO notificationRequestVO);

    /**
     * 发送还款结果通知
     *
     * @param notificationRequestVO 通知请求参数
     * @return
     */
    @RequestMapping(path = "/send-repay-notification")
    FintechResponse<NotificationResponseVO> sendRepaymentResultNotification(@RequestBody(required = true) NotificationRequestVO notificationRequestVO);
}
