package com.fintech.insurance.micro.support.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.support.BankInfoServiceAPI;
import com.fintech.insurance.micro.dto.support.BankInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description: 银行信息管理
 * @Date: 2017/11/9 15:21
 */
@RestController
public class BankInfoController extends BaseFintechController implements BankInfoServiceAPI {

    @Autowired
    private com.fintech.insurance.micro.support.service.BankInfoService bankInfoService;

    @Override
    public FintechResponse<List<BankInfoVO>> listAllBankInfo() {
        return FintechResponse.responseData(bankInfoService.listAllBankInfo());
    }
}
