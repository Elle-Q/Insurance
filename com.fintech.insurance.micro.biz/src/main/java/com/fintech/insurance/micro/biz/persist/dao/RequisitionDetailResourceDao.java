package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetailResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: (业务详情资源)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
@Repository
@Transactional
public interface RequisitionDetailResourceDao extends JpaRepository<RequisitionDetailResource, Integer>, BaseEntityDao<RequisitionDetailResource, Integer>, RequisitionDetailResourceComplexDao {

    @Query("select r from RequisitionDetailResource r where  r.id = :id ")
    RequisitionDetailResource getRequisitionDetailResourceById(@Param("id") Integer id);

    @Query("select r from RequisitionDetailResource r where  r.requisitionDetail.id = :requisitionDetailId ")
    List<RequisitionDetailResource> getByRequisitionDetailId(@Param("requisitionDetailId") Integer requisitionDetailId);
}
