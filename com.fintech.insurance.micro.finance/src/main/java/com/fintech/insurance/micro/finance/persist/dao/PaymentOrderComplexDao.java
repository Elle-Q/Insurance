package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.micro.finance.persist.entity.PaymentOrder;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/18 15:52
 */
@NoRepositoryBean
public interface PaymentOrderComplexDao {

    /**
     * 查询扣款状态为处理中的支付单
     * @param debtStatusList
     * @return
     */
    List<PaymentOrder> findPaymentOrderByDebtStatus(List<String> debtStatusList);
}
