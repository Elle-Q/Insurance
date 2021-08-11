package com.fintech.insurance.components.persist.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@EnableJpaRepositories(basePackages = "com.fintech.insurance",
		includeFilters={@ComponentScan.Filter(type= FilterType.ANNOTATION, value=Repository.class)},
		excludeFilters={@ComponentScan.Filter(type= FilterType.ANNOTATION, value=Service.class),
				@ComponentScan.Filter(type= FilterType.ANNOTATION, value=Controller.class)
		}
)
@EntityScan(basePackages = {"com.fintech.insurance"})
@Configuration
public class FintechPersistConfiguration implements EnvironmentAware {

	private static final Logger logger = LoggerFactory.getLogger(FintechPersistConfiguration.class);

	private Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Bean
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}
}
