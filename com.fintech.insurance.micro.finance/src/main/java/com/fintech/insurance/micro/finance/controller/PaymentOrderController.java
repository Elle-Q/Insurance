package com.fintech.insurance.micro.finance.controller;

import com.fintech.insurance.commons.enums.DebtStatus;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.finance.PaymentOrderServiceAPI;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.dto.finance.PaymentOrderDetailVO;
import com.fintech.insurance.micro.dto.finance.PaymentOrderVO;
import com.fintech.insurance.micro.dto.finance.VoucherVO;
import com.fintech.insurance.micro.feign.biz.RequisitionServiceFeign;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.finance.service.PaymentOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: qxy
 * @Description:
 * @Date: 2017/11/20 11:10
 */
@RestController
public class PaymentOrderController extends BaseFintechController implements PaymentOrderServiceAPI {

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private RequisitionServiceFeign requisitionServiceFeign;

    @Override
    public void changePaymentOrderStatus(String paymentOrderNumber, String debtTransactionSerialNum, DebtStatus debtStatus) {
        paymentOrderService.changePaymentOrderStatus(paymentOrderNumber, debtTransactionSerialNum, debtStatus);
    }

    @Override
    public FintechResponse<VoidPlaceHolder> manTreatPayment(@RequestBody(required = false) RequisitionVO requisitionVO) {
        if (null == requisitionVO) {
            requisitionVO = new RequisitionVO();
        }
        if (StringUtils.isEmpty(requisitionVO.getRequisitionNumber())) {
            throw new FInsuranceBaseException(105007);
        }
        FintechResponse<RequisitionVO> requisitionVOForManResponse = requisitionServiceFeign.getRequisitionByNumber(requisitionVO.getRequisitionNumber());
        if (!requisitionVOForManResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(requisitionVOForManResponse);
        }
        RequisitionVO requisitionVOForMan = requisitionVOForManResponse.getData();

        //判断订单是否是支付失败状态
        if (null == requisitionVOForMan) {
            throw new FInsuranceBaseException(105012);
        }
        if (!RequisitionStatus.FailedPayment.getCode().equals(requisitionVOForMan.getRequisitionStatus())) {
            throw new FInsuranceBaseException(105006);
        }
        if (null == requisitionVOForMan.getPaymentOrderNumber()) {
            throw new FInsuranceBaseException(105004);
        }
        paymentOrderService.manTreat(requisitionVOForMan);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> saveVoucher(@RequestBody VoucherVO voucherVO) {
        paymentOrderService.saveVoucher(voucherVO);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<String[]>  getByUserAndEntityId(@RequestParam(value = "userId") Integer userId, @RequestParam(value = "id")Integer id, @RequestParam(value = "accountType") String accountType) {
        return FintechResponse.responseData(paymentOrderService.getByUserAndEntityId(userId, id, accountType));
    }

    @Override
    public FintechResponse<Integer> savePaymentOrder(@RequestParam(value = "requisitionNumber") String requisitionNumber) {
        return FintechResponse.responseData(paymentOrderService.savePaymentOrder(requisitionNumber));
    }

    @Override
    public FintechResponse<PaymentOrderVO> getByOrderNumber(@RequestParam(value = "orderNumber") String orderNumber) {
        return FintechResponse.responseData(paymentOrderService.getByOrderNumber(orderNumber));
    }

    @Override
    public FintechResponse<List<PaymentOrderDetailVO>> listByOrderId(Integer id) {
        List<PaymentOrderDetailVO> data = paymentOrderService.listByOrderId(id);
        return FintechResponse.responseData(data);
    }

    @Override
    public FintechResponse<VoidPlaceHolder> changeDebtStatusFromYJF() {
        List<String> debtStatusList = new ArrayList<String>();
        debtStatusList.add(DebtStatus.PROCESSING.getCode());
        debtStatusList.add(DebtStatus.CONFIRMED.getCode());
        paymentOrderService.changeDebtStatus(debtStatusList);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> updateBankAccountNumber(@RequestParam(value = "paymentOrderId") Integer paymentOrderId,
                                                                    @RequestParam(value = "bankCardNumber") String bankCardNumber) {
        paymentOrderService.updateBankAccountNumber(paymentOrderId, bankCardNumber);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }
}
