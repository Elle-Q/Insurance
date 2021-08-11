package com.fintech.insurance.micro.thirdparty.service.weixin.handler.customer;

import com.fintech.insurance.commons.beans.WeixinConfigBean;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCustomerMpMessageHandler implements WxMpMessageHandler {

    @Autowired
    protected WxMpService customerWxMpService;

    @Autowired
    protected WeixinConfigBean customerClientWeixinConfig;

    /**
     * 判断当前消息是否复合处理条件，防止router出现路由错误的问题
     * @param message
     * @return 默认返回true
     */
    public boolean canHandle(WxMpXmlMessage message) {
        if (message == null) {
            return false;
        } else {
            return true;
        }
    }
}
