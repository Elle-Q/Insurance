package com.fintech.insurance.micro.api.support;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.support.BankInfoVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping(value = "/support/bankinfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface BankInfoServiceAPI {
    /**
     * 查询所又银行信息
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    FintechResponse<List<BankInfoVO>> listAllBankInfo();
}
