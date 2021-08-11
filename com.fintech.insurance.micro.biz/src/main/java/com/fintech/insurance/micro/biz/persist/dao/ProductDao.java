package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.biz.persist.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: (产品管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */

@Repository
@Transactional
public interface ProductDao extends JpaRepository<Product, Integer>, BaseEntityDao<Product, Integer>, ProductComplexDao {
    @Query("select p from Product p where  p.id = :id ")
    public Product getProductById(@Param("id") Integer id);

    @Query("select count(p) from Product p where  p.productType = :productType ")
    public Long countProductByProductType(@Param("productType") String productType);

    @Query("select p from Product p where  p.productName = :productName ")
    public List<Product> findProductByProductName(@Param("productName") String productName);

    @Query("select p from Product p where  p.productCode = :productCode ")
    public List<Product> findProductByProductCode(@Param("productCode") String productCode);
}
