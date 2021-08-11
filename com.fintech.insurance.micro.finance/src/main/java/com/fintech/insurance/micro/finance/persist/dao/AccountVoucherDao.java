package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.finance.persist.entity.AccountVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/18 15:48
 */
@Repository
public interface AccountVoucherDao extends JpaRepository<AccountVoucher, Integer>, BaseEntityDao<AccountVoucher, Integer>, AccountVoucherComplexDao {
    AccountVoucher getFirstByRequisitionCodeAndAccountType(String requisitionCode, String accountType);

}
