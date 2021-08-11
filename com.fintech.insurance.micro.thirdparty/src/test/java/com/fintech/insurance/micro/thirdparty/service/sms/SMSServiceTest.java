package com.fintech.insurance.micro.thirdparty.service.sms;

import com.fintech.insurance.commons.enums.NotificationEvent;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSQueryDetailsRequestVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSQueryDetailsResponseVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendRequestVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendResponseVO;
import com.fintech.insurance.micro.thirdparty.ServiceTestApplication;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/21 0021 20:16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ServiceTestApplication.class})
public class SMSServiceTest {

    @Autowired
    private SMSService smsService;

    @Before // 在测试开始前初始化工作
    public void setup() {

    }

    @Test
    public void testSMSService() {

        SMSSendRequestVO sendRequestVO = new SMSSendRequestVO();
        sendRequestVO.setPhoneNumbers(new String[]{"15120611011", "15120622022"});
        sendRequestVO.setEvent(NotificationEvent.WX_CHANNEL_LOGIN_AUTH);
        Map<String, String> templateParam = new HashMap<String, String>();
        templateParam.put("code", "123456");
        sendRequestVO.setTemplateParam(templateParam);
        SMSSendResponseVO sendResponseVO = this.smsService.sendSMS(sendRequestVO);

        Assert.assertEquals(sendResponseVO.getCode(), SMSSendResponseVO.SUCCESS_CODE);
        Assert.assertNotNull(sendResponseVO.getSequenceId());
        Assert.assertNotNull(sendResponseVO.getBizId());

        SMSQueryDetailsRequestVO queryDetailsRequestVO = new SMSQueryDetailsRequestVO();
        queryDetailsRequestVO.setPhoneNumber("15120611011");
        queryDetailsRequestVO.setBizId(sendResponseVO.getBizId());
        queryDetailsRequestVO.setSendDate(DateTime.now().toDate());
        queryDetailsRequestVO.setPageSize(10);
        queryDetailsRequestVO.setCurrentPage(1);
        SMSQueryDetailsResponseVO queryDetailsResponseVO = this.smsService.querySMS(queryDetailsRequestVO);

        Assert.assertEquals(queryDetailsResponseVO.getCode(), SMSQueryDetailsResponseVO.SUCCESS_CODE);
    }
}
