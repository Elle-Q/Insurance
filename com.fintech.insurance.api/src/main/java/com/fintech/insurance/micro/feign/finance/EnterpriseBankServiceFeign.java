package com.fintech.insurance.micro.feign.finance;

import com.fintech.insurance.micro.api.finance.EnterpriseBankServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/26 14:25
 */
@FeignClient("insurance-micro-finance")
public interface EnterpriseBankServiceFeign extends EnterpriseBankServiceAPI {
}
