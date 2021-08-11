package com.fintech.insurance.micro.channel.webchat.controller;

import com.fintech.insurance.commons.annotations.FinanceDuplicateSubmitDisable;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.biz.ContractFileRequestVO;
import com.fintech.insurance.micro.dto.biz.ContractInfoVO;
import com.fintech.insurance.micro.dto.biz.SaveRequisitionContractVO;
import com.fintech.insurance.micro.feign.biz.ContractServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;


/**
 * @Description: 微信渠道端外部接口门面
 * @Author: Yong Li
 * @Date: 2017/12/6 18:55
 */
@RestController
@RequestMapping(value = "/wechat/channel/contract", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
@RequireWechatLogin
public class ContractController extends BaseFintechWechatController {

    private static final Logger LOG = LoggerFactory.getLogger(ContractController.class);

    @Autowired
    private ContractServiceFeign contractServiceFeign;

    /**
     * 第四步： 生成合同以及还款计划
     * @param id
     * @param month
     * @return
     */
    @RequestMapping(value = "/get-requisition-contract", method = RequestMethod.GET)
    @FinanceDuplicateSubmitDisable(value = 15)
    public FintechResponse<ContractInfoVO> getRequisitionContractInfoVOByRequisitionId(@RequestParam(value = "id") Integer id,
                                                                                       @RequestParam(value = "month", defaultValue = "12") Integer month){
        LOG.error("channel getRequisitionContractInfoVOByRequisitionId begin");
        return contractServiceFeign.getRequisitionContractInfoVOByRequisitionId(id, month);
    }


    /**
     * 第五步: 更新状态
     * @param requisitionContractVO
     * @return
     */
    @RequestMapping(value = "/save-requisition-contract", method = RequestMethod.POST)
    @FinanceDuplicateSubmitDisable(value = 15)
    public FintechResponse<VoidPlaceHolder> saveRequisitionContractInfoVO(@Validated @RequestBody SaveRequisitionContractVO requisitionContractVO){
        LOG.info("===== 更新申请单状态为已提交");
        FintechResponse<ContractFileRequestVO> respondVO = contractServiceFeign.saveRequisitionContractInfoVO(requisitionContractVO.getId());
        LOG.info("===== 更新申请单状态为完成:" + respondVO.getCode());
        if(!respondVO.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(respondVO);
        }
        LOG.info("合同已经提交完成");
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @RequestMapping(value = "/get-requisition-contract-file", method = RequestMethod.GET)
    public FintechResponse<String> saveRequisitionContractFile(@RequestParam(value = "contractId") Integer contractId,
                                                               @RequestParam(value = "isServiceContract") Integer isServiceContract) {
        FintechResponse<String> response = contractServiceFeign.getRequisitionContractFile(contractId, isServiceContract, true);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return FintechResponse.responseData(response.getData());
    }
}
