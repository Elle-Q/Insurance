package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.finance.persist.entity.PaymentOrder;
import com.fintech.insurance.micro.finance.persist.entity.PaymentOrderDetail;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/18 15:57
 */
@Repository
public class PaymentOrderDetailDaoImpl extends BaseEntityDaoImpl<PaymentOrderDetail, Integer> implements PaymentOrderDetailComplexDao {
}
