package com.fintech.insurance.micro.feign.thirdparty;

import com.fintech.insurance.micro.api.thirdparty.BestsignServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("insurance-micro-thirdparty")
public interface BestsignServiceFeign extends BestsignServiceAPI {
}
