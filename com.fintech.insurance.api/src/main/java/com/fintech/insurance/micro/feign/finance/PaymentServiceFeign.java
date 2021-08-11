package com.fintech.insurance.micro.feign.finance;

import com.fintech.insurance.micro.api.finance.PaymentServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("insurance-micro-finance")
public interface PaymentServiceFeign extends PaymentServiceAPI{
}
