package com.fintech.insurance.micro.feign.finance;

import com.fintech.insurance.micro.api.finance.RepaymentRecordServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 15:48
 */
@FeignClient("insurance-micro-finance")
public interface RepaymentRecordServiceFeign extends RepaymentRecordServiceAPI {
}
