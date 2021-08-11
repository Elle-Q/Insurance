package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.biz.LoanServiceAPI;
import com.fintech.insurance.micro.biz.service.LoanService;
import com.fintech.insurance.micro.dto.biz.LoanVO;
import com.fintech.insurance.micro.dto.biz.RecordVO;
import com.fintech.insurance.micro.dto.validate.groups.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Clayburn
 * @Description: 放款管理
 * @Date: 2017/11/10 18:01
 */
@RestController
public class LoanController extends BaseFintechController implements LoanServiceAPI {
    @Autowired
    private LoanService loanService;

    @Override
    public FintechResponse<Pagination<LoanVO>> pageLoanInfo(@RequestParam(value = "requisitionNumber", defaultValue = "") String requisitionNumber,
                                                            @RequestParam(value = "requisitionStatus", defaultValue = "") String requisitionStatus,
                                                            @RequestParam(value = "productType", defaultValue = "") String productType,
                                                            @RequestParam(value = "channelName", defaultValue = "") String channelName,
                                                            @RequestParam(value = "customerName", defaultValue = "") String customerName,
                                                            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        ProductType type = null;
        RequisitionStatus status = null;
        try {
            if (StringUtils.isNotBlank(productType)) {
                type = ProductType.codeOf(productType);
            }
            if (StringUtils.isNotBlank(requisitionStatus)) {
                status = RequisitionStatus.codeOf(requisitionStatus);
            }
        } catch (Exception e) {
            throw new FInsuranceBaseException(104104);
        }

        return FintechResponse.responseData(loanService.pageLoanInfo(requisitionNumber, status, type, channelName, customerName,  pageIndex, pageSize));
    }

    @Override
    public FintechResponse<VoidPlaceHolder> confirmLoan(@RequestBody @Validated(Query.class) RecordVO recordVO) {
        loanService.recordLoan(recordVO);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }
}
