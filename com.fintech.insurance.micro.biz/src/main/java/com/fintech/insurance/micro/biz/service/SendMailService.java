package com.fintech.insurance.micro.biz.service;

/**
 * @Description: 发送邮件服务接口
 * @Author: East
 * @Date: 2017/11/13 0013 9:58
 */
public interface SendMailService {
    /**
     * 发送文本邮件
     *
     * @param fromUser
     * @param toUser
     * @param subject
     * @param content
     * @return
     */
    String sendTextMail(String fromUser, String toUser, String subject, String content);

    /**
     * 发送自定义附件邮件
     *
     * @param fromUser
     * @param toUser
     * @param subject
     * @param content
     * @return
     */
    String sendAttachmentsMail(String fromUser, String toUser, String subject, String content);

    /**
     * 嵌入静态资源邮件
     *
     * @param fromUser
     * @param toUser
     * @param subject
     * @param content
     * @return
     */
    String sendInlineMail(String fromUser, String toUser, String subject, String content);

    /**
     * 模板邮件邮件
     *
     * @param fromUser
     * @param toUser
     * @param subject
     * @param content
     * @return
     */
    String sendTemplateMail(String fromUser, String toUser, String subject, String content);

    /**
     * 模板邮件邮件
     *
     * @param fromUser
     * @param toUser
     * @param subject
     * @param content
     * @return
     */
     void sendFileMail(String fromUser, String toUser, String subject, String content, String fileUrl, String fileName);
}