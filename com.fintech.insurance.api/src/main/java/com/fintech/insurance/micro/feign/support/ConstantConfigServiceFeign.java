package com.fintech.insurance.micro.feign.support;

import com.fintech.insurance.micro.api.support.ConstantConfigServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Author: Clayburn
 * @Description: 常量配置服务
 * @Date: 2017/11/09 09:49
 */
@FeignClient("insurance-micro-support")
public interface ConstantConfigServiceFeign extends ConstantConfigServiceAPI {
}
