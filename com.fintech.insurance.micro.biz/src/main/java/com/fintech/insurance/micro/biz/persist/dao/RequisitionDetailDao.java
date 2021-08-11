package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: (业务详情)
 * @Author: qxy
 * @Date: 2017/11/13 10:28
 */
@Repository
@Transactional
public interface RequisitionDetailDao extends JpaRepository<RequisitionDetail, Integer>, BaseEntityDao<RequisitionDetail, Integer>, RequisitionDetailComplexDao {

    @Query("select r from RequisitionDetail r where  r.id = :id ")
    RequisitionDetail getRequisitionDetailById(@Param("id") Integer id);

    @Query("select r from RequisitionDetail r where r.requisition.id = :id ")
    List<RequisitionDetail> getRequisitionDetailByRequisition_Id(@Param("id") Integer id);

    List<RequisitionDetail> getByContract_Id(@Param("contractId") Integer contractId);

    RequisitionDetail getByRequisition_IdAndContract_Id(Integer requisitionId, Integer contractId);
}
