package com.fintech.insurance.micro.finance.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.DebtStatus;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.finance.RepaymentRecordServiceAPI;
import com.fintech.insurance.micro.dto.finance.RepaymentRecordVO;
import com.fintech.insurance.micro.finance.service.RepaymentRecordService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 15:51
 */
public class RepaymentRecordController extends BaseFintechController implements RepaymentRecordServiceAPI {

    private RepaymentRecordService repaymentRecordService;

    @Override
    public FintechResponse<RepaymentRecordVO> getRepaymentRecord(Integer repaymentRecordId) {

        RepaymentRecordVO repaymentRecordVO = this.repaymentRecordService.getRepaymentRecord(repaymentRecordId);
        return FintechResponse.responseData(repaymentRecordVO);
    }

}
