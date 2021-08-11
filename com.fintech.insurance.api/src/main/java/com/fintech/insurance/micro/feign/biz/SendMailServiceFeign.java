package com.fintech.insurance.micro.feign.biz;

import com.fintech.insurance.micro.api.biz.SendMailServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("insurance-micro-thirdparty")
public interface SendMailServiceFeign extends SendMailServiceAPI {

}
