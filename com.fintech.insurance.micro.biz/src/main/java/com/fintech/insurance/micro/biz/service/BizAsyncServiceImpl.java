package com.fintech.insurance.micro.biz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2018/1/15 15:52
 */
@Deprecated //异步服务有潜在问题 慎用
@Service
public class BizAsyncServiceImpl implements BizAsyncService {

    private static final Logger LOG = LoggerFactory.getLogger(BizAsyncServiceImpl.class);

    @Autowired
    private ContractService contractService;

    @Override
    public void generateContractFileForRequsition(Integer requsitionId) {
        contractService.generateContractFileOfRequsition(requsitionId);
    }
}
