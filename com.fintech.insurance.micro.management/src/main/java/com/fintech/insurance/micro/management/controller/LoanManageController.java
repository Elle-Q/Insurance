package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.biz.LoanVO;
import com.fintech.insurance.micro.dto.biz.RecordVO;
import com.fintech.insurance.micro.dto.validate.groups.Query;
import com.fintech.insurance.micro.feign.biz.LoanServiceFeign;
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
 * @Date: 2017/12/7 15:28
 */
@RestController
@RequestMapping(value = "/management/loan", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoanManageController extends BaseFintechManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(LoanManageController.class);

    @Autowired
    private LoanServiceFeign loanServiceFeign;

    /**
     * 条件查询放款信息
     * @param requisitionNumber 业务单号
     * @param requisitionStatus 订单状态
     * @param productType 产品类型
     * @param channelName 渠道名称
     * @param customerName 客户名称
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    FintechResponse<Pagination<LoanVO>> pageLoanInfo(@RequestParam(value = "requisitionNumber", defaultValue = "") String requisitionNumber,
                                                     @RequestParam(value = "requisitionStatus", defaultValue = "") String requisitionStatus,
                                                     @RequestParam(value = "productType", defaultValue = "") String productType,
                                                     @RequestParam(value = "channelName", defaultValue = "") String channelName,
                                                     @RequestParam(value = "customerName", defaultValue = "") String customerName,
                                                     @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                     @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        FintechResponse<Pagination<LoanVO>> result = loanServiceFeign.pageLoanInfo(requisitionNumber, requisitionStatus, productType,
                channelName, customerName, pageIndex, pageSize);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    @RequestMapping(value = "/confirm-loan", method = RequestMethod.POST)
    void confirmLoan(@RequestBody @Validated(Query.class) RecordVO recordVO) {
        loanServiceFeign.confirmLoan(recordVO);
    }


}
