package com.fintech.insurance.authz.service;

import com.fintech.insurance.micro.dto.system.UserVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("insurance-micro-system")
public interface SysUserApiClient {

    @GetMapping(path = "/system/user/find")
    UserVO getUserByMobile(@RequestParam(name = "mobile") String mobile);
}
