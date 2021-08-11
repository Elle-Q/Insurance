package com.fintech.insurance.micro.timer.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.feign.finance.PaymentServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 验卡
 * @Author: Nicholas
 * @Date: 2018/1/22
 */
@RestController
@RequestMapping(value = "/timer/verifyBankCard", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class VerifyBankCardTimerController {

    private static final Logger LOG = LoggerFactory.getLogger(VerifyBankCardTimerController.class);

    @Autowired
    private PaymentServiceFeign paymentServiceFeign;

    /**
     * 系统过一段时间查询银行卡验卡结果
     * @return
     */
    @GetMapping(value = "/update-verify-bankcard-record")
    public FintechResponse<VoidPlaceHolder> queryAndUpdateVerifyBankcardRecord() {
        FintechResponse<VoidPlaceHolder> response = paymentServiceFeign.queryAndUpdateVerifyBankcardRecord();
        return response;
    }
}
