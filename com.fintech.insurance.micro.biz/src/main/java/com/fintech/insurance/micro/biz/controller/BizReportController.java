package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.biz.BizReportServiceAPI;
import com.fintech.insurance.micro.biz.service.ContractService;
import com.fintech.insurance.micro.biz.service.ExportExcelService;
import com.fintech.insurance.micro.dto.biz.BizReportVO;
import com.fintech.insurance.micro.feign.retrieval.BizQueryFeign;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: 业务报表查询接口
 * @Author: Yong Li
 * @Date: 2017/11/10 12:16
 */
@RestController
public class BizReportController extends BaseFintechController implements BizReportServiceAPI {

    //导出业务模板文件
   private static final String REPORT_DEMO = "biz_report_business.xlsx";

    @Autowired
    private ContractService contractService;

    @Autowired
    private BizQueryFeign bizQueryFeign;

    @Resource
    private ExportExcelService service;

    @Override
    public FintechResponse<Pagination<BizReportVO>> queryBizReport(@RequestParam(value = "channelCode", required = false) String channelCode,
                                                                  @RequestParam(value = "customerName", required = false) String customerName,
                                                                  @RequestParam(value = "borrowStartTime", required = false) String borrowStartTime,
                                                                  @RequestParam(value = "borrowEndTime", required = false) String borrowEndTime,
                                                                   @RequestParam(value = "carNumber", required = false) String carNumber,
                                                                  @RequestParam(value = "productType", required = false) String productType,
                                                                  @RequestParam(value = "companyId", required = false) Integer companyId,
                                                                  @RequestParam(value = "contractStatus", required = false) String contractStatus,
                                                                  @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                  @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        FintechResponse<Pagination<BizReportVO>> page = bizQueryFeign.pageBizReportVO(channelCode, customerName, borrowStartTime, borrowEndTime, carNumber,
                productType,companyId, contractStatus, pageIndex, pageSize);
        return page;
    }

    @Override
    public FintechResponse<VoidPlaceHolder> exportBizReport(@RequestParam(value = "channelCode", required = false) String channelCode,
                                                            @RequestParam(value = "customerName", required = false) String customerName,
                                                            @RequestParam(value = "borrowStartTime", required = false) String borrowStartTime,
                                                            @RequestParam(value = "borrowEndTime", required = false) String borrowEndTime,
                                                            @RequestParam(value = "carNumber", required = false) String carNumber,
                                                            @RequestParam(value = "productType", required = false) String productType,
                                                            @RequestParam(value = "companyId", required = false) Integer companyId,
                                                            @RequestParam(value = "contractStatus", required = false) String contractStatus,
                                                            @NotBlank @RequestParam(value = "email") String email) {
        FintechResponse<Pagination<BizReportVO>> page = bizQueryFeign.pageBizReportVO(channelCode, customerName, borrowStartTime, borrowEndTime, carNumber,
                productType,companyId, contractStatus, 1, Integer.MAX_VALUE);
        if(page == null || page.getData() == null || page.getData().getItems() == null || page.getData().getItems().size() < 1){
            throw new FInsuranceBaseException(104115);
        }
        ProductType type = null;
        if(StringUtils.isNoneBlank(productType)){
            type = ProductType.codeOf(productType);
        }
        ContractStatus status = null;
        if(StringUtils.isNoneBlank(contractStatus)){
            status = ContractStatus.codeOf(contractStatus);
        }
        List<Map<String,Object>> contractVOList = contractService.convertListToMap(page.getData().getItems(), type, status);
        if(contractVOList == null || contractVOList.size() < 1){
            throw new FInsuranceBaseException(104115);
        }
        service.bizReportExportExcel(contractVOList, email, REPORT_DEMO);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }
}
