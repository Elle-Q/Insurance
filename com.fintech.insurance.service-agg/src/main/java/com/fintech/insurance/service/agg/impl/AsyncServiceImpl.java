package com.fintech.insurance.service.agg.impl;

import com.fintech.insurance.micro.dto.biz.ContractFileRequestVO;
import com.fintech.insurance.micro.feign.biz.ContractServiceFeign;
import com.fintech.insurance.service.agg.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/18 16:47
 */
@Async
@Service
public class AsyncServiceImpl implements AsyncService {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncServiceImpl.class);
}
