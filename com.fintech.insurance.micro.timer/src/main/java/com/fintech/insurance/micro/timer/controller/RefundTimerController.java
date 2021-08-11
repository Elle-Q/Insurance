package com.fintech.insurance.micro.timer.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.feign.finance.RefundServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 还款
 * @Author: qxy
 * @Date: 2017/12/19 16:24
 */
@RestController
@RequestMapping(value = "/timer/refund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RefundTimerController {

    private static final Logger LOG = LoggerFactory.getLogger(RefundTimerController.class);

    @Autowired
    private RefundServiceFeign refundServiceFeign;

    /**
     * 正常申请单还款日自动代扣： 一天三次 8:00 12:00 16:00
     * @return
     */
    @GetMapping(value = "/debit-for-repayday")
    public FintechResponse<VoidPlaceHolder> debitRequisitionForRepayDay() {

        LOG.info("debit for repayday");

        FintechResponse<VoidPlaceHolder> response = refundServiceFeign.debitForRepayDay();
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        LOG.info("succeed in debiting for repayday");
        return response;
    }

    /**
     * 逾期的还款分期(过滤掉人工处理的订单)，在最大逾期天数范围， 每天扣三次（8:00 12:00 16:00），扣款成功则更新状态为已还款
     * @return
     */
    @GetMapping(value = "/debit-for-overdue")
    public FintechResponse<VoidPlaceHolder> debitForOverdue() {
        LOG.info("debit-for-verdue");

        FintechResponse<VoidPlaceHolder> response = refundServiceFeign.debitForOverdue();
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        LOG.info("succeed in debiting for overdue");
        return response;
    }

    /**
     * 每天24:00扫描已过最大预期天数的订单(过滤掉人工处理的订单)，如果扣款失败， 变更状态为待退保
     * @return
     */

    @GetMapping(value = "/change-status-to-surrender")
    public FintechResponse<VoidPlaceHolder> changeStatusToSurrender() {
        LOG.info("change-status-to-surrender");

        FintechResponse<VoidPlaceHolder> response = refundServiceFeign.changeStatusToSurrender();
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        LOG.info("succeed in changing status to surrender");
        return response;
    }


    /**
     * 每天24:00扫描已过还款日期的订单变更状态为已逾期
     * @return
     */
    @GetMapping(value = "/change-status-to-overdue")
    public FintechResponse<VoidPlaceHolder> changeStatusToOverdue() {
        LOG.info("change-status-to-overdue");

        FintechResponse<VoidPlaceHolder> response = refundServiceFeign.changeStatusToOverdue();
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        LOG.info("succeed in changing status to overdue");
        return response;
    }

    /**
     * 每30分钟查询扣款状态为processing， confirmed的支付记录更新状态
     * @return
     */
    @GetMapping(value = "/change-status-from-yjf")
    public FintechResponse<VoidPlaceHolder> changeStatusFromYJF() {
        LOG.info("change-status-from-yjf");

        FintechResponse<VoidPlaceHolder> response = refundServiceFeign.changeStatusFromYJF();
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        LOG.info("succeed in changing status from yjf");
        return response;
    }



    /**
     * 还款日提醒：还款日前XXX日上午09:00推送微信与短息消息给客户
     * @return
     */
    @GetMapping(value = "/send-msg-for-repaydate")
    public FintechResponse<VoidPlaceHolder> sendMsgForRepayDate() {
        LOG.info("send-msg-for-repaydate");

        FintechResponse<VoidPlaceHolder> response = refundServiceFeign.sendMsgForRepayDate();
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        LOG.info("succeed in sending msg for repaydate");
        return response;
    }
}
