package com.fintech.insurance.micro.thirdparty.controller.sms;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.enums.NotificationEvent;
import com.fintech.insurance.commons.enums.SystemProfile;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.api.thirdparty.sms.SMSServiceAPI;
import com.fintech.insurance.micro.dto.thirdparty.sms.*;
import com.fintech.insurance.micro.thirdparty.service.sms.SMSCacheService;
import com.fintech.insurance.micro.thirdparty.service.sms.SMSService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 短信业务服务
 * @Author: East
 * @Date: 2017/11/13 0013 14:32
 */
@RestController
public class SMSController extends BaseFintechController implements SMSServiceAPI {
    @Autowired
    private ConfigurableEnvironment environment;

    private static final Logger LOG = LoggerFactory.getLogger(SMSController.class);

    /**
     * 短信验证码参数
     */
    private static final String SMS_VERIFICATION_CODE_PARAM = "code";
    /**
     * 短信验证码默认长度
     */
    private static final int SMS_VERIFICATION_CODE_LENGTH = 6;
    /**
     * 短信验证码超时时间，单位：秒
     */
    private static final int SMS_VERIFICATION_CODE_TIMEOUT = 7 * 60;
    /**
     * 再次请求的时间与验证码缓存过期时间之间最少时间差
     */
    private static final int SMS_VERIFICATOAN_AGAIN_TIME = 2 * 60;
    @Autowired
    private SMSService smsService;

    @Autowired
    private SMSCacheService smsCacheService;

    @Override
    public FintechResponse<SMSSendResultVO> sendSMS(@RequestBody @Validated SMSSendParamVO smsSendParamVO) {
        //发送短信
        SMSSendRequestVO requestVO = new SMSSendRequestVO(smsSendParamVO.getPhoneNumbers(), smsSendParamVO.getEvent(), smsSendParamVO.getSmsParams(), null);
        SMSSendResponseVO responseVO = this.smsService.sendSMS(requestVO);
        //处理结果
        if (responseVO != null) {
            if (SMSSendResponseVO.SUCCESS_CODE.equals(responseVO.getCode())) {
                return FintechResponse.responseData(new SMSSendResultVO(responseVO.getSequenceId(), responseVO.getBizId(), null));
            } else {
                LOG.error("Send SMS failed with [{}], message [{}]!", JSON.toJSONString(smsSendParamVO), responseVO.getMessage());
                throw new FInsuranceBaseException(106133, new Object[] {responseVO.getMessage()});
            }
        } else {
            LOG.error("Send SMS failed with [{}], message [Null response]!", JSON.toJSONString(smsSendParamVO));
            throw new FInsuranceBaseException(106131);
        }
    }
    @Override
    public FintechResponse<Pagination<SMSSendDetailVO>> querySMS(@RequestBody @Validated SMSQueryParamVO smsQueryParamVO) {
        //查询短信
        SMSQueryDetailsRequestVO requestVO = new SMSQueryDetailsRequestVO(smsQueryParamVO.getPhoneNumber(), smsQueryParamVO.getBizId(), smsQueryParamVO.getSendDate(), smsQueryParamVO.getPageSize(), smsQueryParamVO.getCurrentPage());
        SMSQueryDetailsResponseVO responseVO = this.smsService.querySMS(requestVO);

        if (responseVO != null) {
            if (SMSQueryDetailsResponseVO.SUCCESS_CODE.equals(responseVO.getCode())) {
                return FintechResponse.responseData(Pagination.createInstance(responseVO.getCurrentPage(), responseVO.getPageSize(), responseVO.getTotalCount(), responseVO.getSendDetailVOList()));
            } else {
                LOG.error("Query SMS failed with [{}], message with [{}]!", JSON.toJSONString(smsQueryParamVO), responseVO.getMessage());
                throw new FInsuranceBaseException(106130,new Object[] {responseVO.getMessage()});
            }
        } else {
            LOG.error("Query SMS failed with [{}], message with [Null response]!", JSON.toJSONString(smsQueryParamVO));
            throw new FInsuranceBaseException(106129);
        }
    }

