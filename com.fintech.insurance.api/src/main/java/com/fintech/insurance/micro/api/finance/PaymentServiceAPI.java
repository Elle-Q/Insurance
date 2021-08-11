package com.fintech.insurance.micro.api.finance;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.finance.BankCardVerifyResult;
import com.fintech.insurance.micro.dto.finance.BankcardDebtInfoVO;
import com.fintech.insurance.micro.dto.finance.DebtNotification;
import com.fintech.insurance.micro.dto.finance.DebtResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 支付服务接口
 * @Author: Yong Li
 * @Date: 2017/12/13 10:22
 */
@RequestMapping(value = "/finance/bank-service", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface PaymentServiceAPI {

    /**
     * 根据用户的姓名 身份证号  银行卡号 银行卡预留手机号码做四要素验证（不发送短信验证码, 短信验证码由本平台内部发）。
     * 
     *
     * @param userName 用户真实姓名
     * @param certNum 身份证号
     * @param bankCardNum 银行卡号
     * @param reservedMobile 银行卡预留手机号码
     */
    @GetMapping(value = "/verify-card")
    FintechResponse<BankCardVerifyResult> verifyBankCard(@RequestParam(name = "userName") String userName,
                                                        @RequestParam(name = "certNum") String certNum,
                                                        @RequestParam(name = "bankCardNum") String bankCardNum,
                                                        @RequestParam(name = "reservedMobile") String reservedMobile);

    @GetMapping(value = "/check-debt")
    FintechResponse<Boolean> checkDebtAvailable(@RequestParam(name = "bankCardNum") String bankCardNum);

    /**
     * 根据指定的用户信息对银行卡进行扣款操作， 返回结果只表示第三方支付对扣款业务的受理状态， 并不表示扣款已经
     * 成功。 扣款的最终状态需要后面进行查询.
     *
     * @param userName 用户真实姓名
     * @param certNum 身份证号
     * @param bankCardNum 银行卡号
     * @param reservedMobile 银行卡预留手机号码
     * @param amount 扣款金额
     * @param contactInfo 合同信息, 当有多个合同合并扣款时，可以把多个合同编号拼成字符串的形式，如"contract1, contract2, contract3"
     *
     * @return 是否受理成功
     */
    @GetMapping(value = "/debt")
    FintechResponse<DebtResult> debt(@RequestParam(name = "userName") String userName,
                              @RequestParam(name = "certNum") String certNum,
                              @RequestParam(name = "bankCardNum") String bankCardNum,
                              @RequestParam(name = "reservedMobile") String reservedMobile,
                              @RequestParam(name = "amount") @FinanceDataPoint Double amount,
                              @RequestParam(name = "contactInfo") String contactInfo);

    /**
     * 查询易极付扣款订单状态
     *
     * @param platformDebtOrderNum 平台扣款订单号
     * @return
     *//*
    @GetMapping(value = "/query-debt")
    FintechResponse<DebtStatus> queryDebtStatus(@RequestParam(name = "platformDebtOrderNum") String platformDebtOrderNum);*/

    /**
     * 更新扣款业务的状态信息， 一般是从易极付的通知发起调用
     * @param debtNotification
     */
    @PostMapping(value = "/debt/update")
    FintechResponse<VoidPlaceHolder> debtNotify(@RequestBody DebtNotification debtNotification);


    /**
     * 查询第三方验卡结果查询并更新数据库验卡结果
     */
    @GetMapping(value = "/update-verify-record")
    FintechResponse<VoidPlaceHolder> queryAndUpdateVerifyBankcardRecord();

    @GetMapping(value = "/debt/banknum/info")
    FintechResponse<BankcardDebtInfoVO> queryBankcardDebtInfo(@RequestParam(name = "bankCardNum") String bankCardNum);

}
