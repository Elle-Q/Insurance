package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.support.persist.entity.InsuranceBranch;
import com.fintech.insurance.micro.support.persist.entity.InsuranceCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceCompanyDao extends JpaRepository<InsuranceCompany, Integer>, BaseEntityDao<InsuranceCompany, Integer>, InsuranceCompanyComplexDao {
    List<InsuranceCompany> findAllByDisabledFlagFalse();

    @Query("select i from InsuranceCompany i where i.accountNumber = :loanAccountNumber")
    InsuranceCompany getByAccountNumber(@Param(value = "loanAccountNumber") String loanAccountNumber);
}
