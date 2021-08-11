package com.fintech.insurance.micro.thirdparty.service.weixin.handler.customer;

import com.fintech.insurance.micro.thirdparty.service.weixin.handler.INullHandler;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 其他消息处理器
 */
@Component
public class CustomerMpNullHandler extends AbstractCustomerMpMessageHandler implements INullHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomerMpNullHandler.class);

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        return null;
    }
}
