package com.fintech.insurance.micro.feign.thirdparty;

import com.fintech.insurance.micro.api.HelloServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/11 14:05
 */
@FeignClient("insurance-micro-thirdparty")
public interface HelloServiceFeign extends HelloServiceAPI {
}
