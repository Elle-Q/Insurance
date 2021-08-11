package com.fintech.insurance.micro.feign.biz;

import com.fintech.insurance.micro.api.biz.ContractServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;


@FeignClient("insurance-micro-biz")
public interface ContractServiceFeign extends ContractServiceAPI {
}
