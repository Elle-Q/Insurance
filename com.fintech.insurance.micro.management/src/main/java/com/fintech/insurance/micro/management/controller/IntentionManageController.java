package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.customer.CustomerConsultationVO;
import com.fintech.insurance.micro.feign.customer.IntentionServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/7 15:47
 */
@RestController
@RequestMapping(value = "/management/intention", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class IntentionManageController extends BaseFintechManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(IntentionManageController.class);

    @Autowired
    private IntentionServiceFeign intentionServiceFeign;

    /**
     * 意向管理分页查询
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    FintechResponse<Pagination<CustomerConsultationVO>> pageIntention(@RequestParam(name = "submmitStartTime", required = false) String submmitStartTime,
                                                              @RequestParam(name = "submmitEndTime", required = false) String submmitEndTime,
                                                              @RequestParam(name = "mobile", defaultValue = "") String mobile,
                                                              @RequestParam(name = "name", defaultValue = "") String name,
                                                              @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                              @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        FintechResponse<Pagination<CustomerConsultationVO>> result = intentionServiceFeign.pageIntention(
                DateCommonUtils.convertDateStringToStamp(submmitStartTime, false),
                DateCommonUtils.convertDateStringToStamp(submmitEndTime, true),
                mobile, name, pageIndex, pageSize);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }
}


