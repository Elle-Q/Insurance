package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description: (业务管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
@Repository
@Transactional
public interface RequisitionDao extends JpaRepository<Requisition, Integer>, BaseEntityDao<Requisition, Integer>, RequisitionComplexDao {
    @Query("select r from Requisition r where  r.id = :id ")
    Requisition getRequisitionById(@Param("id") Integer id);

    @Query("select r from Requisition r where  r.requisitionNumber = :requisitionNumber ")
    Requisition getRequisitionByRequisitionNumber(@Param("requisitionNumber") String requisitionNumber);

    @Query("select r from Requisition r where  r.paymentOrderNumber = :paymentOrderNumber ")
    Requisition getRequisitionByPaymentOrderNumber(@Param("paymentOrderNumber") String paymentOrderNumber);

    @Query("select r from Requisition r where  r.requisitionStatus = :requisitionStatus ")
    List<Requisition> listRequisitionByStatus(@Param("requisitionStatus") String requisitionStatus);

    @Query("select r from Requisition r where  r.requisitionStatus in (:requisitionStatus) and  r.auditSuccessTime <= :preEndDay ")
    List<Requisition> listRequisitionForWaitingpayment(@Param("requisitionStatus") List<String> requisitionStatus, @Param("preEndDay") Date preEndDay);

    @Query("select c.requisition from Contract c where  c.contractNumber = :contractNumber ")
    Requisition getRequisitionByContractNumber( @Param("contractNumber") String contractNumber);

    @Query("select r from Requisition r where  r.requisitionStatus in (:statuses) ")
    List<Requisition> listByStatus(@Param("statuses") List<String> statuses);
}
