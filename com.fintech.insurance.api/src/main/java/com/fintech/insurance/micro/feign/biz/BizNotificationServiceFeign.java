package com.fintech.insurance.micro.feign.biz;

import com.fintech.insurance.micro.api.biz.BizNotificationServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/15 0015 17:23
 */
@FeignClient("insurance-micro-biz")
public interface BizNotificationServiceFeign extends BizNotificationServiceAPI {

}
