package com.fintech.insurance.micro.feign.finance;


import com.fintech.insurance.micro.api.finance.RefundServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("insurance-micro-finance")
public interface RefundServiceFeign extends RefundServiceAPI {
}
