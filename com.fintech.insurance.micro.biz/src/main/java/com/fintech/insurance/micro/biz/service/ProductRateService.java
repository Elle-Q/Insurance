package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.micro.biz.persist.entity.Product;
import com.fintech.insurance.micro.biz.persist.entity.ProductRate;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import com.fintech.insurance.micro.dto.biz.ProductRateVO;

import java.util.List;
import java.util.Set;

/**
 * @Description: (产品利率管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
public interface ProductRateService {

    //获取产品利率详情
    public ProductRateVO getProductRateVOById(Integer id);

    //保存产品利率
    public void save(Product product, List<ProductRateVO> productRateVOList);

    //根据产品id获取产品利率详情
    public List<ProductRateVO> findProductRateVOByProductId(Integer productId);

    /**
     * 获取产品利率
     * @param requisitionDetail 车辆信息
     * @param month 贷款月份
     * @return
     */
    public ProductRate getProductRate(RequisitionDetail requisitionDetail, Integer month);
}
