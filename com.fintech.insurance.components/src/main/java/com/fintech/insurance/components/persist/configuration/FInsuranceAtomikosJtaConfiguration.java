package com.fintech.insurance.components.persist.configuration;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.UserTransaction;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2018/1/17 12:22
 */
@Configuration
public class FInsuranceAtomikosJtaConfiguration {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean(initMethod = "init", destroyMethod = "close")
    public DruidXADataSource druidXADataSource() {
        return DataSourceProperties.toDruidDataSource(this.dataSourceProperties);
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    @Primary
    public AtomikosDataSourceBean atomikosXADataSource() {
        AtomikosDataSourceBean atomikosDataSource = new AtomikosDataSourceBean();
        atomikosDataSource.setUniqueResourceName(dataSourceProperties.getResourceUniqueName());
        atomikosDataSource.setXaDataSource(druidXADataSource());
        atomikosDataSource.setMaxPoolSize(this.dataSourceProperties.getMaxPoolSize() !=0 ? this.dataSourceProperties.getMaxPoolSize(): 50);
        atomikosDataSource.setMinPoolSize(this.dataSourceProperties.getMinPoolSize() != 0 ? this.dataSourceProperties.getMinPoolSize() : 5);
        return atomikosDataSource;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public UserTransactionManager jtaTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }

    @Bean
    public UserTransaction jtaUserTransaction() throws Exception {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(this.dataSourceProperties.getTransactionTimeout());
        return userTransactionImp;
    }

    @Bean
    public JtaTransactionManager transactionManager() throws Exception {
        return new JtaTransactionManager(this.jtaUserTransaction(), this.jtaTransactionManager());
    }
}


