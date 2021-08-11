package com.fintech.insurance.micro.customer.webchat.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.thirdparty.ImageVercodeVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendResultVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendVerificationParamVO;
import com.fintech.insurance.micro.feign.thirdparty.ImageVercodeServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.sms.SMSServiceFeign;
import com.fintech.insurance.micro.vo.wechat.SendVerificationParamVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * WX 验证码
 * @author qxy
 */
@RestController
@RequestMapping(value = "/wechat/customer/verify-code", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class VerifyCodeController extends BaseFintechWechatController {

    @Autowired
    private ImageVercodeServiceFeign imageVercodeServiceFeign;

    @Autowired
    private SMSServiceFeign smsServiceFeign;

    /**
     * 获取图片验证码
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public FintechResponse<ImageVercodeVO> getVercode() throws IOException{
        FintechResponse<ImageVercodeVO> response = imageVercodeServiceFeign.getVercode();
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    /**
     * 校验验证码是否正确
     *
     * @param vercodeId 图片验证码标识
     * @param content 用户填写的验证码内容
     * @return 正确与否，如果验证码过期也返回false
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public FintechResponse<Integer> checkVercode(@RequestParam(name = "vercodeId") String vercodeId,
                                          @RequestParam(name = "content") String content){
        FintechResponse<Integer> response = imageVercodeServiceFeign.checkVercode(vercodeId, content);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    /**
     * 校验图片验证码，发送短信验证码
     */
    @RequestMapping(value = "/send")
    public FintechResponse<SMSSendResultVO> sendVerifyCode(@RequestBody SendVerificationParamVO sendVerificationParamVO) {
        boolean checkFlag = true;
        if (StringUtils.isNotEmpty(sendVerificationParamVO.getVercodeId()) && StringUtils.isNotEmpty(sendVerificationParamVO.getContent())) {
            FintechResponse<Integer> response = imageVercodeServiceFeign.checkVercode(sendVerificationParamVO.getVercodeId(), sendVerificationParamVO.getContent());
            if (!response.isOk()) {
                throw FInsuranceBaseException.buildFromErrorResponse(response);
            }
            checkFlag = (response.getData() == 0);
        }
        if (checkFlag) {
            throw new FInsuranceBaseException(107022);
        }
        SMSSendVerificationParamVO smsSendVerificationParamVO = new SMSSendVerificationParamVO();
        smsSendVerificationParamVO.setEventCode(sendVerificationParamVO.getEventCode());
        smsSendVerificationParamVO.setPhoneNumber(sendVerificationParamVO.getPhoneNumber());
        return smsServiceFeign.sendSMSVerification(smsSendVerificationParamVO);
    }

}
