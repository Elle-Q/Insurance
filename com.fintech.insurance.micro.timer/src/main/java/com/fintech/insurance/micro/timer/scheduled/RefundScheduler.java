package com.fintech.insurance.micro.timer.scheduled;

import com.fintech.insurance.commons.annotations.FInsuranceTimer;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.timer.controller.RefundTimerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 还款
 * @author qxy
 * @since 2017/12/19 16:24
 */
@Component
@Async
public class RefundScheduler {

    @Autowired
    private RefundTimerController refundTimerController;

    /**
     * 正常申请单还款日自动代扣： 一天三次 8:00 12:00 16:00
     */
    @Scheduled(cron = "0 0 8,12,16 * * ?")
    @FInsuranceTimer(name = "debitForRepayDayTimer", desc = "正常申请单还款日自动代扣")
    public void debitRequisitionForRepayDay() {
        this.refundTimerController.debitRequisitionForRepayDay();
    }

    /**
     * 逾期的还款分期(过滤掉人工处理的订单)，在最大逾期天数范围， 每天扣三次（8:00 12:00 16:00），扣款成功则更新状态为已还款
     */
    @Scheduled(cron = "0 0 8,12,16 * * ?")
    @FInsuranceTimer(name = "debitForOverdueTimer", desc = "逾期的还款分期(过滤掉人工处理的订单)代扣")
    public void debitForOverdue() {
        this.refundTimerController.debitForOverdue();
    }

    /**
     * 每天24:00扫描已过最大预期天数的订单(过滤掉人工处理的订单)，如果扣款失败， 变更状态为待退保
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @FInsuranceTimer(name = "changeStatusToSurrenderTimer", desc = "已过最大预期天数的订单(过滤掉人工处理的订单)，如果扣款失败， 变更状态为待退保")
    public void changeStatusToSurrender() {
        this.refundTimerController.changeStatusToSurrender();
    }


    /**
     * 每天24:00扫描已过还款日期的订单变更状态为已逾期
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @FInsuranceTimer(name = "changeStatusToOverdueTimer", desc = "已过还款日期的订单变更状态为已逾期")
    public void changeStatusToOverdue() {
        this.refundTimerController.changeStatusToOverdue();
    }

    /**
     * 每30分钟查询扣款状态为processing， confirmed的支付记录更新状态
     */
    @Scheduled(cron = "0 0/5  * * * ?")
    @FInsuranceTimer(name = "changeStatusFromYJFTimer", desc = "查询扣款状态为processing， confirmed的支付记录更新状态")
    public void changeStatusFromYJF() {
        this.refundTimerController.changeStatusFromYJF();
    }



    /**
     * 还款日提醒：还款日前XXX日上午09:00推送微信与短息消息给客户
     */
    @Scheduled(cron = "0 0 9 * * ?")
    @FInsuranceTimer(name = "sendMsgForRepayDateTimer", desc = "还款日提醒：推送微信与短息消息给客户")
    public void sendMsgForRepayDate() {
        this.refundTimerController.sendMsgForRepayDate();
    }
}
