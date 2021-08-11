package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.customer.persist.entity.CustomerAccountInfo;
import org.springframework.data.domain.Page;
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
public interface CustomerAccountInfoDao extends JpaRepository<CustomerAccountInfo, Integer>,
        BaseEntityDao<CustomerAccountInfo, Integer>, CustomerAccountInfoComplexDao {

    @Query("select c from CustomerAccountInfo c where c.customerAccount.id = :customerId and c.channelCode = :channelCode order by c.createAt desc")
    List<CustomerAccountInfo> findByCustomerIdAndChannelCode(@Param("customerId") Integer customerId, @Param("channelCode") String channelCode);

    @Query("select c from CustomerAccountInfo c where c.customerName like :customerName")
    List<CustomerAccountInfo> listByCustomerNameLike(@Param("customerName") String customerName);

    @Query("select c from CustomerAccountInfo c where c.customerMobile = :phone")
    CustomerAccountInfo getByMobile(@Param("phone") String phone);

    CustomerAccountInfo getByCustomerAccount_Id(Integer id);

    @Query("select c.channelCode from CustomerAccountInfo c where c.customerAccount.id = :customerId order by c.id ")
    List<String> findCustomerChannelCodeByCustomerId(@Param("customerId") Integer customerId);

    @Query("select c from CustomerAccountInfo c where c.customerMobile = :phone and c.channelCode = :channelOf")
    CustomerAccountInfo getByMobileAndChannelCode(@Param("phone")String phone, @Param("channelOf") String channelOf);

    @Query("select c from CustomerAccountInfo c where c.customerAccount.id = :customerAccountId order by c.id ")
    List<CustomerAccountInfo> listByAccountId(@Param("customerAccountId") Integer customerAccountId);

    @Query("select c from CustomerAccountInfo c where c.customerMobile = :phoneNumber order by c.id ")
    List<CustomerAccountInfo> listCustomerAccountInfoByMobile(@Param("phoneNumber") String phoneNumber);

    @Query("select c from CustomerAccountInfo c where c.accountNumber = :bankCard and c.channelUserId = :channelUserId")
    CustomerAccountInfo getByBankNumAndChannelUserId(@Param("bankCard") String bankCard, @Param("channelUserId") Integer channelUserId);
}
