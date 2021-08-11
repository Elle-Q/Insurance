package com.fintech.insurance.micro.thirdparty.controller.wexin;

import com.fintech.insurance.commons.beans.WeixinConfigBean;
import com.fintech.insurance.commons.beans.WeixinTemplateMessagesConfgBean;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.thirdparty.service.WechatService;
import com.fintech.insurance.micro.thirdparty.service.weixin.provider.UserProvider;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信相关业务控制器的基类
 */
public abstract class AbstractWeixinController implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AbstractWeixinController.class);

    public static final String WXMP_API_LANG = "zh_CN";
    public static final int WXMP_UNSUPPORTED = 106202;

    private Map<String, WxMpService> wxMpServiceMap = new HashMap<>();

    private Map<String, WxMpConfigStorage> wxMpConfigStorageMap = new HashMap<>();

    private Map<String, WeixinConfigBean> weixinConfigBeanMap = new HashMap<>();

    private Map<String, UserProvider> weixinUserProviderMap = new HashMap<>();

    private Map<String, WxMpMessageRouter> wxMpMessageRouterMap = new HashMap<>();

    private Map<String, WeixinTemplateMessagesConfgBean> templateMessagesConfgBeanMap = new HashMap<>();

    @Autowired
    @Qualifier("customerClientWeixinConfig")
    protected WeixinConfigBean customerClientWeixinConfig;

    @Autowired
    @Qualifier("channelClientWeixinConfig")
    protected WeixinConfigBean channelClientWeixinConfig;

    @Autowired
    @Qualifier("customerMpConfigStorage")
    protected WxMpConfigStorage customerMpConfigStorage;

    @Autowired
    @Qualifier("clientMpConfigStorage")
    protected WxMpConfigStorage clientMpConfigStorage;

    @Autowired
    @Qualifier("customerWxMpService")
    protected WxMpService customerWxMpService;

    @Autowired
    @Qualifier("clientWxMpService")
    protected WxMpService clientWxMpService;

    @Autowired
    @Qualifier("customerUserProvider")
    protected UserProvider customerUserProvider;

    @Autowired
    @Qualifier("systemUserProvider")
    protected UserProvider sysmtemUserProvider;

    @Autowired
    protected WxMpMessageRouter clientMpRouter;

    @Autowired
    protected WxMpMessageRouter customerMpRouter;

    @Autowired
    private Environment environment;

    @Autowired
    protected WechatService wechatService;

    @Autowired
    protected WeixinTemplateMessagesConfgBean customerTemplateMessages;

    @Autowired
    protected WeixinTemplateMessagesConfgBean clientTemplateMessages;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.wxMpServiceMap.put(this.customerClientWeixinConfig.getAppid(), this.customerWxMpService);
        this.wxMpServiceMap.put(this.channelClientWeixinConfig.getAppid(), this.clientWxMpService);

        this.wxMpConfigStorageMap.put(this.customerClientWeixinConfig.getAppid(), this.customerMpConfigStorage);
        this.wxMpConfigStorageMap.put(this.channelClientWeixinConfig.getAppid(), this.clientMpConfigStorage);

        this.weixinConfigBeanMap.put(this.customerClientWeixinConfig.getAppid(), this.customerClientWeixinConfig);
        this.weixinConfigBeanMap.put(this.channelClientWeixinConfig.getAppid(), this.channelClientWeixinConfig);

        this.weixinUserProviderMap.put(this.customerClientWeixinConfig.getAppid(), this.customerUserProvider);
        this.weixinUserProviderMap.put(this.channelClientWeixinConfig.getAppid(), this.sysmtemUserProvider);

        this.wxMpMessageRouterMap.put(this.customerClientWeixinConfig.getAppid(), this.customerMpRouter);
        this.wxMpMessageRouterMap.put(this.channelClientWeixinConfig.getAppid(), this.clientMpRouter);

        this.templateMessagesConfgBeanMap.put(this.customerClientWeixinConfig.getAppid(), this.customerTemplateMessages);
        this.templateMessagesConfgBeanMap.put(this.channelClientWeixinConfig.getAppid(), this.clientTemplateMessages);
    }

    /**
     * 获得指定编码的错误消息
     * @param errorCode
     * @param params
     * @return
     */
    protected String getExceptionCodeMessage(int errorCode, Object... params) {
        return String.format(this.environment.getProperty(String.valueOf(errorCode)), params);
    }

    /**
     * 封装公众号不被支持的异常
     * @param appid
     * @return
     */
    protected FInsuranceBaseException packWxMpUnsupportedException(String appid) {
        return this.packWxErrorException(AbstractWeixinController.WXMP_UNSUPPORTED, appid);
    }

    /**
     * 将微信异常包装成base exception
     * @param errorCode
     * @return
     */
    protected FInsuranceBaseException packWxErrorException(int errorCode, Object... params) {
        return new FInsuranceBaseException(errorCode, this.getExceptionCodeMessage(errorCode, params));
    }

    /**
     * 根据appid获取服务号service
     * @param appid
     * @return
     */
    protected WxMpService getWxMpService(String appid) {
        if (StringUtils.isEmpty(appid) || this.wxMpServiceMap == null) {
            return null;
        } else {
            return this.wxMpServiceMap.get(appid);
        }
    }

    /**
     * 根据appid获取服务号的配置
     * @param appid
     * @return
     */
    protected WxMpConfigStorage getWxMpConfigStorage(String appid) {
        if (StringUtils.isEmpty(appid) || this.wxMpConfigStorageMap == null) {
            return null;
        } else {
            return this.wxMpConfigStorageMap.get(appid);
        }
    }

    protected WeixinConfigBean getWeixinConfigBean(String appid) {
        if (StringUtils.isEmpty(appid) || this.weixinConfigBeanMap == null) {
            return null;
        } else {
            return this.weixinConfigBeanMap.get(appid);
        }
    }

    /**
     * 获得当前请求
     * @return http servlet request实例
     */
    protected HttpServletRequest getCurrentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } else {
            return null;
        }
    }

    /**
     * 获得指定公众号的用户服务提供者
     * @param appid
     * @return
     */
    protected UserProvider getUserProvider(String appid) {
        if (StringUtils.isEmpty(appid)) {
            return null;
        } else {
            return this.weixinUserProviderMap.get(appid);
        }
    }

    /**
     * 获取指定公众号的消息路由器
     * @param appid
     * @return
     */
    protected WxMpMessageRouter getWxMpMessageRouter(String appid) {
        if (StringUtils.isEmpty(appid)) {
            return null;
        } else {
            return this.wxMpMessageRouterMap.get(appid);
        }
    }

    /**
     * 获取模版消息配置
     * @param appid
     * @return
     */
    protected WeixinTemplateMessagesConfgBean getTemplateMessagesConfigBean(String appid) {
        if (StringUtils.isEmpty(appid)) {
            return null;
        } else {
            return this.templateMessagesConfgBeanMap.get(appid);
        }
    }
}
