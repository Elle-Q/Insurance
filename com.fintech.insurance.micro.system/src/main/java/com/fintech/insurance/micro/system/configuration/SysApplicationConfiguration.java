package com.fintech.insurance.micro.system.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

@Configuration
public class SysApplicationConfiguration {

    @Value("${fintech.insurance.security.password-salt}")
    private String passwordSalt = "sT4Fng0LRXLykQ2ICj5WKemb";

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new PasswordEncoder() {

            private final Logger logger = LoggerFactory.getLogger(PasswordEncoder.class.getSimpleName());

            @Override
            public String encode(CharSequence rawPassword) {
                rawPassword = (rawPassword == null ? "" : rawPassword);
                logger.debug("The original password is " + rawPassword);
                String encryptedPassword = DigestUtils.md5DigestAsHex((rawPassword + passwordSalt).getBytes());
                logger.debug("The encrypted password is " + encryptedPassword);
                return encryptedPassword;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                if (rawPassword == null || encodedPassword == null) {
                    return false;
                } else {
                    String calculatedEncryptedPassword = this.encode(rawPassword);
                    logger.debug("The input original password is " + rawPassword);
                    logger.debug("The calculated encrypted password is " + calculatedEncryptedPassword);
                    logger.debug("The encrypted password in store is " + encodedPassword);
                    return calculatedEncryptedPassword.equals(encodedPassword);
                }
            }
        };
    }
}
