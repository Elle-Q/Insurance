package com.fintech.insurance.micro.timer.scheduled;

import com.fintech.insurance.commons.annotations.FInsuranceTimer;
import com.fintech.insurance.micro.timer.controller.RequisitionTimerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 申请单
 * @author qxy
 * @since 2017/12/19 16:24
 */
@Component
@Async
public class RequisitionScheduler {

    @Autowired
    private RequisitionTimerController requisitionTimerController;

    /**
     * 系统每天24:00取消未确认的申请单
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @FInsuranceTimer(name = "cancelForUnconfirmedTimer", desc = "取消未确认的申请单")
    public void cancelForUnconfirmed() {
        this.requisitionTimerController.cancelForUnconfirmed();
    }

    /**
     * 系统每天24:00取消T-1日待支付的申请单
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @FInsuranceTimer(name = "cancelForWaitingpaymentTimer", desc = "取消T-1日待支付的申请单")
    public void cancelForWaitingpayment() {
        this.requisitionTimerController.cancelForWaitingpayment();
    }

    /**
     * 每30分钟申请的服务支付的状态进行更新: 查询扣款状态为processing， confirmed的支付记录更新状态
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    @FInsuranceTimer(name = "changeServiceFeePaymentStatusFromYJFTimer", desc = "申请的服务支付的状态进行更新")
    public void changeServiceFeePaymentStatusFromYJF() {
        this.requisitionTimerController.changeServiceFeePaymentStatusFromYJF();
    }
}
