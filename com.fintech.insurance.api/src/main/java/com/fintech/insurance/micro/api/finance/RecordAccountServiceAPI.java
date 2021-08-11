package com.fintech.insurance.micro.api.finance;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.RecordVO;
import com.fintech.insurance.micro.dto.finance.VoucherVO;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: Clayburn
 * @Description: 用于上传财务相关的图片凭证
 * @Date: 2017/11/20 11:11
 */
@RequestMapping(value = "/finance/account", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface RecordAccountServiceAPI {
    @RequestMapping(value = "/record", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> recordAccount(@RequestBody @Validated VoucherVO voucher);

    @RequestMapping(value = "/item", method = RequestMethod.GET)
    RecordVO getRecord(@RequestParam(value = "requisitionCode", defaultValue = "") String requisitionCode);
}
