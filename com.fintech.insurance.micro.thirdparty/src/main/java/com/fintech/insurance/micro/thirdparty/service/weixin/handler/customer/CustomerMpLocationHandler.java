package com.fintech.insurance.micro.thirdparty.service.weixin.handler.customer;

import com.fintech.insurance.micro.thirdparty.service.weixin.handler.ILocationHandler;
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
 * 客户端微信号地址位置上报事件
 */
@Component
public class CustomerMpLocationHandler extends AbstractCustomerMpMessageHandler implements ILocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomerMpLocationHandler.class);

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        return null;
    }
}
