package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RepayDayType;
import com.fintech.insurance.micro.biz.persist.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @Description: (产品管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
@NoRepositoryBean
public interface ProductComplexDao {
    /**
     * 产品分页查询
     * @param productType 产品类型
     * @param productName 产品名称
     * @param repayDayType 还款类型
     * @param isOnsale 是否上架
     * @param pageIndex 页数
     * @param pageSize 每页数
     * @return
     */
    public Page<Product> query(ProductType productType, String productName, RepayDayType repayDayType, Boolean isOnsale, int pageIndex, int pageSize);

    /**
     * 通过渠道查询产品列表
     * @param channelId 渠道id
     * @param productType 产品类型
     * @param pageIndex 页数
     * @param pageSize 每页数
     * @return
     */
    Page<Product> queryWeChatProductInfoByChannelId(Integer channelId, String productType, Integer pageIndex, Integer pageSize);
}
