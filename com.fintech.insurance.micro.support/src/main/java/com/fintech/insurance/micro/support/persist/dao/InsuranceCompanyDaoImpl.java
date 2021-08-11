package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.support.persist.entity.InsuranceCompany;
import org.springframework.stereotype.Repository;

@Repository
public class InsuranceCompanyDaoImpl extends BaseEntityDaoImpl<InsuranceCompany, Integer> implements InsuranceCompanyComplexDao {
}
