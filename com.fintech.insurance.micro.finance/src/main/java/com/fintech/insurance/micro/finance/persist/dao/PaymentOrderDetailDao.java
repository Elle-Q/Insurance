package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.finance.persist.entity.PaymentOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/18 15:55
 */@Repository
public interface PaymentOrderDetailDao extends JpaRepository<PaymentOrderDetail, Integer>,
        BaseEntityDao<PaymentOrderDetail, Integer>, PaymentOrderDetailComplexDao {

    @Query("select p from PaymentOrderDetail p where p.paymentOrder.id = :id")
    List<PaymentOrderDetail> listByOrderId(@Param(value = "id") Integer id);
}
