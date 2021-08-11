package com.fintech.insurance.micro.thirdparty.service.weixin.handler;

import com.alibaba.fastjson.JSON;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 微信消息日志记录处理器
 */
@Component
public class WxMessageLogHandler implements WxMpMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(WxMessageLogHandler.class);

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        logger.info("\n接收到请求消息，内容：\n{}", JSON.toJSONString(wxMessage));
        return null;
    }
}
