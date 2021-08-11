package com.fintech.insurance.micro.support.service;

import com.fintech.insurance.micro.dto.support.BankInfoVO;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/15 14:13
 */
public interface BankInfoService {
    /**
     * 获取所有银行的信息
     *
     * @return
     */
    List<BankInfoVO> listAllBankInfo();
}
