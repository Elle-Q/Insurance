package com.fintech.insurance.micro.api.biz;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.BizReportVO;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: service for the export of business report.
 * @Author: Yong Li
 * @Date: 2017/11/10 11:50
 */
@RequestMapping(value = "/biz/report", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface BizReportServiceAPI {

    /**
     *  根据搜索条件查询业务报表
     *
     * @param channelCode 渠道代码
     * @param customerName 客户名称
     * @param productType 产品类型
     * @param companyId 合同的业务所属分公司ID
     * @param contractStatus 合同当前状态代码
     * @param borrowStartTime 合同借款查询开始时间
     * @param borrowEndTime 合同借款查询结束时间
     * @param carNumber 车牌号
     * @param pageIndex 页码
     * @param pageSize 每页记录数
     * @return
     */
    @GetMapping(path = "/page")
    FintechResponse<Pagination<BizReportVO>> queryBizReport(@RequestParam(value = "channelCode", required = false) String channelCode,
                                                           @RequestParam(value = "customerName", required = false) String customerName,
                                                           @RequestParam(value = "borrowStartTime", required = false) String borrowStartTime,
                                                           @RequestParam(value = "borrowEndTime", required = false) String borrowEndTime,
                                                            @RequestParam(value = "carNumber", required = false) String carNumber,
                                                           @RequestParam(value = "productType", required = false) String productType,
                                                           @RequestParam(value = "companyId", required = false) Integer companyId,
                                                           @RequestParam(value = "contractStatus", required = false) String contractStatus,
                                                           @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                           @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);

    /**
     *  根据搜索条件查询业务导出报表
     *
     * @param channelCode 渠道代码
     * @param customerName 客户名称
     * @param productType 产品类型
     * @param companyId 合同的业务所属分公司ID
     * @param contractStatus 合同当前状态代码
     * @param borrowStartTime 合同借款查询开始时间
     * @param borrowEndTime 合同借款查询结束时间
     * @param carNumber 车牌号
     * @return
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    FintechResponse<VoidPlaceHolder> exportBizReport(@RequestParam(value = "channelCode", required = false) String channelCode,
                                                     @RequestParam(value = "customerName", required = false) String customerName,
                                                     @RequestParam(value = "borrowStartTime", required = false) String borrowStartTime,
                                                     @RequestParam(value = "borrowEndTime", required = false) String borrowEndTime,
                                                     @RequestParam(value = "carNumber", required = false) String carNumber,
                                                     @RequestParam(value = "productType", required = false) String productType,
                                                     @RequestParam(value = "companyId", required = false) Integer companyId,
                                                     @RequestParam(value = "contractStatus", required = false) String contractStatus,
                                                     @NotBlank @RequestParam(value = "email") String email);

}
