package com.fintech.insurance.gateway;

import com.fintech.insurance.commons.constants.RoleConstants;
import com.fintech.insurance.commons.utils.URLUtils;
import com.fintech.insurance.gateway.service.GatewayOauthClientExceptionRenderer;
import com.fintech.insurance.gateway.service.GatewayUserInfoTokenService;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.HystrixAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@EnableZuulProxy
@SpringBootApplication(exclude = {HystrixAutoConfiguration.class})
@EnableResourceServer
@EnableDiscoveryClient
@EnableOAuth2Client
@EnableFeignClients
@EnableConfigurationProperties
@Configuration
public class GatewayServer extends ResourceServerConfigurerAdapter {

    @Autowired
    private ResourceServerProperties sso;

    @Autowired
    private GatewayOauthClientExceptionRenderer exceptionRenderer;

    public static void main(String[] args) {
        SpringApplication.run(GatewayServer.class, args);
    }

    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
    }

    @Bean
    public OAuth2RestTemplate clientCredentialsRestTemplate() {
        return new OAuth2RestTemplate(clientCredentialsResourceDetails());
    }

    @Primary
    @Bean
    public ResourceServerTokenServices tokenServices() {
        return new GatewayUserInfoTokenService(sso.getUserInfoUri(), sso.getClientId());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(URLUtils.WECHAT_INTEGRATION_URL_PATTERN).permitAll() //?????????????????????????????????
                .antMatchers(URLUtils.WECHAT_CHANNEL_URL_PATTERN, URLUtils.WECHAT_CUSTOMER_URL_PATTERN).permitAll() //???????????????????????????gateway?????????
                .antMatchers(URLUtils.OAUTH_URL_PATTERN).permitAll() //?????????????????????
                .antMatchers(URLUtils.MANAGEMENT_CALLBACK_URL_PATTERN).permitAll()
                //??????????????????
                .antMatchers("/management/requisition/audit/**").hasAnyAuthority(RoleConstants.ROLE_ADMIN, RoleConstants.ROLE_AUDITOR) //??????????????????????????????????????????
                .antMatchers(
                    "/management/requisition/cancel/**", //???????????????
                        "/management/requisition/confirmpaid/**", //???????????????
                        "/management/requisition/debit/**", //?????????????????????
                        "/management/refund/withhold/**", //????????????????????????
                        "/management/loan/confirm-loan/**", //???????????????
                        "/management/contract/insurance/confirm-return/**", //???????????????
                        "/management/refund/confirm-refund" //???????????????
                ).hasAnyAuthority(RoleConstants.ROLE_ADMIN, RoleConstants.ROLE_TREASURER) //???????????????????????????????????????
                .anyRequest().authenticated()
                .and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint() {
        OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
        entryPoint.setExceptionRenderer(this.exceptionRenderer);
        return entryPoint;
    }

    @Bean
    public OAuth2AccessDeniedHandler accessDeniedHandler() {
        OAuth2AccessDeniedHandler handler = new OAuth2AccessDeniedHandler();
        handler.setExceptionRenderer(this.exceptionRenderer);
        return handler;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint(oAuth2AuthenticationEntryPoint()).accessDeniedHandler(accessDeniedHandler());
    }
}