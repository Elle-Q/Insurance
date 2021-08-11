package com.fintech.insurance.micro.feign.support;

import com.fintech.insurance.micro.api.support.OrganizationServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Description: balala
 * @Author: qxy
 * @Date: 2017/11/15 11:50
 */
@FeignClient("insurance-micro-support")
public interface OrganizationServiceFeign extends OrganizationServiceAPI {
}
