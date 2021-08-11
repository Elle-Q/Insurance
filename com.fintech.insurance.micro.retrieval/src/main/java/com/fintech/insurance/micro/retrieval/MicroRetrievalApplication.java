package com.fintech.insurance.micro.retrieval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/30 18:06
 */
@EnableDiscoveryClient
@EnableFeignClients
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ServletComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, WebServicesAutoConfiguration.class, JmxAutoConfiguration.class})
public class MicroRetrievalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroRetrievalApplication.class, args);
    }
}
