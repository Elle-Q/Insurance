package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.components.persist.BaseDao;
import com.fintech.insurance.micro.biz.persist.entity.ProductRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: (产品利率管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */

@Repository
@Transactional
public interface ProductRateDao extends JpaRepository<ProductRate, Integer>, BaseDao<ProductRate>, ProductRateComplexDao {
    @Query("select p from ProductRate p where  p.id = :id ")
    public ProductRate getProductRateById(@Param("id") Integer id);

    @Query("select p from ProductRate p where  p.product.id = :productId ")
    public List<ProductRate> findAllProductRateByProductId(@Param("productId") Integer productId);

}
