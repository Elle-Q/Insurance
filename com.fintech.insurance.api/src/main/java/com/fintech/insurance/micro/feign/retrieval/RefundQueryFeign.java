package com.fintech.insurance.micro.feign.retrieval;

import com.fintech.insurance.micro.api.retrieval.BizQueryAPI;
import com.fintech.insurance.micro.api.retrieval.RefundQueryAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/30 18:54
 */
@FeignClient("insurance-micro-retrieval")
public interface RefundQueryFeign extends RefundQueryAPI {
}
