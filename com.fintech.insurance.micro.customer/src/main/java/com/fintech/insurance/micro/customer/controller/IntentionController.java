package com.fintech.insurance.micro.customer.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.biz.IntentionServiceAPI;
import com.fintech.insurance.micro.customer.service.CustomerConsultationService;
import com.fintech.insurance.micro.dto.customer.CustomerConsultationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Validated
public class IntentionController extends BaseFintechController implements IntentionServiceAPI {

    @Autowired
    private CustomerConsultationService customerConsultationService;

    public FintechResponse<Pagination<CustomerConsultationVO>> pageIntention(@RequestParam(name = "submmitStartTime", required = false) Long submmitStartTime,
                                                                             @RequestParam(name = "submmitEndTime", required = false) Long submmitEndTime,
                                                                             @RequestParam(name = "mobile", defaultValue = "") String mobile,
                                                                             @RequestParam(name = "name", defaultValue = "") String name,
                                                                             @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                             @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        Date startTime = null;
        Date endTime = null;
        if (null != submmitStartTime) {
            startTime = DateCommonUtils.stampToDate(submmitStartTime);
        }
        if (null != submmitEndTime) {
            endTime = DateCommonUtils.stampToDate(submmitEndTime);
        }
        return FintechResponse.responseData(customerConsultationService.query(startTime, endTime, mobile, name, pageIndex, pageSize));

    }

    @Override
    public FintechResponse<Integer> saveConsultation(@RequestBody CustomerConsultationVO customerConsultationVO) {
        return FintechResponse.responseData(customerConsultationService.save(customerConsultationVO));
    }
}
