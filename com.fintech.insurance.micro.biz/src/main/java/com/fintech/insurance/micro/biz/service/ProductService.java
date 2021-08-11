package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.biz.persist.entity.Product;
import com.fintech.insurance.micro.biz.persist.entity.ProductRate;
import com.fintech.insurance.micro.dto.biz.ProductVO;
import com.fintech.insurance.micro.dto.biz.SimpleProductVO;

import java.util.Map;

/**
 * @Description: (产品管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
public interface ProductService {

    //获取产品详情
    public ProductVO getProductVOById(Integer id);

    //保存产品 返回产品
    public Integer save(ProductVO productVO);

    //上架
    public void onShelves(ProductVO productVO);

    //下架
    public void downShelves(ProductVO productVO);

    //产品查询
    public Pagination<ProductVO> queryProduct(String productType, String productName, String repayDayType,
                                              Integer isOnsale, int pageIndex, int pageSize);

    /**
     * 将产品利率配置转换为MAP数据类型输出
     * @param product
     * @return
     */
    public Map<String, ProductRate> getProductRate(Product product);

    /**
     * WX获取渠道产品列表
     * @param channelId 渠道id
     * @param productType 产品类型
     * @param pageIndex 页数
     * @param pageSize 每页数
     * @return
     */
    Pagination<SimpleProductVO> queryWeChatProductInfoByChannelId( Integer channelId, String productType, Integer pageIndex, Integer pageSize);
}
