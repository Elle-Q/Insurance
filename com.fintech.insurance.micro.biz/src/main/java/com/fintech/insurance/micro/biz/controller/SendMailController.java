package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.biz.SendMailServiceAPI;
import com.fintech.insurance.micro.biz.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 邮件业务服务
 * @Author: East
 * @Date: 2017/11/13 0013 14:32
 */

@RestController
public class SendMailController extends BaseFintechController implements SendMailServiceAPI {

    @Autowired
    private SendMailService sendMailService;

    @Override
    public FintechResponse<String> sendTextMail(String fromUser, String toUser, String subject, String content) {
        sendMailService.sendTextMail(fromUser, toUser, subject, content);
        return null;
    }

    @Override
    public FintechResponse<String> sendAttachmentsMail(String fromUser, String toUser, String subject, String content) {
        sendMailService.sendAttachmentsMail(fromUser, toUser, subject, content);
        return null;
    }

    @Override
    public FintechResponse<String> sendInlineMail(String fromUser, String toUser, String subject, String content) {
        sendMailService.sendInlineMail(fromUser, toUser, subject, content);
        return null;
    }

    @Override
    public FintechResponse<String> sendTemplateMail(String fromUser, String toUser, String subject, String content) {
        sendMailService.sendTemplateMail(fromUser, toUser, subject, content);
        return null;
    }
}
