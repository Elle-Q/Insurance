package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.biz.ContractDetailVO;
import com.fintech.insurance.micro.dto.biz.ContractVO;
import com.fintech.insurance.micro.dto.biz.RecordVO;
import com.fintech.insurance.micro.dto.validate.groups.Find;
import com.fintech.insurance.micro.feign.biz.ContractServiceFeign;
import com.fintech.insurance.micro.feign.finance.RepaymentPlanServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/7 15:41
 */
@RestController
@RequestMapping(value = "/management/contract", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ContractManageController extends BaseFintechManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(ContractManageController.class);

    @Autowired
    private ContractServiceFeign contractServiceFeign;

    @Autowired
    private RepaymentPlanServiceFeign repaymentPlanServiceFeign;

    @Autowired
    private QiniuBusinessServiceFeign qiniuBusinessServiceFeign;

    /**
     * 条件查询合同
     * @param contractCode  合同编号
     * @param contractStatus 合同状态
     * @param channelName 渠道名称
     * @param customerName 客户名称
     * @param loanBeginDate 放款开始时间
     * @param loanEndDate 放款结束时间
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    FintechResponse<Pagination<ContractVO>> pageContract(@RequestParam(value = "contractCode", defaultValue = "") String contractCode,
                                                         @RequestParam(value = "contractStatus", defaultValue = "")String contractStatus,
                                                         @RequestParam(value = "channelName", defaultValue = "")String channelName,
                                                         @RequestParam(value = "customerName", defaultValue = "")String customerName,
                                                         @RequestParam(value = "carNumber", defaultValue = "") String carNumber,
                                                         @RequestParam(value = "loanBeginDate", required = false)String loanBeginDate,
                                                         @RequestParam(value = "loanEndDate", required = false)String loanEndDate,
                                                         @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                         @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        FintechResponse<Pagination<ContractVO>> result = contractServiceFeign.pageContract(contractCode, contractStatus, channelName,
                customerName, carNumber, DateCommonUtils.convertDateStringToStamp(loanBeginDate, false),
                DateCommonUtils.convertDateStringToStamp(loanEndDate, true), pageIndex, pageSize);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 根据合同编号查询合同详情
     * @param contractCode
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    FintechResponse<ContractDetailVO> getContractDetailByNo(@RequestParam("contractCode") String contractCode) {
        FintechResponse<ContractDetailVO> result = contractServiceFeign.getContractDetailByNo(contractCode);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 确认退保操作
     * @param recordVO
     */
    @RequestMapping(value = "/insurance/confirm-return", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> confirmReturnInsurance(@RequestBody @Validated({Find.class}) RecordVO recordVO) {
        FintechResponse<VoidPlaceHolder> result = repaymentPlanServiceFeign.confirmReturnInsurance(recordVO.getCode());
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return FintechResponse.voidReturnInstance();
    }

    /**
     * 获取合同下载地址
     * @param pdfContractUrl
     * @param contractId
     * @param isServiceContract
     * @return
     */
    @RequestMapping(value = "/getDownloadUrl", method = RequestMethod.GET)
    FintechResponse<String> getDownloadFileUrl(@RequestParam(name = "pdfContractUrl", required = false) String pdfContractUrl,
                                               @RequestParam("contractId")Integer contractId,
                                               @RequestParam("isServiceContract")Integer isServiceContract){
        if (pdfContractUrl == null || StringUtil.isBlank(pdfContractUrl)){
            // 创建PDF合同
            FintechResponse<String> response = contractServiceFeign.getRequisitionContractFile(contractId, isServiceContract, false);
            if (!response.isOk()) {
                throw FInsuranceBaseException.buildFromErrorResponse(response);
            }
            return FintechResponse.responseData(response.getData());
        } else {
            // 获取QiniuDownloadUrl
            FintechResponse<String> qiniuFile = qiniuBusinessServiceFeign.getQiniuDownloadUrl(pdfContractUrl, 0);
            if (!qiniuFile.isOk()) {
                throw FInsuranceBaseException.buildFromErrorResponse(qiniuFile);
            }
            return FintechResponse.responseData(qiniuFile.getData());
        }
    }

}
