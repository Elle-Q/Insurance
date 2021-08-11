package com.fintech.insurance.micro.biz;

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
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/14 9:13
 */
@SpringBootApplication(exclude = {FintechPersistConfiguration.class, XADataSourceAutoConfiguration.class, SessionAutoConfiguration.class, HibernateJpaAutoConfiguration.class, DataSourceAutoConfiguration.class, MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, WebServicesAutoConfiguration.class, JmxAutoConfiguration.class})
public abstract class ServiceTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTestApplication.class, args);
    }
}
