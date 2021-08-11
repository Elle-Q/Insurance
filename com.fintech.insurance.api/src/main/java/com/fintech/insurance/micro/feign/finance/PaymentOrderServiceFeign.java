package com.fintech.insurance.micro.feign.finance;

import com.fintech.insurance.micro.api.finance.PaymentOrderServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Author: qxy
 * @Description:
 * @Date: 2017/11/20 11:11
 */
@FeignClient("insurance-micro-finance")
public interface PaymentOrderServiceFeign extends PaymentOrderServiceAPI {
}
