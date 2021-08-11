package com.fintech.insurance.micro.biz.service.notification;

import com.fintech.insurance.micro.dto.biz.ContractVO;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/12/1 0001 9:50
 */
public interface NotificationService {

    /**
     * 发送审批通知，运营、风控、领导审核通过后，提醒支付
     *
     * @param requisitionVO 申请单
     */
    public void sendAuditNotification(RequisitionVO requisitionVO);

    /**
     * 发送放款通知，放款提醒
     *
     * @param requisitionVO 申请单
     */
    public void sendLoanConfirmNotification(RequisitionVO requisitionVO);

    /**
     * 发送还款提醒通知，还款日前XXX日提醒
     *
     * @param contractVO      合同
     * @param repaymentPlanVO 还款计划
     */
    public void sendRepaymentRemindNotification(ContractVO contractVO, FinanceRepaymentPlanVO repaymentPlanVO);

    /**
     * 发送逾期提醒通知，逾期提醒通知，过了还款日没过最大逾期天数期间扣款失败提醒
     *
     * @param contractVO      合同
     * @param repaymentPlanVO 还款计划
     */
    public void sendOverdueRemindNotification(ContractVO contractVO, FinanceRepaymentPlanVO repaymentPlanVO);

    /**
     * 发送还款结果通知
     *
     * @param contractVO      合同
     * @param repaymentPlanVO 还款计划
     */
    public void sendRepaymentResultNotification(ContractVO contractVO, FinanceRepaymentPlanVO repaymentPlanVO);
}
