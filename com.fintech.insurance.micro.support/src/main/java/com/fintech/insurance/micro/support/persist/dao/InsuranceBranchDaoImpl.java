package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.support.persist.entity.InsuranceBranch;
import org.springframework.stereotype.Repository;

@Repository
public class InsuranceBranchDaoImpl extends BaseEntityDaoImpl<InsuranceBranch, Integer> implements InsuranceBranchComplexDao {
}
