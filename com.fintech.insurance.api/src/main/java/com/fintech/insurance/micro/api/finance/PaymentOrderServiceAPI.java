package com.fintech.insurance.micro.api.finance;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.DebtStatus;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.dto.finance.PaymentOrderDetailVO;
import com.fintech.insurance.micro.dto.finance.PaymentOrderVO;
import com.fintech.insurance.micro.dto.finance.VoucherVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: qxy
 * @Description:
 * @Date: 2017/11/20 11:11
 */
@RequestMapping(value = "/finance/payment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface PaymentOrderServiceAPI {

    /**
     * 更新支付单状态
     * @param paymentOrderNumber
     * @param debtStatus
     */
    @GetMapping(value = "/change-status")
    void changePaymentOrderStatus(@RequestParam("paymentOrderNumber") String paymentOrderNumber,
                                  @RequestParam("debtTransactionSerialNum") String debtTransactionSerialNum,
                                  @RequestParam("debtStatus") DebtStatus debtStatus);
    /**
     * 人工处理
     * @param requisitionVO
     */
    @RequestMapping(value = "/mantreat", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> manTreatPayment(@RequestBody(required = false) RequisitionVO requisitionVO);

    /**
     * 支付凭证保存
     * @param voucherVO
     */
    @RequestMapping(value = "/save-voucher", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> saveVoucher(@RequestBody VoucherVO voucherVO );

    /**
     *  操作附件查询
     * @param userId
     * @param id
     * @param accountType
     * @return
     */
    @RequestMapping(value = "/get-by-user-and-entityid", method = RequestMethod.GET)
    FintechResponse<String[]> getByUserAndEntityId(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "id")Integer id, @RequestParam(value = "accountType") String accountType);

    /**
     * 生成支付订单
     * @param requisitionNumber
     */
    @RequestMapping(value = "/save-payment-order", method = RequestMethod.GET)
    FintechResponse<Integer> savePaymentOrder(@RequestParam(value = "requisitionNumber") String requisitionNumber);

    /**
     * 根据支付订单号获得支付订单
     * @param orderNumber   支付订单号
     * @return
     */
    @RequestMapping(value = "/get-by-ordernumber", method = RequestMethod.GET)
    FintechResponse<PaymentOrderVO> getByOrderNumber(@RequestParam(value = "orderNumber") String orderNumber);

    /**
     * 根据支付订单id获得支付明细信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/list-by-order-id", method = RequestMethod.GET)
    FintechResponse<List<PaymentOrderDetailVO>> listByOrderId(@RequestParam(value = "id") Integer id);

    /**
     * 改变支付单状态
     */
    @RequestMapping(value = "/change-debt-status", method = RequestMethod.GET)
    FintechResponse<VoidPlaceHolder> changeDebtStatusFromYJF();

    /**
     * 更新支付单扣款银行卡号
     */
    @RequestMapping(value = "/add-bank-account-number", method = RequestMethod.GET)
    FintechResponse<VoidPlaceHolder> updateBankAccountNumber(@RequestParam(value = "paymentOrderId") Integer paymentOrderId,
                                                             @RequestParam(value = "bankCardNumber") String bankCardNumber);
}
