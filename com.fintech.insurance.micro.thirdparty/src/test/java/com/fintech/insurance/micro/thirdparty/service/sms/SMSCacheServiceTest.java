package com.fintech.insurance.micro.thirdparty.service.sms;

import com.fintech.insurance.commons.enums.NotificationEvent;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSCacheVO;
import com.fintech.insurance.micro.thirdparty.ServiceTestApplication;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/22 0022 14:26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ServiceTestApplication.class})
public class SMSCacheServiceTest {

    @Autowired
    private SMSCacheService smsCacheService;

    @Before // 在测试开始前初始化工作
    public void setup() {

    }

    @Test
    public void testSMSCacheService() {
        String sequenceId = "Seq000001";
        String phoneNumber = "15120611011";
        String eventCode = NotificationEvent.DEFAULT_VERIFICATION.getCode();
        String verification = "123456";
        Date expireTime = DateTime.now().minusMillis(5).toDate();
        SMSCacheVO putCacheVO = new SMSCacheVO(sequenceId, phoneNumber, eventCode, verification, expireTime);
        this.smsCacheService.setCache(putCacheVO, 60, TimeUnit.SECONDS);

        SMSCacheVO getCacheVO = this.smsCacheService.getCache(sequenceId);
        Assert.assertTrue(getCacheVO.getVerification().equals(verification));
    }
}
