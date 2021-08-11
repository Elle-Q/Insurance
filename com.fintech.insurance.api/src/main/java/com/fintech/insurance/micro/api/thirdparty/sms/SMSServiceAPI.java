package com.fintech.insurance.micro.api.thirdparty.sms;

import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.thirdparty.sms.*;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Description: 短信服务接口
 * @Author: East
 * @Date: 2017/11/13 0013 9:58
 */
@RequestMapping(path = "/thirdparty/sms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface SMSServiceAPI {

    /**
     * 发送短信
     *
     * @param smsSendParamVO 短信参数
     * @return
     */
    @RequestMapping(path = "/send-sms", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    FintechResponse<SMSSendResultVO> sendSMS(@RequestBody @Validated SMSSendParamVO smsSendParamVO);

    /**
     * 查询短信
     *
     * @param smsQueryParamVO 短信参数
     * @return
     */
    @RequestMapping(path = "/query-sms", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    FintechResponse<Pagination<SMSSendDetailVO>> querySMS(@RequestBody SMSQueryParamVO smsQueryParamVO);

    /**
     * 发送短信验证码
     *
     * @param smsSendVerificationParamVO 短信参数
     * @return 返回短信验证码发送信息
     */
    @RequestMapping(path = "/send-sms-verification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    FintechResponse<SMSSendResultVO> sendSMSVerification(@RequestBody @Validated SMSSendVerificationParamVO smsSendVerificationParamVO);

    /**
     * 校验短信验证码
     *
     * @param smsCheckVerificationParamVO 短信验证参数
     * @return
     */
    @RequestMapping(path = "/check-sms-verification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    FintechResponse<Boolean> checkSMSVerification(SMSCheckVerificationParamVO smsCheckVerificationParamVO);

}
