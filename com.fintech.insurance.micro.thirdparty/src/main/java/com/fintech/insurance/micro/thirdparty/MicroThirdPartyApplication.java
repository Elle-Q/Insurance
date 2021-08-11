package com.fintech.insurance.micro.thirdparty;

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
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.fintech.insurance.micro.feign.customer",
        "com.fintech.insurance.micro.feign.finance",
        "com.fintech.insurance.micro.feign.retrieval",
        "com.fintech.insurance.micro.feign.support",
        "com.fintech.insurance.micro.feign.system"}
)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ServletComponentScan
@RefreshScope
@SpringBootApplication(exclude = {FintechPersistConfiguration.class, FInsuranceAtomikosJtaConfiguration.class, XADataSourceAutoConfiguration.class, SessionAutoConfiguration.class, HibernateJpaAutoConfiguration.class, DataSourceAutoConfiguration.class, MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, WebServicesAutoConfiguration.class, JmxAutoConfiguration.class})
public class MicroThirdPartyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroThirdPartyApplication.class, args);
    }
}
