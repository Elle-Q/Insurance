package com.fintech.insurance.micro.finance.service;

import com.fintech.insurance.commons.enums.DebtStatus;
import com.fintech.insurance.micro.dto.finance.BankCardVerifyResult;
import com.fintech.insurance.micro.dto.finance.DebtNotification;
import com.fintech.insurance.micro.dto.finance.DebtResult;
import com.fintech.insurance.micro.dto.finance.YjfNotification;
import com.fintech.insurance.micro.finance.persist.entity.RepaymentRecord;
import com.fintech.insurance.micro.finance.service.yjf.model.DebtQueryResponse;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description:
 * @Author: Yong Li
 * @Date: 2017/12/11 19:52
 */
public interface PaymentService {

    /**
     * * 根据用户的姓名 身份证号  银行卡号 银行卡预留手机号码做四要素+短信验证
     *
     * @param userName 用户真实姓名
     * @param certNum 身份证号
     * @param bankCardNum 银行卡号
     * @param reservedMobile 银行卡预留手机号码
     * @return 验卡结果对象
     */
    BankCardVerifyResult verifyBankCard(String userName, String certNum, String bankCardNum, String reservedMobile);

    /***
     * 代扣接口
     *
     * @param userName 用户真实姓名
     * @param certNum 身份证号
     * @param bankCardNum 银行卡号
     * @param reservedMobile 银行卡预留手机号码
     * @param amount 扣款金额
     * @param contactInfo 合同信息
     *
     * @return 是否受理成功
     */
    DebtResult debtAmount(String userName, String certNum, String bankCardNum, String reservedMobile, Double amount, String contactInfo);

    /**
     * 查询扣款订单的状态
     *
     * @param platformDebtOrderNum 扣款订单号
     * @return 扣款状态
     */
    DebtStatus queryDebtStatus(String platformDebtOrderNum, String debtBankcardNum);

    /**
     * 查询扣款订单的状态并返回易极付的原始响应结果
     * @param platformDebtOrderNum
     * @param debtBankcardNum
     * @return
     */
    DebtQueryResponse queryDebtStatusWithYjfResponse(String platformDebtOrderNum, String debtBankcardNum);

    /**
     * 将易极付的查询结果转换为DebtStatus
     * @param platformDebtOrderNum
     * @param debtBankcardNum
     * @param response
     * @return
     */
    DebtStatus convertDebtQueryResponseToDebtStatus(String platformDebtOrderNum, String debtBankcardNum, DebtQueryResponse response);

    /**
     * 根据指定的通知更新扣款单的处理状态， 主要完成：
     * 1. 根据最新的处理状态更新支付记录的扣款状态
     * 2. 根据支付记录的状态变更决定是否需要更新还款计划的状态
     *
     * @param debtNotification 易极付通知对象
     */
    void updateDebtStatusByNotification(@RequestBody DebtNotification debtNotification);

    /**
     * 更新服务单支付状态
     * @param debtOrderNum
     * @param newDebtStatus
     */
    void updatePaymentOrderDebtStatus(String debtOrderNum, DebtStatus newDebtStatus);

    /**
     * 更新还款计划支付状态
     * @param debtOrderNum
     * @param newDebtStatus
     * @param resultMessage
     */
    void updateRepaymentRecordsDebtStatus(String debtOrderNum, DebtStatus newDebtStatus, String resultMessage);

    /**
     * 尝试针对同一个用户的同一个银行卡发起另一次扣款（参见代扣策略方案文档）
     */
    void tryTheSecondDebt(RepaymentRecord sampleRecord);

    /**
     * 在数据库中持久化易极付的通知消息
     *
     * @param notification
     */
    void saveNotification(YjfNotification notification);

    /**
     * 查询验卡结果并更新结果
     */
    void queryAndUpdateVerifyBankcardRecord();


}
