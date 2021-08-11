package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.finance.persist.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: qxy
 * @Description:
 * @Date: 2017/11/18 15:52
 */
@Repository
public interface PaymentOrderDao extends JpaRepository<PaymentOrder, Integer>, BaseEntityDao<PaymentOrder, Integer>, PaymentOrderComplexDao {

    @Query("select p from PaymentOrder p where  p.orderNumber = :requisitionNumber ")
    PaymentOrder getByOrderNumber(@Param("requisitionNumber") String requisitionNumber);


    @Query("select p from PaymentOrder p where p.paymentStatus = :status")
    List<PaymentOrder> getPaymentOrderListByPaymentStatus(@Param("status")String status);

    @Query("select p from PaymentOrder p where p.transactionSerial = :transactionSerial")
    PaymentOrder getByTransactionSerial(@Param("transactionSerial")String transactionSerial);
}
