package com.fintech.insurance.micro.feign.finance;

import com.fintech.insurance.micro.api.finance.RepaymentPlanServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/16 0016 20:15
 */
@FeignClient("insurance-micro-finance")
public interface RepaymentPlanServiceFeign extends RepaymentPlanServiceAPI {
}
