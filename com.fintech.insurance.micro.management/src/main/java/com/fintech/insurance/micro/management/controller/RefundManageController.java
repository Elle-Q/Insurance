package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.annotations.FinanceDuplicateSubmitDisable;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.biz.RecordVO;
import com.fintech.insurance.micro.dto.biz.RefundVO;
import com.fintech.insurance.micro.dto.validate.groups.Save;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import com.fintech.insurance.micro.feign.finance.RefundServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 还款管理
 * @Author: Yong Li
 * @Date: 2017/12/7 14:04
 */
@RestController
@RequestMapping(value = "/management/refund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RefundManageController extends BaseFintechManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(RefundManageController.class);

    @Autowired
    private RefundServiceFeign refundServiceFeign;

    /**
     * 条件查询还款信息
     * @param contractCode 合同编号
     * @param customerName 客户名称
     * @param refundStatus 还款状态
     * @param requisitionNumber 业务编号
     * @param refundBeginDate 还款开始时间
     * @param refundEndDate 还款结束时间
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    FintechResponse<Pagination<RefundVO>> pageRefundItem(@RequestParam(value = "contractCode", defaultValue = "") String contractCode,
                                                         @RequestParam(value = "customerName", defaultValue = "") String customerName,
                                                         @RequestParam(value = "refundStatus", defaultValue = "") String refundStatus,
                                                         @RequestParam(value = "requisitionNumber", defaultValue = "") String requisitionNumber,
                                                         @RequestParam(value = "carNumber", defaultValue = "") String carNumber,
                                                         @RequestParam(value = "refundBeginDate", required = false) String refundBeginDate,
                                                         @RequestParam(value = "refundEndDate", required = false) String refundEndDate,
                                                         @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                         @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        FintechResponse<Pagination<RefundVO>> result = refundServiceFeign.pageRefundItem(contractCode, customerName,
                refundStatus, requisitionNumber, carNumber, DateCommonUtils.convertDateStringToStamp(refundBeginDate, false),
                DateCommonUtils.convertDateStringToStamp(refundEndDate, true), pageIndex, pageSize);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }


    @RequestMapping(value = "/confirm-refund", method = RequestMethod.POST)
    @FinanceDuplicateSubmitDisable
    FintechResponse<VoidPlaceHolder> confirmRefund(@RequestBody @Validated(Save.class) RecordVO recordVO) {
        FintechResponse<VoidPlaceHolder> response = refundServiceFeign.confirmRefund(recordVO);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    @RequestMapping(value = "/manual-handling", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> dealWithHuman(@RequestBody @Validated(Update.class) RecordVO recordVO) {
        FintechResponse<VoidPlaceHolder> response = refundServiceFeign.dealWithHuman(recordVO);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    @RequestMapping(value = "/withhold", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> withHold(@RequestBody @Validated(Update.class) RecordVO recordVO) {
        FintechResponse<VoidPlaceHolder> response = refundServiceFeign.withHold(recordVO);;
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }
}
