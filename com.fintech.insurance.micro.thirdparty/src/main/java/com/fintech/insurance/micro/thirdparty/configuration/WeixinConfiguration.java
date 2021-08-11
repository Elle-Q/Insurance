package com.fintech.insurance.micro.thirdparty.configuration;

import com.fintech.insurance.commons.beans.WeixinConfigBean;
import com.fintech.insurance.commons.beans.WeixinTemplateMessagesConfgBean;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInRedisConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * 微信集成的配置类
 */
@Configuration
public class WeixinConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(WeixinConfiguration.class);

    @Autowired
    private JedisPool jedisPool;

    @Bean("customerClientWeixinConfig")
    @ConfigurationProperties(prefix = "weixin.customer-client")
    public WeixinConfigBean customerClientWeixinConfig() {
        return new WeixinConfigBean();
    }

    @Bean("channelClientWeixinConfig")
    @ConfigurationProperties(prefix = "weixin.channel-client")
    public WeixinConfigBean channelClientWeixinConfig() {
        return new WeixinConfigBean();
    }

    @Bean
    @ConfigurationProperties(prefix = "weixin.template-messages.customer")
    public WeixinTemplateMessagesConfgBean customerTemplateMessages() {
        return new WeixinTemplateMessagesConfgBean();
    }

    @Bean
    @ConfigurationProperties(prefix = "weixin.template-messages.channel")
    public WeixinTemplateMessagesConfgBean clientTemplateMessages() {
        return new WeixinTemplateMessagesConfgBean();
    }

    @Bean("customerMpConfigStorage")
    public WxMpConfigStorage customerMpConfigStorage() {
        WeixinConfigBean customerClientConfig = customerClientWeixinConfig();
        logger.debug("Customer weixin client configuration is : " + customerClientConfig.toString());
        WxMpInRedisConfigStorage customerRedisConfig = new WxMpInRedisConfigStorage(this.jedisPool);
        customerRedisConfig.setAppId(customerClientConfig.getAppid());
        customerRedisConfig.setSecret(customerClientConfig.getAppSecret());
        customerRedisConfig.setToken(customerClientConfig.getToken());
        customerRedisConfig.setAesKey(customerClientConfig.getEncodingAesKey());
        return customerRedisConfig;
    }

    @Bean("clientMpConfigStorage")
    public WxMpConfigStorage clientMpConfigStorage() {
        WeixinConfigBean channelClientConfig = channelClientWeixinConfig();
        logger.debug("Channel weixin client configuration is : " + channelClientConfig.toString());
        WxMpInRedisConfigStorage clientRedisConfig = new WxMpInRedisConfigStorage(this.jedisPool);
        clientRedisConfig.setAppId(channelClientConfig.getAppid());
        clientRedisConfig.setSecret(channelClientConfig.getAppSecret());
        clientRedisConfig.setToken(channelClientConfig.getToken());
        clientRedisConfig.setAesKey(channelClientConfig.getEncodingAesKey());
        return clientRedisConfig;
    }

    @Bean("customerWxMpService")
    public WxMpService customerWxMpService() {
        WxMpService customerWxMpService = new WxMpServiceImpl();
        customerWxMpService.setWxMpConfigStorage(customerMpConfigStorage());
        return customerWxMpService;
    }

    @Bean("clientWxMpService")
    public WxMpService clientWxMpService() {
        WxMpService clientWxMpService = new WxMpServiceImpl();
        clientWxMpService.setWxMpConfigStorage(clientMpConfigStorage());
        return clientWxMpService;
    }
}
