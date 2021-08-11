package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.support.persist.entity.InsuranceBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceBranchDao extends JpaRepository<InsuranceBranch, Integer>, BaseEntityDao<InsuranceBranch, Integer>, InsuranceBranchComplexDao {
    @Query("select i from InsuranceBranch i where i.accountNumber = :loanAccountNumber")
    InsuranceBranch getByAccountNumber(@Param(value = "loanAccountNumber") String loanAccountNumber);
}
