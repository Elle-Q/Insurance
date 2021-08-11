package com.fintech.insurance.micro.thirdparty.service.weixin.handler.customer;

import com.fintech.insurance.commons.constants.WechatConstants;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 客户端微信关注事件处理器
 */
@Component
public class CustomerMpSubscribeHandler extends AbstractCustomerMpMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomerMpSubscribeHandler.class);

    @Autowired
    private CustomerMpConfirmRequisitionScanHandler customerMpConfirmRequisitionScanHandler;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        String eventKey = wxMpXmlMessage.getEventKey();
        if (Pattern.matches(WechatConstants.QR_TMP_CONFIRM_REQUISITION_SUBSCRIBE_PATTERN, eventKey)) {
            return this.customerMpConfirmRequisitionScanHandler.handle(wxMpXmlMessage, map, wxMpService, wxSessionManager);
        } else {
            return null;
        }
    }

    @Override
    public boolean canHandle(WxMpXmlMessage message) {
        if (message == null) {
            return false;
        } else {
            return WxConsts.EventType.SUBSCRIBE.equals(message.getEvent());
        }
    }
}
