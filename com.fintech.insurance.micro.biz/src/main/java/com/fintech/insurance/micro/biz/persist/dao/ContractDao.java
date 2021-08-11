package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.biz.persist.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: (合同管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
@Repository
@Transactional
public interface ContractDao extends JpaRepository<Contract, Integer>, BaseEntityDao<Contract, Integer>, ContractComplexDao {

    @Query("select c from Contract c where  c.id = :id ")
    Contract getContractById(@Param("id") Integer id);

    @Query("select c from Contract c where  c.contractNumber = :contractNumber ")
    Contract getContractInfoByContractNo(@Param("contractNumber") String contractNumber);

    @Query("select c from Contract c where  c.requisition.id = :id order by c.businessDuration,c.createAt asc ")
    List<Contract> listByRequisitionId(@Param("id") Integer id);

    Page<Contract> getByRequisition_Id(Integer requisitionId, Pageable pageable);

    Page<Contract> getByRequisition_IdOrderByBusinessDurationAsc(Integer requisitionId, Pageable pageable);

    @Query("select c.requisition.overdueFineRate from Contract c where  c.contractNumber = :contractNumber ")
    Double getOverdueFineRateByContractNo(@Param("contractNumber") String contractNumber);

}
