package com.fintech.insurance.micro.feign.customer;

import com.fintech.insurance.micro.api.biz.IntentionServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("insurance-micro-customer")
public interface IntentionServiceFeign extends IntentionServiceAPI{

}
