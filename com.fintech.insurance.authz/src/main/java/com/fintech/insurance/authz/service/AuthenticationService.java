package com.fintech.insurance.authz.service;

import com.fintech.insurance.authz.util.FinanceManageUser;
import com.fintech.insurance.micro.dto.system.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
@Configuration
public class AuthenticationService implements UserDetailsService, PasswordEncoder {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Value("${fintech.insurance.security.password-salt}")
    private String passwordSalt = "sT4Fng0LRXLykQ2ICj5WKemb";

    @Autowired
    private SysUserApiClient sysUserApiClient;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("username cannot be empty");
        }
        final UserVO user = this.sysUserApiClient.getUserByMobile(username);
        if (user == null) {
            throw new UsernameNotFoundException("username error");
        }
        FinanceManageUser manageUser = new FinanceManageUser();
        manageUser.setId(user.getId());
        manageUser.setMobilePhone(user.getMobile());
        manageUser.setPassword(user.getPassword());
        manageUser.setUserName(user.getName());
        manageUser.setRoles(user.getRoles());
        manageUser.setUserType(user.getUserType());
        manageUser.setOrganizationId(user.getOrganizationId());
        manageUser.setChannelCode(user.getChannelCode());
        manageUser.setChannelAdmin(user.isChannelAdmin());
        manageUser.setLocked(user.getIsLocked() != null && user.getIsLocked() > 0);
        return manageUser;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        rawPassword = (rawPassword == null ? "" : rawPassword);
        return DigestUtils.md5DigestAsHex((rawPassword + passwordSalt).getBytes());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        } else {
            String calculatedEncryptedPassword = this.encode(rawPassword);
            return calculatedEncryptedPassword.equals(encodedPassword);
        }
    }
}
