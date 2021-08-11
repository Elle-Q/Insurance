package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.micro.finance.persist.entity.AccountVoucher;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/18 15:48
 */
@NoRepositoryBean
public interface AccountVoucherComplexDao {
    AccountVoucher getByUserAndCodeAndType(Integer userId, String requisitionNumber, String accountType);
}
