package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.commons.annotations.FinanceDuplicateSubmitDisable;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.biz.ContractFileRequestVO;
import com.fintech.insurance.micro.dto.finance.BankCardVerifyResult;
import com.fintech.insurance.micro.dto.finance.DebtNotification;
import com.fintech.insurance.micro.dto.finance.DebtResult;
import com.fintech.insurance.micro.feign.finance.PaymentServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.HelloServiceFeign;
import com.fintech.insurance.service.agg.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/13 21:57
 */
@RestController
@RequestMapping(value = "/management/temptest/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TempTestController extends BaseFintechManagementController {

    @Autowired
    private PaymentServiceFeign paymentServiceFeign;

    @Autowired
    private HelloServiceFeign helloServiceFeign;

    @Autowired
    private AsyncService asyncService;

    @GetMapping(value = "/yjf/verify-card")
    FintechResponse<BankCardVerifyResult> verifyBankCard(@RequestParam(name = "userName") String userName,
                                        @RequestParam(name = "certNum") String certNum,
                                        @RequestParam(name = "bankCardNum") String bankCardNum,
                                        @RequestParam(name = "reservedMobile") String reservedMobile) {
        return paymentServiceFeign.verifyBankCard(userName, certNum, bankCardNum, reservedMobile);
    }

    @GetMapping(value = "/yjf/debt")
    FintechResponse<DebtResult> debt(@RequestParam(name = "userName") String userName,
                    @RequestParam(name = "certNum") String certNum,
                    @RequestParam(name = "bankCardNum") String bankCardNum,
                    @RequestParam(name = "reservedMobile") String reservedMobile,
                    @RequestParam(name = "amount") @FinanceDataPoint Double amount,
                    @RequestParam(name = "contactInfo") String contactInfo) {
        return paymentServiceFeign.debt(userName, certNum, bankCardNum, reservedMobile, amount, contactInfo);
    }

    @GetMapping(value = "/test/financedata")
    FintechResponse<Double> financeData(@RequestParam(name = "amount") @FinanceDataPoint Double amount) {
        return FintechResponse.responseData(amount);
    }

    @PostMapping(value = "/test/enum")
    FintechResponse<ContractFileRequestVO> testEnum(@RequestBody ContractFileRequestVO contactInfo) {
        return FintechResponse.responseData(contactInfo);
    }

    @GetMapping(value = "/test/get/enum")
    FintechResponse<ProductType> testGetEnum(@RequestParam ProductType productType) {
        return FintechResponse.responseData(productType);
    }

    @PostMapping(value = "/test/void")
    FintechResponse<VoidPlaceHolder> testVoid() {
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    /**
     * 更新扣款业务的状态信息， 一般是从易极付的通知发起调用
     * @param debtNotification
     */
    @PostMapping(value = "/debt/update")
    void debtNotify(@RequestBody DebtNotification debtNotification) {
        paymentServiceFeign.debtNotify(debtNotification);
    }

    @GetMapping(value = "/test/async")
    String testAsync() {
        //asyncService.testTest();
        return "finished.";
    }

    @GetMapping(value = "/test/exception/throw")
    FintechResponse<Integer> testExceptionThrown() {
        FintechResponse<String> result = helloServiceFeign.testException("abc");
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return FintechResponse.responseData(1) ;
    }

    @GetMapping(value = "/test/exception/throw2")
    @FinanceDuplicateSubmitDisable
    FintechResponse<Integer> testExceptionThrown2() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(1==1) {
            throw new FInsuranceBaseException(000000);
        }

        return FintechResponse.responseData(1) ;
    }

    @GetMapping(value = "/test/exception/test2")
    @FinanceDuplicateSubmitDisable(value = 20)
    FintechResponse<Integer> test2() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return FintechResponse.responseData(1) ;
    }
}
