package com.fintech.insurance.micro.finance.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.finance.RecordAccountServiceAPI;
import com.fintech.insurance.micro.dto.biz.RecordVO;
import com.fintech.insurance.micro.dto.finance.VoucherVO;
import com.fintech.insurance.micro.finance.service.RecordAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Clayburn
 * @Description:  用于上传入账出账的收据，图片等信息
 * @Date: 2017/11/20 11:10
 */
@RestController
public class RecordAccountController extends BaseFintechController implements RecordAccountServiceAPI {
    @Autowired
    private RecordAccountService recordAccountService;

    @Override
    public FintechResponse<VoidPlaceHolder> recordAccount(@RequestBody @Validated VoucherVO voucher) {
        recordAccountService.recordAccount(voucher);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public RecordVO getRecord(@RequestParam(value = "requisitionCode", defaultValue = "") String requisitionCode) {
        return recordAccountService.getRecord(requisitionCode);
    }
}
