package com.fintech.insurance.micro.feign.support;

import com.fintech.insurance.micro.api.support.AdvertisementServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/15 18:25
 */
@FeignClient("insurance-micro-support")
public interface AdvertisementServiceFeign extends AdvertisementServiceAPI{
}
