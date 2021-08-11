package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RepayDayType;
import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.biz.persist.entity.Product;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/13 0013 14:26
 */

@Repository
public class ProductDaoImpl extends BaseEntityDaoImpl<Product, Integer> implements ProductComplexDao {
    @Override
    public Page<Product> query(ProductType productType, String productName, RepayDayType repayDayType, Boolean isOnsale, int pageIndex, int pageSize) {
        StringBuilder hql = new StringBuilder(" from Product p where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (productType != null) {
            hql.append(" and p.productType = :productType");
            params.put("productType", productType.getCode());
        }
        if (StringUtils.isNotEmpty(productName)) {
            hql.append(" and p.productName like :productName");
            params.put("productName", "%" + productName + "%");
        }
        if (isOnsale != null) {
            hql.append(" and p.isOnsale = :isOnsale");
            params.put("isOnsale", isOnsale);
        }
        if (repayDayType != null) {
            hql.append(" and p.repayDayType = :repayDayType");
            params.put("repayDayType", repayDayType.getCode());
        }
        String countSql = "select count(p) " + hql.toString();
        hql.append(" order by p.createAt desc");
        String querySql = "select p " + hql.toString();
        return this.findPagination(querySql, countSql, params, pageIndex, pageSize);
    }

    @Override
    public Page<Product> queryWeChatProductInfoByChannelId(Integer channelId, String productType, Integer pageIndex, Integer pageSize) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" from Product p join p.channelSet c where p.isOnsale = true ");
        if(channelId != null) {
            hql.append(" and c.id = :channelId ");
            params.put("channelId", channelId);
        }
        if(StringUtils.isNoneBlank(productType)) {
            hql.append(" and p.productType = :productType ");
            params.put("productType", productType);
        }
        String countSql = "select count(p) " + hql.toString();
        hql.append(" order by case when p.productType = 'policy_loans' then 1 when p.productType = 'car_instalments' then 2 end, p.createAt asc");
        String querySql = "select p " + hql.toString();
        return this.findPagination(querySql, countSql, params, pageIndex, pageSize);
    }
}
