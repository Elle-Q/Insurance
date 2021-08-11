package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.components.persist.BaseDaoImpl;
import com.fintech.insurance.micro.biz.persist.entity.ProductRate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/13 0013 14:26
 */
public class ProductRateDaoImpl extends BaseDaoImpl<ProductRate, Integer> implements ProductRateComplexDao {
    @Override
    public ProductRate getProductRateByProductIdAndDuration(Integer productId, Integer duration) {
        StringBuilder hql =new StringBuilder("select p from ProductRate p where  p.product.id = :productId and p.businessDuration = :businessDuration ");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("productId", productId);
        map.put("businessDuration", duration);
        return findFirstEntity(hql, map);
    }
}
