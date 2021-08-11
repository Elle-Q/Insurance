package com.fintech.insurance.micro.feign.finance;

import com.fintech.insurance.micro.api.finance.RecordAccountServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Author: Clayburn
 * @Description: 用于上传财务相关的图片凭证
 * @Date: 2017/11/20 11:11
 */
@FeignClient("insurance-micro-finance")
public interface RecordAccountServiceFeign extends RecordAccountServiceAPI {
}
