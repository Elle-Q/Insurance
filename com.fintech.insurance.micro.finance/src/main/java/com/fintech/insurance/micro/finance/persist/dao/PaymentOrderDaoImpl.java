package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.finance.persist.entity.PaymentOrder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/18 15:54
 */
@Repository
public class PaymentOrderDaoImpl extends BaseEntityDaoImpl<PaymentOrder, Integer> implements PaymentOrderComplexDao {
    @Override
    public List<PaymentOrder> findPaymentOrderByDebtStatus(List<String> debtStatusList) {
        StringBuilder sub = new StringBuilder(" from PaymentOrder p where p.paymentStatus in :paymentStatus ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("paymentStatus", debtStatusList);
        return this.findEntityList(sub, 0, map);
    }
}
