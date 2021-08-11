package com.fintech.insurance.micro.finance.service;

import com.fintech.insurance.micro.dto.biz.RecordVO;
import com.fintech.insurance.micro.dto.finance.VoucherVO;

/**
 * @Author: Clayburn
 * @Description: 入账，出账记录
 * @Date: 2017/11/20 10:54
 */
public interface RecordAccountService {
    void recordAccount(VoucherVO voucherVO);

    RecordVO getRecord(String requisitionCode);
}
