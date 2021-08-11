package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.customer.persist.entity.CustomerBankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/13 15:15
 */
@Repository
public interface CustomerBankCardDao extends JpaRepository<CustomerBankCard, Integer>,
        BaseEntityDao<CustomerBankCard, Integer>, CustomerBankCardComplexDao {

    @Query("select c from CustomerBankCard c where c.customerAccount.id = :customerId and c.disableFlag = 0")
    List<CustomerBankCard> findCustomerBankCardByCustomerId(@Param("customerId") Integer customerId);

}
