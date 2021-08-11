package com.fintech.insurance.micro.feign.support;

import com.fintech.insurance.micro.api.support.InsuranceCompanyConfigServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("insurance-micro-support")
public interface InsuranceCompanyConfigServiceFeign extends InsuranceCompanyConfigServiceAPI {

}
