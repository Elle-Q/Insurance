package com.fintech.insurance.micro.api.biz;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.LoanVO;
import com.fintech.insurance.micro.dto.biz.RecordVO;
import com.fintech.insurance.micro.dto.validate.groups.Query;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = "/biz/loan", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface LoanServiceAPI {
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
                                                     @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);

    @RequestMapping(value = "/confirm-loan", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> confirmLoan(@RequestBody @Validated(Query.class) RecordVO recordVO);
}
