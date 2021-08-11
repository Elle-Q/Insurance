package com.fintech.insurance.micro.feign.thirdparty.weixin;

import com.fintech.insurance.micro.api.thirdparty.weixin.WeixinIntegrationServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("insurance-micro-thirdparty")
public interface WeixinIntegrationServiceFeign extends WeixinIntegrationServiceAPI {
}
