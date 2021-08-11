package com.fintech.insurance.micro.finance.service;

import com.fintech.insurance.commons.enums.DebtStatus;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.dto.finance.PaymentOrderDetailVO;
import com.fintech.insurance.micro.dto.finance.PaymentOrderVO;
import com.fintech.insurance.micro.dto.finance.VoucherVO;

import java.util.List;

public interface PaymentOrderService {
    /**
     * 人工处理
     * @param requisitionVO
     */
    void manTreat(RequisitionVO requisitionVO);

    /**
     * 保存支付凭证
     * @param voucherVO
     */
    void saveVoucher(VoucherVO voucherVO);

    String[] getByUserAndEntityId(Integer userId, Integer entityId, String accountType);

    /**
     * 生成支付订单
     * @param requisitionNumber
     */
    Integer savePaymentOrder(String requisitionNumber);

    /**
     * 根据申请单编号获得支付单信息
     * @param requisitionNumber
     * @return
     */
    PaymentOrderVO getByOrderNumber(String requisitionNumber);

    /**
     * 根据支付订单id获得支付明细信息
     * @param id
     * @return
     */
    List<PaymentOrderDetailVO> listByOrderId(Integer id);

    void deletePaymentOrderAndPaymentOrderDetail(Integer paymentOrderId);

    void changePaymentOrderStatus(String paymentOrderNumber, String debtTransactionSerialNum, DebtStatus status);

    /**
     * 查询扣款状态为处理中的支付单
     * @param debtStatusList
     * @return
     */
    void changeDebtStatus(List<String> debtStatusList);

    String getBankcardNumByDebtSerialNum(String debtSerialNum);

    void updateBankAccountNumber(Integer paymentOrderId, String bankCardNumber);
}
