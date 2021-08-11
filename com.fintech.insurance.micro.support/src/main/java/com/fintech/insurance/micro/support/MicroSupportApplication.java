package com.fintech.insurance.micro.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.fintech.insurance.micro.feign.customer",
        "com.fintech.insurance.micro.feign.biz",
        "com.fintech.insurance.micro.feign.retrieval",
        "com.fintech.insurance.micro.feign.finance",
        "com.fintech.insurance.micro.feign.system",
        "com.fintech.insurance.micro.feign.thirdparty",
        "com.fintech.insurance.micro.feign.thirdparty.sms",
        "com.fintech.insurance.micro.feign.thirdparty.weixin"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ServletComponentScan
@RefreshScope
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, WebServicesAutoConfiguration.class, JmxAutoConfiguration.class})
public class MicroSupportApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroSupportApplication.class, args);
    }
}
