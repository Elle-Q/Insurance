package com.fintech.insurance.authz.configuration;

import com.fintech.insurance.authz.service.AuthenticationService;
import com.fintech.insurance.commons.utils.URLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.authenticationService).passwordEncoder(this.authenticationService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers(URLUtils.WECHAT_INTEGRATION_URL_PATTERN).permitAll() //微信集成的接口无需鉴权
                .antMatchers(URLUtils.WECHAT_CHANNEL_URL_PATTERN, URLUtils.WECHAT_CUSTOMER_URL_PATTERN).permitAll() //微信端的接口无需在gateway中鉴权
                .antMatchers(URLUtils.MANAGEMENT_CALLBACK_URL_PATTERN).permitAll()
                .anyRequest().authenticated()
                .and().formLogin().permitAll()
                .and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
