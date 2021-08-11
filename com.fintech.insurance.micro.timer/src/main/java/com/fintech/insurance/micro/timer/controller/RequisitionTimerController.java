package com.fintech.insurance.micro.timer.controller;

import com.fintech.insurance.commons.annotations.FInsuranceTimer;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.feign.biz.RequisitionServiceFeign;
import com.fintech.insurance.micro.feign.finance.PaymentOrderServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 申请单
 * @Author: qxy
 * @Date: 2017/12/19 16:24
 */
@RestController
@RequestMapping(value = "/timer/requisition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RequisitionTimerController {

    private static final Logger LOG = LoggerFactory.getLogger(RequisitionTimerController.class);

    @Autowired
    private RequisitionServiceFeign requisitionServiceFeign;

    @Autowired
    private PaymentOrderServiceFeign paymentOrderServiceFeign;

    /**
     * 系统每天24:00取消未确认的申请单
     * @return
     */
    @GetMapping(value = "/cancel-for-unconfirmed")
    public FintechResponse<VoidPlaceHolder> cancelForUnconfirmed() {

        LOG.info("cancel requisition who's status is unconfirmed(submitted)");
        FintechResponse<VoidPlaceHolder> response = requisitionServiceFeign.cancelForUnconfirmed();
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        LOG.info("succeed in canceling requisition who's status is unconfirmed(submited)");
        return response;
    }

    /**
     * 系统每天24:00取消T-1日待支付的申请单
     * @return
     */
    @GetMapping(value = "/cancel-for-waitingpayment")
    public FintechResponse<VoidPlaceHolder> cancelForWaitingpayment() {

        LOG.info("cancel requisition who's status is waitpayment");

        FintechResponse<VoidPlaceHolder> response = requisitionServiceFeign.cancelForWaitingpayment();
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        LOG.info("succeed in canceling requisition who's status is waitpayment");
        return response;
    }

    /**
     * 每30分钟申请的服务支付的状态进行更新: 查询扣款状态为processing， confirmed的支付记录更新状态
     * @return
     */
    @GetMapping(value = "/change-payment-status-from-yjf")
    public FintechResponse<VoidPlaceHolder> changeServiceFeePaymentStatusFromYJF() {
        LOG.info("start change-payment-status-from-yjf");

        FintechResponse<VoidPlaceHolder> response = paymentOrderServiceFeign.changeDebtStatusFromYJF();
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        LOG.info("succeed in change-payment-status from yjf");
        return response;
    }
}
