package com.fintech.insurance.micro.feign.customer;


import com.fintech.insurance.micro.api.customer.CustomerServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("insurance-micro-customer")
public interface CustomerServiceFeign extends CustomerServiceAPI {

}
