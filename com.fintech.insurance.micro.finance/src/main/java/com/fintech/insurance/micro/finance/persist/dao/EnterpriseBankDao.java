package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.micro.finance.persist.entity.EnterpriseBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/26 11:55
 */
@Repository
public interface EnterpriseBankDao extends JpaRepository<EnterpriseBank, Integer>{
    EnterpriseBank getByApplicationCodeAndEnterpriseBankCode(String applicationCode, String enterpriseBankCode);
}