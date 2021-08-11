package com.fintech.insurance.micro.thirdparty.controller.sms;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.commons.enums.NotificationEvent;
import com.fintech.insurance.micro.dto.thirdparty.sms.*;
import com.fintech.insurance.micro.thirdparty.service.sms.SMSCacheService;
import com.fintech.insurance.micro.thirdparty.service.sms.SMSService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/21 0021 11:27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(SMSController.class)
public class SMSControllerTest {

    @Autowired
    private SMSController smsController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc; // 模拟MVC对象

    @MockBean
    private SMSService smsService;

    @MockBean
    private SMSCacheService smsCacheService;

    @Before // 在测试开始前初始化工作
    public void setup() {
        // mock smsService
        SMSSendResponseVO smsSendResponseVO = new SMSSendResponseVO();
        smsSendResponseVO.setBizId("B000001");
        smsSendResponseVO.setSequenceId("Seq000001");
        smsSendResponseVO.setCode(SMSSendResponseVO.SUCCESS_CODE);

        given(this.smsService.sendSMS(any(SMSSendRequestVO.class))).willReturn(smsSendResponseVO);

        SMSQueryDetailsResponseVO smsQueryDetailsResponseVO = new SMSQueryDetailsResponseVO();
        smsQueryDetailsResponseVO.setCode(SMSQueryDetailsResponseVO.SUCCESS_CODE);
        smsQueryDetailsResponseVO.setPhoneNumber("15120611011");
        smsQueryDetailsResponseVO.setBizId("B000001");
        smsQueryDetailsResponseVO.setCurrentPage(1L);
        smsQueryDetailsResponseVO.setPageSize(1L);
        smsQueryDetailsResponseVO.setTotalCount(1L);
        smsQueryDetailsResponseVO.setTotalPage(1L);

        given(this.smsService.querySMS(any(SMSQueryDetailsRequestVO.class))).willReturn(smsQueryDetailsResponseVO);

        doNothing().when(this.smsCacheService).setCache(any(SMSCacheVO.class), eq(60), eq(TimeUnit.SECONDS));

        SMSCacheVO smsCacheVO = mock(SMSCacheVO.class);
        when(smsCacheVO.checkVerification("Seq000001", "15120611011", NotificationEvent.DEFAULT_VERIFICATION.getCode(), "123456")).thenReturn(true);
        given(this.smsCacheService.getCache(anyString())).willReturn(smsCacheVO);
    }

    @Test
    public void testSendSMS() throws Exception {
        String[] phoneNumbers = new String[]{"15120611011", "15120622022"};
        Map<String, String> smsParams = new HashMap<String, String>();
        smsParams.put("code", "123456");
        SMSSendParamVO sendParamVO = new SMSSendParamVO(phoneNumbers, NotificationEvent.AUDIT_NOTIFICATION_FOR_CHANNEL, smsParams);
        RequestBuilder request = MockMvcRequestBuilders.post("/thirdparty/sms/send-sms").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(JSON.toJSONString(sendParamVO));
        MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.code").value("0")).andReturn();
    }

    @Test
    public void testQuerySMS() throws Exception {
        SMSQueryParamVO queryParamVO = new SMSQueryParamVO("15120611011", "B000001", DateTime.now().toDate(), 10L, 1L);
        RequestBuilder request = MockMvcRequestBuilders.post("/thirdparty/sms/query-sms").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(JSON.toJSONString(queryParamVO));
        MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.code").value("0")).andReturn();
    }

    @Test
    public void testSendSMSVerification() throws Exception {
        SMSSendVerificationParamVO smsSendVerificationParamVO = new SMSSendVerificationParamVO();
        smsSendVerificationParamVO.setPhoneNumber("15120611011");
        smsSendVerificationParamVO.setEventCode(NotificationEvent.WX_CUSTOMER_LOGIN_AUTH.getCode());
        RequestBuilder request = MockMvcRequestBuilders.post("/thirdparty/sms/send-sms-verification").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(JSON.toJSONString(smsSendVerificationParamVO));
        MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.code").value("0")).andExpect(jsonPath("$.data.sequenceId").isNotEmpty()).andReturn();
    }

    @Test
    public void testCheckSMSVerification() throws Exception {
        SMSCheckVerificationParamVO smsCheckVerificationParamVO = new SMSCheckVerificationParamVO();
        smsCheckVerificationParamVO.setPhoneNumber("15120611011");
        smsCheckVerificationParamVO.setEventCode(NotificationEvent.WX_CHANNEL_LOGIN_AUTH.getCode());
        smsCheckVerificationParamVO.setSequenceId("Seq000001");
        smsCheckVerificationParamVO.setVerification("123456");
        RequestBuilder request = MockMvcRequestBuilders.post("/thirdparty/sms/check-sms-verification").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(JSON.toJSONString(smsCheckVerificationParamVO));
        MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.code").value("0")).andReturn();
    }
}
