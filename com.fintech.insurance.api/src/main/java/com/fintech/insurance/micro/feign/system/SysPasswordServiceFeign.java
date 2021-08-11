package com.fintech.insurance.micro.feign.system;

import com.fintech.insurance.micro.api.system.SysPasswordServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("insurance-micro-system")
public interface SysPasswordServiceFeign extends SysPasswordServiceAPI {

}
