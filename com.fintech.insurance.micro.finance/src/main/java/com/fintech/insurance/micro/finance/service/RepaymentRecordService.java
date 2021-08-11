package com.fintech.insurance.micro.finance.service;

import com.fintech.insurance.micro.dto.finance.RepaymentRecordVO;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 15:42
 */
public interface RepaymentRecordService {

    RepaymentRecordVO getRepaymentRecord(Integer repaymentRecordId);

    String getRepaymentBankcardByDebtOrder(String debtOrderNO);

    void changeRepaymentRecordStatusFromYJF();
}
