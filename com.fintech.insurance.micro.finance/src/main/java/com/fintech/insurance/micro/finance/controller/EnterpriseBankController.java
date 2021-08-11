package com.fintech.insurance.micro.finance.controller;

import com.fintech.insurance.commons.enums.ApplicationType;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.api.finance.EnterpriseBankServiceAPI;
import com.fintech.insurance.micro.dto.finance.EnterpriseBankVO;
import com.fintech.insurance.micro.finance.service.EnterpriseBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/26 14:16
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, path = "/finance/enterprisebank")
public class EnterpriseBankController implements EnterpriseBankServiceAPI {
    @Autowired
    private EnterpriseBankService enterpriseBankService;

    @GetMapping("/info")
    public FintechResponse<EnterpriseBankVO> getEnterpriseBank(@RequestParam(value = "applicationType") String applicationType,
                                                               @RequestParam("enterpriseBankCode") String enterpriseBankCode) {
        ApplicationType type = null;
        try {
            type = ApplicationType.codeOf(applicationType);
        } catch (Exception e) {
            throw new FInsuranceBaseException("105001", new Object[]{applicationType, ApplicationType.class.getSimpleName()});
        }
        return FintechResponse.responseData(enterpriseBankService.getByApplicationCodeAndEnterpriseBankCode(type, enterpriseBankCode));
    }
}
