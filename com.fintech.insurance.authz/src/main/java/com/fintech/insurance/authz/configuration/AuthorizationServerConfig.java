package com.fintech.insurance.authz.configuration;

import com.fintech.insurance.authz.service.AuthenticationService;
import com.fintech.insurance.authz.service.FinanceManagementTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${fintech.insurance.security.token-sign-key}")
    private String tokenSignKey = "";

    @Value("${fintech.insurance.security.enable-token-refresh}")
    private boolean enableTokenRefresh = true;

    @Value("${fintech.insurance.security.token-key-access}")
    private String tokenKeyAccess = "permitAll()";

    @Value("${fintech.insurance.security.check-token-access}")
    private String checkTokenAccess = "isAuthenticated()";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ApplicationContext context;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess(this.tokenKeyAccess).checkTokenAccess(this.checkTokenAccess);
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        return new FinanceManagementTokenEnhancer(this.tokenSignKey);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager).accessTokenConverter(accessTokenConverter())
            .userDetailsService(this.authenticationService);
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(this.enableTokenRefresh);
        return defaultTokenServices;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //clients.inMemory().withClient("management_platform").scopes("all").secret("ipay@123!").authorizedGrantTypes("password", "authorization_code", "refresh_token").accessTokenValiditySeconds(7 * 24 * 2600).refreshTokenValiditySeconds(30 * 24 * 3600);
        clients.jdbc(this.context.getBean(DataSource.class));
    }
}
