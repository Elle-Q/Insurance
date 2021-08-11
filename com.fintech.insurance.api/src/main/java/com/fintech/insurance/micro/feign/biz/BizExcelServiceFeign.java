package com.fintech.insurance.micro.feign.biz;

import com.fintech.insurance.micro.api.biz.BizExcelServiceAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Description: service for the export of business report.
 * @Author: Yong Li
 * @Date: 2017/11/10 11:50
 */

@FeignClient("insurance-micro-biz")
public interface BizExcelServiceFeign extends BizExcelServiceAPI {

}