    @Override
    public FintechResponse<SMSSendResultVO> sendSMSVerification(@RequestBody @Validated SMSSendVerificationParamVO smsSendVerificationParamVO) {

        if (smsSendVerificationParamVO == null) {
            LOG.error("Null sendSMSVerificationParamVO!");
            throw new FInsuranceBaseException(106102);
        }
        //根据手机号码和短信模板组合成sequenceId,判断是否为再次请求发送短信验证码
        String cacheVerification = smsSendVerificationParamVO.getPhoneNumber() + smsSendVerificationParamVO.getEventCode();
        //如果是非生产环境,就不发送验证码
        if (SystemProfile.PROD != FInsuranceApplicationContext.getSystemProfile()) {
            SMSSendResultVO smsSendResultVO = new SMSSendResultVO();
            smsSendResultVO.setSequenceId(cacheVerification);
            smsSendResultVO.setBizId("MOCKED");
            return FintechResponse.responseData(smsSendResultVO);
        }
        //短信事件,默认为默认短信模板
        NotificationEvent event = NotificationEvent.DEFAULT_VERIFICATION;
        if (!StringUtils.isBlank(smsSendVerificationParamVO.getEventCode()) && NotificationEvent.codeOf(smsSendVerificationParamVO.getEventCode()) != null) {
            event = NotificationEvent.codeOf(smsSendVerificationParamVO.getEventCode());
        }
        //生成短信验证码
        String verification = RandomStringUtils.randomNumeric(SMS_VERIFICATION_CODE_LENGTH);
        //获取短信验证缓存数据
        SMSCacheVO oldSmsCacheVO = this.smsCacheService.getCache(cacheVerification);
        //是否需要新建缓存
        boolean createNewCache = true;
        //短信验证缓存数据不为null，说明不是首次请求且缓存未过期
        if (oldSmsCacheVO!=null) {
            //缓存过期时间应比此刻多于两分钟
            if (DateCommonUtils.getSeconds(oldSmsCacheVO.getExpireTime(),new Date()) > SMS_VERIFICATOAN_AGAIN_TIME) {
                verification = oldSmsCacheVO.getVerification();
                //不用新建缓存
                createNewCache = false;
            }
        }
        //处理短信模板参数
        Map<String, String> templateParam = new HashMap<String, String>();
        templateParam.put(SMS_VERIFICATION_CODE_PARAM, verification);
        //发送短信
        SMSSendParamVO smsSendParamVO = new SMSSendParamVO(new String[]{smsSendVerificationParamVO.getPhoneNumber()}, event, templateParam);
        FintechResponse<SMSSendResultVO> response = this.sendSMS(smsSendParamVO);
        if (response.isOk()) {
            if (createNewCache) {
                Date expireTime = DateCommonUtils.nowPlusSeconds(SMS_VERIFICATION_CODE_TIMEOUT);
                SMSSendResultVO resultVO = response.getData();
                resultVO.setExpireTime(expireTime);
                //处理短信验证码缓存
                SMSCacheVO smsCacheVO = new SMSCacheVO(resultVO.getSequenceId(), smsSendVerificationParamVO.getPhoneNumber(), event.getCode(), verification, expireTime);
                this.smsCacheService.setCache(smsCacheVO, SMS_VERIFICATION_CODE_TIMEOUT, TimeUnit.SECONDS);
            }
        }else {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }
    @Override
    public FintechResponse<Boolean> checkSMSVerification(@RequestBody @Validated SMSCheckVerificationParamVO smsCheckVerificationParamVO) {
        //如果是非生产环境，用"111111"做为短信验证码，就不做校验
        if (SystemProfile.PROD != FInsuranceApplicationContext.getSystemProfile()) {
            if(BasicConstants.DEFAULT_TEST_SMS_VERIFICATION.equals(smsCheckVerificationParamVO.getVerification())) {
                return FintechResponse.responseData(true);
            }else {
                throw new FInsuranceBaseException(106128);
            }
        }
        //获取短信验证缓存数据
        SMSCacheVO smsCacheVO = this.smsCacheService.getCache(smsCheckVerificationParamVO.getSequenceId());
        //缓存数据不存在
        if (smsCacheVO == null) {
            LOG.error("Verification code timeout with sequenceId [{}]", smsCheckVerificationParamVO.getSequenceId());
            throw new FInsuranceBaseException(106127);
        }
        //验证短信验证码
        if (smsCacheVO.checkVerification(smsCheckVerificationParamVO.getSequenceId(), smsCheckVerificationParamVO.getPhoneNumber(), smsCheckVerificationParamVO.getEventCode(), smsCheckVerificationParamVO.getVerification())) {
            //缓存数据存在，且验证通过
            return FintechResponse.responseData(true);
        } else {
            //缓存数据已过期或者验证失败
            LOG.error("Check SMS verification failed with sequenceId [{}] and verificationCode [{}], cache data [{}]!", smsCheckVerificationParamVO.getSequenceId(), smsCheckVerificationParamVO.getVerification(), smsCacheVO);
            throw new FInsuranceBaseException(106128);
        }
    }
}
