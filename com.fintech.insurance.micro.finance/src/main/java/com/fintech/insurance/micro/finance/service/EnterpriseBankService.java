package com.fintech.insurance.micro.finance.service;

import com.fintech.insurance.commons.enums.ApplicationType;
import com.fintech.insurance.micro.dto.finance.EnterpriseBankVO;

/**
 * @Author: Clayburn
 * @Description: 企业银行信息
 * @Date: 2017/12/26 14:02
 */
public interface EnterpriseBankService {
    /**
     * 通过应用类型和银行编码返回银行信息
     * @param applicationType
     * @param enterpriseBankCode
     * @return
     */
    EnterpriseBankVO getByApplicationCodeAndEnterpriseBankCode(ApplicationType applicationType, String enterpriseBankCode);
}
