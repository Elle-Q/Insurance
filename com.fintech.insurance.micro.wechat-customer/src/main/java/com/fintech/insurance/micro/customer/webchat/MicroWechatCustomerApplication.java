package com.fintech.insurance.micro.customer.webchat;

import com.fintech.insurance.commons.beans.WeixinConfigBean;
import com.fintech.insurance.components.persist.configuration.FInsuranceAtomikosJtaConfiguration;
import com.fintech.insurance.components.persist.configuration.FintechPersistConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/6 18:56
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.fintech.insurance.micro.feign.biz",
        "com.fintech.insurance.micro.feign.customer",
        "com.fintech.insurance.micro.feign.finance",
        "com.fintech.insurance.micro.feign.retrieval",
        "com.fintech.insurance.micro.feign.support",
        "com.fintech.insurance.micro.feign.system",
        "com.fintech.insurance.micro.feign.thirdparty",
        "com.fintech.insurance.micro.feign.thirdparty.sms",
        "com.fintech.insurance.micro.feign.thirdparty.weixin"})
@ServletComponentScan
@EnableAsync
@ComponentScan(basePackages = {"com.fintech.insurance.service.agg", "com.fintech.insurance.micro.customer.webchat"})
@SpringBootApplication(exclude = {FintechPersistConfiguration.class, FInsuranceAtomikosJtaConfiguration.class, TransactionAutoConfiguration.class, XADataSourceAutoConfiguration.class, SessionAutoConfiguration.class, HibernateJpaAutoConfiguration.class, DataSourceAutoConfiguration.class, MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, WebServicesAutoConfiguration.class, JmxAutoConfiguration.class})
public class MicroWechatCustomerApplication {

    @Bean
    @ConfigurationProperties(prefix = "weixin.customer-client")
    public WeixinConfigBean customerClientWeixinConfig() {
        return new WeixinConfigBean();
    }

    public static void main(String[] args) {
        SpringApplication.run(MicroWechatCustomerApplication.class, args);
    }
}
