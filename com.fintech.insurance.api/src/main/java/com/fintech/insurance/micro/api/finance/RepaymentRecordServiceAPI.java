package com.fintech.insurance.micro.api.finance;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.finance.RepaymentRecordVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 15:48
 */
public interface RepaymentRecordServiceAPI {

    @RequestMapping(path = "/get-repayment-record")
    FintechResponse<RepaymentRecordVO> getRepaymentRecord(@RequestParam(name = "repaymentRecordId", required = true) Integer repaymentRecordId);

}
