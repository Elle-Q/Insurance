package com.fintech.insurance.micro.finance.controller;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.api.finance.PaymentServiceAPI;
import com.fintech.insurance.micro.dto.finance.BankCardVerifyResult;
import com.fintech.insurance.micro.dto.finance.BankcardDebtInfoVO;
import com.fintech.insurance.micro.dto.finance.DebtNotification;
import com.fintech.insurance.micro.dto.finance.DebtResult;
import com.fintech.insurance.micro.feign.retrieval.BizQueryFeign;
import com.fintech.insurance.micro.finance.service.PaymentService;
import com.fintech.insurance.micro.finance.service.UserDebtRedisService;
import com.fintech.insurance.micro.finance.service.yjf.YjfPropertiesBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 支付服务接口
 * @Author: Yong Li
 * @Date: 2017/12/13 9:57
 */
@RestController
public class PaymentServiceController implements PaymentServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentServiceController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserDebtRedisService userDebtRedisService;

    @Autowired
    private BizQueryFeign bizQueryFeign;

    @Autowired
    private YjfPropertiesBean yjfPropertiesBean;

    @Override
    public FintechResponse<BankCardVerifyResult> verifyBankCard(@RequestParam(name = "userName") String userName,
                                                                @RequestParam(name = "certNum") String certNum,
                                                                @RequestParam(name = "bankCardNum") String bankCardNum,
                                                                @RequestParam(name = "reservedMobile") String reservedMobile) {
        LOG.info("prepare to verify bankcard: userName-{}, certNum-{}, bankCardNum-{}, mobile-{}", userName, certNum, bankCardNum, reservedMobile);
        BankCardVerifyResult result = paymentService.verifyBankCard(userName, certNum, bankCardNum, reservedMobile);
        LOG.info("verify result: serialNum-{}, isPass-{}, errorMessage-{}, bankName-{}, bankCode-{}", result.getRequestSerialNum(),
                result.getIsSuccess(), result.getFailedMessage(), result.getBankName(), result.getBankCode());
        return FintechResponse.responseData(result);
    }

    @Override
    public FintechResponse<Boolean> checkDebtAvailable(@RequestParam(name = "bankCardNum") String bankCardNum) {
        return FintechResponse.responseData(userDebtRedisService.isDebtAvailable(bankCardNum));
    }

    @Override
    public FintechResponse<DebtResult> debt(@RequestParam(name = "userName") String userName,
                                     @RequestParam(name = "certNum") String certNum,
                                     @RequestParam(name = "bankCardNum") String bankCardNum,
                                     @RequestParam(name = "reservedMobile") String reservedMobile,
                                     @RequestParam(name = "amount") @FinanceDataPoint Double amount,
                                     @RequestParam(name = "contactInfo") String contactInfo) {
        LOG.info("prepare to debt money from bankcard: userName-{}, certNum-{}, bankCardNum-{}, mobile-{}, amount-{}, contactInfo-{}", userName,
                certNum, bankCardNum, reservedMobile, amount, contactInfo);
        DebtResult result = paymentService.debtAmount(userName, certNum, bankCardNum, reservedMobile, amount, contactInfo);
        LOG.info("Debt result: serialNum-{}, isPass-{}, errorMessage-{}, amount-{}", result.getRequestSerialNum(),
                result.getIsSuccess(), result.getFailedMessage(), result.getAmount());
        return FintechResponse.responseData(result);
    }

    /*@Override
    public FintechResponse<DebtStatus> queryDebtStatus(@RequestParam(name = "platformDebtOrderNum") String platformDebtOrderNum) {
        LOG.info("prepare to query debt status for debt order: {}", platformDebtOrderNum);
        DebtStatus debtStatus = paymentService.queryDebtStatus(platformDebtOrderNum, "");
        LOG.info("Debt status: {} for debt order: {}", debtStatus.getCode(), platformDebtOrderNum);
        return FintechResponse.responseData(debtStatus);
    }*/

    @Override
    public FintechResponse<VoidPlaceHolder> debtNotify(@RequestBody DebtNotification debtNotification) {
        LOG.info("received Debt notification: {}", debtNotification.toString());
        paymentService.saveNotification(debtNotification);
        LOG.info("Had persistent Debt notification: {}", debtNotification.toString());
        paymentService.updateDebtStatusByNotification(debtNotification);
        LOG.info("process notification success");
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> queryAndUpdateVerifyBankcardRecord() {
        LOG.info("process query verify bank card");
        paymentService.queryAndUpdateVerifyBankcardRecord();
        LOG.info("process query verify bank card successfully");
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<BankcardDebtInfoVO> queryBankcardDebtInfo(@RequestParam(name = "bankCardNum") String bankCardNum) {
        BankcardDebtInfoVO vo = new BankcardDebtInfoVO();
        vo.setUsedDebtTimes(userDebtRedisService.getUsedDebtTimes(bankCardNum));
        vo.setTotalDebtTimes(yjfPropertiesBean.getDebtTimes());
        vo.setDebtedAmount(userDebtRedisService.countTotalDebtedAmount(bankCardNum));
        return FintechResponse.responseData(vo);
    }
}
