package com.fintech.insurance.micro.api.system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 系统用户的密码接口定义
 */
@RequestMapping(path = "/system/password")
public interface SysPasswordServiceAPI {

    /**
     * 加密密码
     * @param rawPassword 密码明文
     * @return
     */
    @GetMapping(path = "/encode")
    String encode(@RequestParam(name = "rawPassword") CharSequence rawPassword);

    /**
     * 验证密码明文与密文是否匹配
     *
     * @param rawPassword 待验证的密码明文
     * @param encodedPassword 数据库中获取的密码密文
     * @return 如果匹配则返回true，否则返回false
     */
    @GetMapping(path = "/matches")
    boolean matches(@RequestParam(name = "rawPassword") CharSequence rawPassword, @RequestParam(name = "encodedPassword") String encodedPassword);

}
