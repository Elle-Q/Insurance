package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.components.exception.FInsuranceBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 邮件服务
 * @Author: East
 * @Date: 2017/11/13 0013 16:55
 */
@Service
public class SendMailServiceImpl implements SendMailService {

    private static final Logger LOG = LoggerFactory.getLogger(SendMailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String sendTextMail(String fromUser, String toUser, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromUser);
        message.setTo(toUser);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
        return null;
    }

    @Override
    public String sendAttachmentsMail(String fromUser, String toUser, String subject, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,"UTF-8");
            helper.setFrom(fromUser);
            helper.setTo(toUser);
            helper.setSubject("主题：有附件");
            helper.setText("有附件的邮件");
            FileSystemResource file = new FileSystemResource(new File("C:/Users/Administrator/Downloads/excel-1510992215571.xlsx"));
            helper.addAttachment("excel-1510992215571.xlsx", file);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String sendInlineMail(String fromUser, String toUser, String subject, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromUser);
            helper.setTo(toUser);
            helper.setSubject("主题：嵌入静态资源");
            helper.setText("<html><body><img src=\"cid:weixin\" ></body></html>", true);
            FileSystemResource file = new FileSystemResource(new File("weixin.jpg"));
            helper.addInline("weixin", file);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String sendTemplateMail(String fromUser, String toUser, String subject, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromUser);
            helper.setTo(toUser);
            helper.setSubject(subject);
            Map<String, Object> model = new HashMap();
            model.put("username", "didi");
            String text = null; //VelocityEngineUtils.mergeTemplateIntoString(
            //velocityEngine, "template.vm", "UTF-8", model);
            helper.setText(text, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void sendFileMail(String fromUser, String toUser, String subject, String content, String fileUrl, String fileName) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,"gb2312");
            helper.setFrom(fromUser);
            helper.setTo(toUser);
            helper.setSubject(subject);
            helper.setText(content);
            File file = new File(fileUrl);
            //FileDataSource fileSystemResource = new FileDataSource(file);
            //helper.addAttachment(fileName, fileSystemResource);
            //Transport.send(mimeMessage);
            BodyPart fileBodyPart = new MimeBodyPart();
            mimeMessage.setSubject(subject);
            mimeMessage.setFrom(fromUser);
            MimeMultipart multi = new MimeMultipart();
            BodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(subject);
            multi.addBodyPart(textBodyPart);
            FileDataSource fds = new FileDataSource(file);
            fileBodyPart.setDataHandler(new DataHandler(fds));
            fileBodyPart.setFileName(MimeUtility.encodeText( fileName,"gb2312","b"));//如果附件有中文通过转换没有问题了
            multi.addBodyPart(fileBodyPart);
            mimeMessage.setContent(multi);
            mimeMessage.setSentDate(new Date());
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            LOG.error("sendFileMail failed with fromUser=" + fromUser + ",toUser=" + toUser + ",content=" + content + ",fileUrl=" + fileUrl + ",fileName=" + fileName,e);
            throw new FInsuranceBaseException(104929);
        }
    }
}
