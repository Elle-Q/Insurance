package com.fintech.insurance.micro.customer.webchat.controller;

import com.fintech.insurance.commons.enums.ApplicationType;
import com.fintech.insurance.commons.enums.NotificationEvent;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.customer.CustomerAccountVO;
import com.fintech.insurance.micro.dto.customer.CustomerSimpleVO;
import com.fintech.insurance.micro.dto.finance.BankCardVerifyResult;
import com.fintech.insurance.micro.dto.finance.EnterpriseBankVO;
import com.fintech.insurance.micro.dto.thirdparty.ImageVercodeVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSCheckVerificationParamVO;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.finance.EnterpriseBankServiceFeign;
import com.fintech.insurance.micro.feign.finance.PaymentServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.sms.SMSServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/13 13:45
 */
@RestController
@RequestMapping("/wechat/customer/bankcard")
@RequireWechatLogin
public class CustomerBankCardController extends BaseFintechWechatController {
    @Autowired
    private VerifyCodeController verifyCodeController;

    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    @Autowired
    private SMSServiceFeign smsServiceFeign;

    @Autowired
    private PaymentServiceFeign paymentServiceFeign;

    @Autowired
    private EnterpriseBankServiceFeign enterpriseBankServiceFeign;

    @GetMapping("/bankcard-info")
    public FintechResponse<CustomerAccountVO> getBankCardInfo() throws IOException {

        // 获取客户信息
        FintechResponse<CustomerSimpleVO> simpleVOResponse = customerServiceFeign.getCustomerSimpleInfo(null);
        if (simpleVOResponse == null || simpleVOResponse.getData() == null) {
            throw new FInsuranceBaseException(103007);
        }
        CustomerSimpleVO simpleVO = simpleVOResponse.getData();

        CustomerAccountVO vo = new CustomerAccountVO();
        if (simpleVO.getBankCardNumber() != null) {
            StringBuilder stringBuilder = new StringBuilder(simpleVO.getBankCardNumber());
            vo.setBankCardNumber("**** **** **** " + stringBuilder.substring(stringBuilder.length() - 4));
            // 通过银行编码找到银行名称
            FintechResponse<EnterpriseBankVO> enterpriseBankResponse = enterpriseBankServiceFeign.getEnterpriseBank(ApplicationType.DEBT.getCode(), simpleVO.getBankName());
            if (enterpriseBankResponse == null || enterpriseBankResponse.getData() == null) {
                throw new FInsuranceBaseException(107037);
            }
            EnterpriseBankVO enterpriseBankVO = enterpriseBankResponse.getData();
            // 银行名称
            vo.setBankName(enterpriseBankVO.getBankName());
            // 银行编码
            vo.setBankCode(simpleVO.getBankName());
        }
        // 客户名称
        vo.setName(simpleVO.getCustomerName());
        // 身份证号
        vo.setIdNumber(simpleVO.getIdNumber());
        return FintechResponse.responseData(vo);
    }

    @GetMapping("/customer-info")
    public FintechResponse<CustomerAccountVO> getCustomerInfo() throws IOException {
        // 获取图片验证码
        FintechResponse<ImageVercodeVO> response = verifyCodeController.getVercode();
        if (response == null || response.getData() == null) {
            throw new FInsuranceBaseException(107008);
        }

        ImageVercodeVO imageVercodeVO = response.getData();

        // 获取客户信息
        FintechResponse<CustomerSimpleVO> simpleVOResponse = customerServiceFeign.getCustomerSimpleInfo(null);
        if (simpleVOResponse == null || simpleVOResponse.getData() == null) {
            throw new FInsuranceBaseException(103007);
        }
        CustomerSimpleVO simpleVO = simpleVOResponse.getData();

        CustomerAccountVO vo = new CustomerAccountVO();
        vo.setName(simpleVO.getCustomerName());
        vo.setIdNumber(simpleVO.getIdNumber());
        vo.setSerialNumber(imageVercodeVO.getVercodeId());
        vo.setVerificationCode(imageVercodeVO.getImage());

        return FintechResponse.responseData(vo);
    }

    @PostMapping("/save")
    public void saveBankCardInfo(@Validated @RequestBody CustomerAccountVO accountVO) {
        // 获取客户信息
        FintechResponse<CustomerSimpleVO> simpleVOResponse = customerServiceFeign.getCustomerSimpleInfo(null);
        if (!simpleVOResponse.isOk()) {
            throw new FInsuranceBaseException(103007);
        }
        CustomerSimpleVO simpleVO = simpleVOResponse.getData();
        //校验短信验证码
        SMSCheckVerificationParamVO smsCheckVerificationParamVO = new SMSCheckVerificationParamVO();
        smsCheckVerificationParamVO.setEventCode(NotificationEvent.BIND_CARD.getCode());
        smsCheckVerificationParamVO.setPhoneNumber(accountVO.getPhone());
        smsCheckVerificationParamVO.setSequenceId(accountVO.getSequenceId());
        smsCheckVerificationParamVO.setVerification(accountVO.getVerification());
        FintechResponse<Boolean> fintechResponse = smsServiceFeign.checkSMSVerification(smsCheckVerificationParamVO);
        if (!fintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(fintechResponse);
        }
        Boolean flag = fintechResponse.getData();
        if (!flag) {
            throw new FInsuranceBaseException(107023);//短信校验出错
        }

        FintechResponse<BankCardVerifyResult> response = paymentServiceFeign.verifyBankCard(simpleVO.getCustomerName(), simpleVO.getIdNumber(), accountVO.getBankCardNumber(), accountVO.getPhone());
        if (response == null || response.getData() == null || !response.getData().getIsSuccess()) {
            throw new FInsuranceBaseException(107011);// 四要素认证失败
        }
        String bankCode = response.getData().getBankCode();
        // 四要素认证通过后还需要查看该银行卡是否允许扣款
        FintechResponse<EnterpriseBankVO> enterpriseBankResponse = enterpriseBankServiceFeign.getEnterpriseBank(ApplicationType.DEBT.getCode(), bankCode);
        if (!enterpriseBankResponse.isOk()) {
            throw new FInsuranceBaseException(107046, new Object[]{bankCode});
        }
        if (enterpriseBankResponse.getData() == null) {
            throw new FInsuranceBaseException(107038, new Object[]{response.getData().getBankName()});
        }
        accountVO.setBankName(bankCode);
        customerServiceFeign.updateBankCardInfo(accountVO);
    }
}
