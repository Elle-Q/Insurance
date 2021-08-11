package com.fintech.insurance.micro.feign.thirdparty;

import com.fintech.insurance.micro.api.thirdparty.ImageVercodeServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("insurance-micro-thirdparty")
public interface ImageVercodeServiceFeign extends ImageVercodeServiceAPI {

}
