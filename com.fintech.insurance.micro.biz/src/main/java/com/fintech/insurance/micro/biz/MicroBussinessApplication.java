package com.fintech.insurance.micro.biz;


import com.fintech.insurance.commons.beans.TemplateMessageConfigBean;
import com.fintech.insurance.commons.beans.WeixinConfigBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.fintech.insurance.micro.feign.customer",
        "com.fintech.insurance.micro.feign.finance",
        "com.fintech.insurance.micro.feign.retrieval",
        "com.fintech.insurance.micro.feign.support",
        "com.fintech.insurance.micro.feign.system",
        "com.fintech.insurance.micro.feign.thirdparty",
        "com.fintech.insurance.micro.feign.thirdparty.sms",
        "com.fintech.insurance.micro.feign.thirdparty.weixin"}
        )
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ServletComponentScan
@RefreshScope
@EnableAsync
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, WebServicesAutoConfiguration.class, JmxAutoConfiguration.class})
public class MicroBussinessApplication {

    @Bean
    @ConfigurationProperties(prefix = "weixin.customer-client")
    public WeixinConfigBean customerWxConfigBean() {
        return new WeixinConfigBean();
    }

    @Bean
    @ConfigurationProperties(prefix = "weixin.channel-client")
    public WeixinConfigBean clientWxConfigBean() {
        return new WeixinConfigBean();
    }

    @Bean
    public TemplateMessageConfigBean templateMessageConfigBean() {
        return new TemplateMessageConfigBean();
    }

    public static void main(String[] args) {
        SpringApplication.run(MicroBussinessApplication.class, args);
    }
}
