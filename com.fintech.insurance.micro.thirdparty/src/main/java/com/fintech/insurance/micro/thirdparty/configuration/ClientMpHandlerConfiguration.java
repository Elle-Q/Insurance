package com.fintech.insurance.micro.thirdparty.configuration;

import com.fintech.insurance.micro.thirdparty.service.weixin.handler.WxMessageLogHandler;
import com.fintech.insurance.micro.thirdparty.service.weixin.handler.client.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientMpHandlerConfiguration {

    @Autowired
    private WxMessageLogHandler logHandler;

    @Autowired
    private ClientMpNullHandler nullHandler;

    @Autowired
    private ClientMpKfSessionHandler kfSessionHandler;

    @Autowired
    private ClientMpLocationHandler locationHandler;

    @Autowired
    private ClientMpMenuHandler menuHandler;

    @Autowired
    private ClientMpMsgHandler msgHandler;

    @Autowired
    private ClientMpSubscribeHandler subscribeHandler;

    @Autowired
    private ClientMpUnsubscribeHandler unsubscribeHandler;

    @Autowired
    private ClientMpScanHandler scanHandler;

    @Bean("clientMpRouter")
    public WxMpMessageRouter router(@Qualifier("clientWxMpService") WxMpService wxMpService) {
        final WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
        //记录所有事件的日志（异步执行）
        router.rule().handler(this.logHandler).next();

        //接收客服会话管理事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxMpEventConstants.CustomerService.KF_CREATE_SESSION).handler(this.kfSessionHandler).end();
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxMpEventConstants.CustomerService.KF_CLOSE_SESSION).handler(this.kfSessionHandler).end();
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxMpEventConstants.CustomerService.KF_SWITCH_SESSION).handler(this.kfSessionHandler).end();

        // 门店审核事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxMpEventConstants.POI_CHECK_NOTIFY).handler(this.nullHandler).end();

        //自定义菜单事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.MenuButtonType.CLICK).handler(this.menuHandler).end();

        //点击菜单链接事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.MenuButtonType.VIEW).handler(this.nullHandler).end();

        // 关注事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(this.subscribeHandler).end();

        // 取消关注事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.UNSUBSCRIBE).handler(this.unsubscribeHandler).end();

        // 上报地理位置事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.LOCATION).handler(this.locationHandler).end();

        // 接收地理位置消息
        router.rule().async(false).msgType(WxConsts.XmlMsgType.LOCATION).handler(this.locationHandler).end();

        //扫码事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SCAN).handler(this.scanHandler).end();

        //默认
        router.rule().async(false).handler(this.msgHandler).end();

        return router;
    }
}
