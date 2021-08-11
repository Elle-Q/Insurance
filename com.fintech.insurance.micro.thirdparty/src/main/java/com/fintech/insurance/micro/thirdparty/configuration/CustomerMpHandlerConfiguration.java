package com.fintech.insurance.micro.thirdparty.configuration;

import com.fintech.insurance.commons.constants.WechatConstants;
import com.fintech.insurance.micro.thirdparty.service.weixin.handler.WxMessageLogHandler;
import com.fintech.insurance.micro.thirdparty.service.weixin.handler.customer.*;
import me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerMpHandlerConfiguration {

    @Autowired
    private WxMessageLogHandler logHandler;

    @Autowired
    private CustomerMpNullHandler nullHandler;

    @Autowired
    private CustomerMpKfSessionHandler kfSessionHandler;

    @Autowired
    private CustomerMpLocationHandler locationHandler;

    @Autowired
    private CustomerMpMenuHandler menuHandler;

    @Autowired
    private CustomerMpMsgHandler msgHandler;

    @Autowired
    private CustomerMpSubscribeHandler subscribeHandler;

    @Autowired
    private CustomerMpUnsubscribeHandler unsubscribeHandler;

    @Autowired
    private CustomerMpConfirmRequisitionScanHandler confirmRequisitionScanHandler;

    @Bean("customerMpRouter")
    public WxMpMessageRouter router(@Qualifier("customerWxMpService") WxMpService wxMpService) {
        final WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
        //记录所有事件的日志（异步执行）
        router.rule().handler(this.logHandler).next();

        //接收客服会话管理事件
        router.rule().async(false).msgType(XmlMsgType.EVENT).event(WxMpEventConstants.CustomerService.KF_CREATE_SESSION).handler(this.kfSessionHandler).end();
        router.rule().async(false).msgType(XmlMsgType.EVENT).event(WxMpEventConstants.CustomerService.KF_CLOSE_SESSION).handler(this.kfSessionHandler).end();
        router.rule().async(false).msgType(XmlMsgType.EVENT).event(WxMpEventConstants.CustomerService.KF_SWITCH_SESSION).handler(this.kfSessionHandler).end();

        // 门店审核事件
        router.rule().async(false).msgType(XmlMsgType.EVENT).event(WxMpEventConstants.POI_CHECK_NOTIFY).handler(this.nullHandler).end();

        //自定义菜单事件
        router.rule().async(false).msgType(XmlMsgType.EVENT).event(WxConsts.MenuButtonType.CLICK).handler(this.menuHandler).end();

        //点击菜单链接事件
        router.rule().async(false).msgType(XmlMsgType.EVENT).event(WxConsts.MenuButtonType.VIEW).handler(this.nullHandler).end();

        // 关注事件
        router.rule().async(false).msgType(XmlMsgType.EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(this.subscribeHandler).end();

        // 取消关注事件
        router.rule().async(false).msgType(XmlMsgType.EVENT).event(WxConsts.EventType.UNSUBSCRIBE).handler(this.unsubscribeHandler).end();

        // 上报地理位置事件
        router.rule().async(true).msgType(XmlMsgType.EVENT).event(WxConsts.EventType.LOCATION).handler(this.locationHandler).end();

        // 接收地理位置消息
        router.rule().async(true).msgType(XmlMsgType.LOCATION).handler(this.locationHandler).end();

        //扫码事件
        router.rule().async(false).msgType(XmlMsgType.EVENT).event(WxConsts.EventType.SCAN).eventKeyRegex(WechatConstants.QR_TMP_CONFIRM_REQUISITION_PATTERN).handler(this.confirmRequisitionScanHandler).end();

        //默认
        router.rule().async(true).handler(this.msgHandler).end();

        return router;
    }
}
