package com.fintech.insurance.components.web.configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fintech.insurance.commons.utils.URLUtils;
import com.fintech.insurance.components.web.MicroJsonMessageConverter;
import com.fintech.insurance.components.web.interceptor.FintechResponseInterceptor;
import com.fintech.insurance.components.web.interceptor.FInsuranceApplicationContextInterceptor;
import com.fintech.insurance.components.web.interceptor.WxMpTokenInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Iterator;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/10 12:41
 */
@Configuration
//@ConditionalOnProperty(prefix = "fintech.web.fastjson",value = "enabled", matchIfMissing = true)
public class MicroWebAppConfigurer extends WebMvcConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(MicroWebAppConfigurer.class);

    @Autowired
    @Qualifier("jacksonObjectMapper")
    private ObjectMapper jacksonObjectMapper;

    /*@Value("${fintech.web.fastjson.serializerFeatures}")
    private String[] serializerFeatures;*/

    /**
     * token的加密
     */
    @Value("${fintech.insurance.security.mp-token-sign-key:pX9a5WTiCRvx1cE6REa0kCq86uFewHWYhYOlejW2h9F3trijLu1yEh3NqqQxgaLODkTNVAoYMtvzoBF9vJc8AI15eZuKY17yqAcywqFa6fcU6IlIiZ4rYpzgDSoEkVfc}")
    protected String mpTokenSignKey;

    /**
     * token的过期时间
     */
    @Value("${fintech.insurance.security.mp-token-expire-seconds:604800}")
    protected int mpTokenExpireSeconds;

    /**
     * 证书签发者
     */
    @Value("${fintech.insurance.security.mp-token-issuer:xinlebao}")
    protected String mpTokenIssuer;

    @Autowired
    private Environment environment;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        WxMpTokenInterceptor wxMpTokenInterceptor = new WxMpTokenInterceptor(this.environment);
        wxMpTokenInterceptor.setMpTokenExpireSeconds(this.mpTokenExpireSeconds);
        wxMpTokenInterceptor.setMpTokenIssuer(this.mpTokenIssuer);
        wxMpTokenInterceptor.setMpTokenSignKey(this.mpTokenSignKey);
        registry.addInterceptor(wxMpTokenInterceptor).addPathPatterns(URLUtils.WECHAT_CHANNEL_URL_PATTERN, URLUtils.WECHAT_CUSTOMER_URL_PATTERN);
        registry.addInterceptor(new FInsuranceApplicationContextInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new FintechResponseInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(jacksonObjectMapper);
        return converter;
    }

    /*@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //清除默认的MappingJackson2HttpMessageConverter
        Iterator<HttpMessageConverter<?>> it = converters.iterator();
        while (it.hasNext()) {
            HttpMessageConverter<?> converter = it.next();
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                it.remove();
            }
        }

        super.configureMessageConverters(converters);
        MicroJsonMessageConverter fastJsonConverter = MicroJsonMessageConverter.converterInstance;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        if (null != this.serializerFeatures && this.serializerFeatures.length > 0) {
            SerializerFeature[] addingFeatures = new SerializerFeature[this.serializerFeatures.length];
            for (int i =0; i < this.serializerFeatures.length; i++) {
                //fastJsonConfig.setSerializerFeatures(SerializerFeature.valueOf(SerializerFeature.class, serializerFeatures[i]));
                addingFeatures[i] = SerializerFeature.valueOf(SerializerFeature.class, serializerFeatures[i]);
            }
            fastJsonConfig.setSerializerFeatures(addingFeatures);
        }
        LOG.debug("==========Debug FastJSON CONFIG:");
        for (SerializerFeature feature : fastJsonConfig.getSerializerFeatures()) {
            LOG.debug(feature.name());
        }
        fastJsonConverter.setFastJsonConfig(fastJsonConfig);
        //添加自定义的MicroJsonMessageConverter
        converters.add(fastJsonConverter);
    }*/

}
