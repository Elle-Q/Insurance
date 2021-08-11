package com.fintech.insurance.micro.timer.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.feign.biz.ContractServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 还款
 * @Author: qxy
 * @Date: 2017/12/19 16:24
 */
@RestController
@RequestMapping(value = "/timer/contract", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ContractTimerController {

    private static final Logger LOG = LoggerFactory.getLogger(ContractTimerController.class);

    @Autowired
    private ContractServiceFeign contractServiceFeign;

    /**
     * 生成合同文件
     * @return
     */
    @GetMapping(value = "/generate-contract-file")
    public FintechResponse<VoidPlaceHolder> generateContractFileForRequisition() {

        LOG.info("generate ContractFile For Requsitions");

        FintechResponse<VoidPlaceHolder> response = contractServiceFeign.generateContractFileForRequsitions();
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        LOG.info("succeed in generating ContractFile For Requsitions");
        return response;
    }

}
