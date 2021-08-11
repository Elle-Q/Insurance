package com.fintech.insurance.micro.customer.persist.dao;

import com.fintech.insurance.micro.customer.persist.entity.CustomerBankCard;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/14 13:36
 */
@NoRepositoryBean
public interface CustomerBankCardComplexDao {
    /**
     * 根据指定的用户已经银行卡号获取用户曾经绑定过的银行卡信息
     * @param customerId 指定客户
     * @param accountNumber 银行卡卡号
     * @return
     */
    CustomerBankCard findCustomerBankCardByCustomerIdAndCardNumber(Integer customerId, String accountNumber);
}
