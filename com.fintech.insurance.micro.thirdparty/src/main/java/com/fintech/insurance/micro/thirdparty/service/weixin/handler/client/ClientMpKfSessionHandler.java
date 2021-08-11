package com.fintech.insurance.micro.thirdparty.service.weixin.handler.client;

import com.fintech.insurance.micro.thirdparty.service.weixin.handler.IKfSessionHandler;
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
 * 渠道端微信客服会话处理器
 */
@Component
public class ClientMpKfSessionHandler extends AbstractClientMpMessageHandler implements IKfSessionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClientMpKfSessionHandler.class);

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        return null;
    }
}
