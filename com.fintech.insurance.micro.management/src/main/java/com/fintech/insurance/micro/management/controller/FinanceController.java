package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.feign.finance.PaymentOrderServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/7 16:22
 */
@RestController
@RequestMapping(value = "/management/finance", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FinanceController extends BaseFintechManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceController.class);

    @Autowired
    private PaymentOrderServiceFeign paymentOrderServiceFeign;
    /**
     * 人工处理
     * @param requisitionVO
     */
    @RequestMapping(value = "/payment/mantreat", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> manTreat(@RequestBody(required = false) RequisitionVO requisitionVO) {
        FintechResponse<VoidPlaceHolder> result = paymentOrderServiceFeign.manTreatPayment(requisitionVO);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

}
