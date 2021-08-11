package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccount;
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
public interface CustomerAccountDao extends JpaRepository<CustomerAccount, Integer>,
        BaseEntityDao<CustomerAccount, Integer>, CustomerAccountComplexDao {

    /**
     * 根据用户id查询用户渠道编号集合
     * @param customerId
     * @return
     */
    @Query("select c.channelCode from CustomerAccountInfo c where c.customerAccount.id = :customerId")
    public List<String> findCustomerChannelCodeByCustomerId(@Param("customerId") Integer customerId);

    /**
     * 根据身份证号查询用户
     * @param idNumber
     * @return
     */
    @Query("select c from CustomerAccount c where c.idNumber = :idNumber")
    CustomerAccount getCustomerAccountByIdNumber(@Param("idNumber") String idNumber);

    /**
     * 根据客户id查询客户冻结状态
     * @param id 客户id
     * @return
     */
    @Query("select c.lockedTag from CustomerAccount c where c.id = :id")
    Boolean getCustomerLockedStatusById(@Param("id") Integer id);
}
