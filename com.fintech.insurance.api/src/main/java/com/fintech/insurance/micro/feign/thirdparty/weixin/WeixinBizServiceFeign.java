package com.fintech.insurance.micro.feign.thirdparty.weixin;


import com.fintech.insurance.micro.api.thirdparty.weixin.WeixinBizServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("insurance-micro-thirdparty")
public interface WeixinBizServiceFeign extends WeixinBizServiceAPI {

}
