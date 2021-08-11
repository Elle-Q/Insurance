package com.fintech.insurance.micro.api.finance;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.finance.EnterpriseBankVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/26 14:23
 */
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, path = "/finance/enterprisebank")
public interface EnterpriseBankServiceAPI {
    /**
     * 根据应用类型和银行编码获取银行信息
     * @param applicationType
     * @param enterpriseBankCode
     * @return
     */
    @GetMapping("/info")
    FintechResponse<EnterpriseBankVO> getEnterpriseBank(@RequestParam(value = "applicationType") String applicationType,
                                                               @RequestParam("enterpriseBankCode") String enterpriseBankCode);
}
