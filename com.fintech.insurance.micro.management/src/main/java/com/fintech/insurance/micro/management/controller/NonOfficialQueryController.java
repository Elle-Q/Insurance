package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.finance.BankcardDebtInfoVO;
import com.fintech.insurance.micro.feign.finance.PaymentServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2018/1/19 20:28
 */
@RestController
@RequestMapping(value = "/management/non-official/query", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class NonOfficialQueryController extends BaseFintechManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(NonOfficialQueryController.class);

    @Autowired
    private PaymentServiceFeign paymentServiceFeign;

    @GetMapping(value = "/debt/banknum/info")
    FintechResponse<BankcardDebtInfoVO> queryBankcardDebtInfo(@RequestParam(name = "bankCardNum") String bankCardNum) {
        return paymentServiceFeign.queryBankcardDebtInfo(bankCardNum);
    }

}
