package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.micro.biz.service.SendMailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description: (some words)
 * @Author: Administrator
 * @Date: 2017/11/16 0016 16:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class SendMailControllerTest {
    @Autowired
    SendMailService sendMailService;
    @Test
    void sendTextMail() {
        sendMailService.sendTextMail("1286961802@qq.com","liu.yongneng@zte.com.cn","你好","下班聚餐");
    }

    @Test
    void sendAttachmentsMail() {
    }

    @Test
    void sendInlineMail() {
    }

    @Test
    void sendTemplateMail() {
    }

}