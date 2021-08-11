package com.fintech.insurance.micro.system.controller;

import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.system.SysPasswordServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供运营管理平台用户统一的密码加密、密码匹配功能等。
 *
 * @author Sean
 * @since 2017-11-15 11:56
 */
@RestController
public class PasswordController extends BaseFintechController implements SysPasswordServiceAPI {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String encode(CharSequence rawPassword) {
        return this.passwordEncoder.encode(rawPassword == null ? "" : rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        } else {
            return this.passwordEncoder.matches(rawPassword, encodedPassword);
        }
    }
}
