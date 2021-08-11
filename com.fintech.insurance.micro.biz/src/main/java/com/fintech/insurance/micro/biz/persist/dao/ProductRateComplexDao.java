package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.micro.biz.persist.entity.ProductRate;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @Description: (产品利率管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
@NoRepositoryBean
public interface ProductRateComplexDao {
    public ProductRate getProductRateByProductIdAndDuration( Integer productId, Integer duration);
}
