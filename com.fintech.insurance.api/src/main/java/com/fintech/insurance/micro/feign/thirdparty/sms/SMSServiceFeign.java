package com.fintech.insurance.micro.feign.thirdparty.sms;

import com.fintech.insurance.micro.api.thirdparty.sms.SMSServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("insurance-micro-thirdparty")
public interface SMSServiceFeign extends SMSServiceAPI {
}
