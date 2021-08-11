package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.finance.persist.entity.AccountVoucher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/18 15:49
 */
@Repository
public class AccountVoucherDaoImpl extends BaseEntityDaoImpl<AccountVoucher, Integer> implements AccountVoucherComplexDao {
    @Override
    public AccountVoucher getByUserAndCodeAndType(Integer userId, String requisitionNumber, String accountType) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("from AccountVoucher where 1 = 1");

        if (StringUtils.isNotEmpty(requisitionNumber)) {
            sql.append(" and requisitionCode = :requisitionNumber");
            paramsMap.put("requisitionNumber", requisitionNumber);
        }
        if (null != userId) {
            sql.append(" and userId = :userId");
            paramsMap.put("userId", userId);
        }
        if (StringUtils.isNotEmpty(accountType)) {
            sql.append(" and accountType = :accountType");
            paramsMap.put("accountType", accountType);
        }

        return this.findFirstEntity(sql.toString(), paramsMap);
    }
}
