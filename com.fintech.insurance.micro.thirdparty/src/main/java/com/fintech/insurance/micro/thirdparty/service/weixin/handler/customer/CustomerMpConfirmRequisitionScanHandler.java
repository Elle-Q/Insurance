package com.fintech.insurance.micro.thirdparty.service.weixin.handler.customer;

import com.fintech.insurance.commons.constants.WechatConstants;
import com.fintech.insurance.micro.thirdparty.service.weixin.handler.IScanHandler;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 客户端微信扫码确认申请单事件处理器
 */
@Component
@Configuration
public class CustomerMpConfirmRequisitionScanHandler extends AbstractCustomerMpMessageHandler implements IScanHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomerMpConfirmRequisitionScanHandler.class);

    @Value("${weixin.kefu-messages.customer-confirm-requisition-tips}")
    private String confirmRequisitionTips;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        logger.info("start to handle the incoming wx mp message " + wxMpXmlMessage.getMsgId());
        //判断当前是否能处理此消息
        if (!this.canHandle(wxMpXmlMessage)) {
            return null;
        }
        if (StringUtils.isEmpty(this.confirmRequisitionTips)) {
            logger.info("The out put confirm requisition tips is empty");
            return null;
        } else {
            logger.info("handler is ready for handling the incoming wx mp message " + wxMpXmlMessage.getMsgId());
            //获得申请单id
            Integer requisitionId = null;
            if (wxMpXmlMessage.getEventKey().startsWith(WechatConstants.QR_TMP_CONFIRM_REQUISITION_PREFIX)) {
                requisitionId = Integer.valueOf(wxMpXmlMessage.getEventKey().replace(WechatConstants.QR_TMP_CONFIRM_REQUISITION_PREFIX, ""));
            } else {
                requisitionId = Integer.valueOf(wxMpXmlMessage.getEventKey().replace(WechatConstants.QR_TMP_CONFIRM_REQUISITION_PREFIX_SUBSCRIBE, ""));
            }

            //用户的openid
            String openid = wxMpXmlMessage.getFromUser();
            //准备业务数据
            String url = this.customerClientWeixinConfig.getRequisitionDetailUrl() + String.valueOf(requisitionId);
            String messageText = String.format(this.confirmRequisitionTips, url);
            return WxMpXmlOutMessage.TEXT().content(messageText).toUser(openid).fromUser(wxMpXmlMessage.getToUser()).build();
        }
    }

    @Override
    public boolean canHandle(WxMpXmlMessage message) {
        if (message == null) {
            logger.info("current incoming message is null");
            return false;
        } else {
            if (WxConsts.XmlMsgType.EVENT.equalsIgnoreCase(message.getMsgType())) {
                logger.info("current incoming message is with event key " + message.getEventKey());
                //判断事件的类型是否match
                if (StringUtils.isNotEmpty(message.getEventKey()) && (Pattern.matches(WechatConstants.QR_TMP_CONFIRM_REQUISITION_PATTERN, message.getEventKey()) || Pattern.matches(WechatConstants.QR_TMP_CONFIRM_REQUISITION_SUBSCRIBE_PATTERN, message.getEventKey()))) {
                    return true;
                } else {
                    logger.info("current incoming message is with event " + message.getEvent());
                    return false;
                }
            } else {
                logger.info("current incoming message is with type " + message.getMsgType() + ", but current handler only support " + WxConsts.XmlMsgType.EVENT);
                return false;
            }
        }
    }
}
