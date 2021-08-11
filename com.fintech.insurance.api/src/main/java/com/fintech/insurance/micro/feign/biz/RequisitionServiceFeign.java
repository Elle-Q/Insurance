package com.fintech.insurance.micro.feign.biz;

import com.fintech.insurance.micro.api.biz.RequisitionServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("insurance-micro-biz")
public interface RequisitionServiceFeign extends RequisitionServiceAPI{

}