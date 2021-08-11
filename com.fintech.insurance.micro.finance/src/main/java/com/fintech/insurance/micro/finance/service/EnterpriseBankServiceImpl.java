package com.fintech.insurance.micro.finance.service;

import com.fintech.insurance.commons.enums.ApplicationType;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.dto.finance.EnterpriseBankVO;
import com.fintech.insurance.micro.finance.persist.dao.EnterpriseBankDao;
import com.fintech.insurance.micro.finance.persist.entity.EnterpriseBank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/26 14:08
 */
@Service
public class EnterpriseBankServiceImpl implements EnterpriseBankService {
    @Autowired
    private EnterpriseBankDao enterpriseBankDao;

    @Override
    @Transactional(readOnly = true)
    public EnterpriseBankVO getByApplicationCodeAndEnterpriseBankCode(ApplicationType applicationType, String enterpriseBankCode) {
        EnterpriseBank enterpriseBank = enterpriseBankDao.getByApplicationCodeAndEnterpriseBankCode(applicationType.getCode(), enterpriseBankCode);

        if (enterpriseBank == null) {
            return null;
        }

        return entityToVO(enterpriseBank);
    }

    private EnterpriseBankVO entityToVO(EnterpriseBank enterpriseBank) {
        if (enterpriseBank == null) {
            return null;
        }
        EnterpriseBankVO vo = new EnterpriseBankVO();
        vo.setEnterpriseCode(enterpriseBank.getEnterpriseCode());
        vo.setApplicationCode(enterpriseBank.getApplicationCode());
        vo.setAppBankCode(enterpriseBank.getAppBankCode());
        vo.setEnterpriseBankCode(enterpriseBank.getEnterpriseBankCode());
        vo.setBankName(enterpriseBank.getBankName());
        vo.setRemark(enterpriseBank.getRemark());
        if (null != enterpriseBank.getSingleLimit()) {
            vo.setSingleLimit(enterpriseBank.getSingleLimit().doubleValue());
        }
        if (null != enterpriseBank.getDailyLimit()) {
            vo.setDailyLimit(enterpriseBank.getDailyLimit().doubleValue());
        }
        return vo;
    }
}
