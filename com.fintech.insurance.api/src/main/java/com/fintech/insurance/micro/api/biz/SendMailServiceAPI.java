package com.fintech.insurance.micro.api.biz;

import com.fintech.insurance.commons.web.FintechResponse;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Description: 发送邮件服务接口
 * @Author: East
 * @Date: 2017/11/13 0013 9:58
 */
@RequestMapping(path = "/biz/mail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface SendMailServiceAPI {

    /**
     * 发送文本邮件
     * @param fromUser
     * @param toUser
     * @param subject
     * @param content
     * @return
     */
    @RequestMapping(path = "/send-text", method = RequestMethod.POST)
    FintechResponse<String> sendTextMail(@NotBlank String fromUser, @NotBlank String toUser, @NotBlank String subject , @NotBlank String content);

    /**
     * 发送自定义附件邮件
     * @param fromUser
     * @param toUser
     * @param subject
     * @param content
     * @return
     */
    @RequestMapping(path = "/send-attachments", method = RequestMethod.POST)
    FintechResponse<String> sendAttachmentsMail(@NotBlank String fromUser, @NotBlank String toUser, @NotBlank String subject , @NotBlank String content);

    /**
     * 嵌入静态资源邮件
     * @param fromUser
     * @param toUser
     * @param subject
     * @param content
     * @return
     */
    @RequestMapping(path = "/send-inline", method = RequestMethod.POST)
    FintechResponse<String> sendInlineMail(@NotBlank String fromUser, @NotBlank String toUser, @NotBlank String subject , @NotBlank String content);

    /**
     * 模板邮件邮件
     * @param fromUser
     * @param toUser
     * @param subject
     * @param content
     * @return
     */
    @RequestMapping(path = "/send-template", method = RequestMethod.POST)
    FintechResponse<String> sendTemplateMail(@NotBlank String fromUser, @NotBlank String toUser, @NotBlank String subject , @NotBlank String content);

}
