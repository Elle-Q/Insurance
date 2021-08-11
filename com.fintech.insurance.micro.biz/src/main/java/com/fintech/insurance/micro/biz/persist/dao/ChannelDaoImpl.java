package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.biz.persist.entity.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/13 0013 14:26
 */
public class ChannelDaoImpl extends BaseEntityDaoImpl<Channel, Integer> implements ChannelComplexDao {

    @Override
    public Page<Channel> queryChannel(String channelCode, String channelName,List<Integer> companyIds, List<String> channelCodes, int pageIndex, int pageSize) {
        StringBuilder hql = new StringBuilder(" from Channel c where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(channelCode)) {
            hql.append(" and c.channelCode like :channelCode");
            params.put("channelCode", "%" + channelCode + "%");
        }
        if (StringUtils.isNotEmpty(channelName)) {
            hql.append(" and c.channelName like :channelName ");
            params.put("channelName", "%" + channelName + "%");
        }
        if (companyIds.size() > 0) {
            hql.append(" and c.organizationId in (:companyIds) ");
            params.put("companyIds", companyIds);
        }
        if (channelCodes.size() > 0) {
            hql.append(" and c.channelCode in (:channelCodes) ");
            params.put("channelCodes", channelCodes);
        }
        String countSql = "select count(c) " + hql.toString();
        hql.append(" order by c.createAt desc");
        String querySql = "select c " + hql.toString();
        return this.findPagination(querySql, countSql, params, pageIndex, pageSize);
    }

    @Override
    public List<Channel> queryChannelByProductIdAndBelongFlag(Integer productId, boolean belongFlag) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        if (belongFlag) {
            hql.append("from Channel c join c.productSet p where 1=1 ");
            if(productId != null) {
                hql.append(" and p.id = :productId ");
                params.put("productId", productId);
            }
        }else{
            hql.append("from Channel c where 1=1 ");
            if(productId != null) {
               hql.append(" and not exists (select 1 from Channel t1 left join t1.productSet t2 where t2.id = :productId and t1.id = c.id ) ");
                params.put("productId", productId);
            }
        }
        hql.append(" order by c.createAt desc");
        String querySql = "select c " + hql.toString();
        return this.findEntityList(querySql, 0, params);
    }

    @Override
    public List<Channel> findChannelVOByIds(List<Integer> ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder("from Channel c where 1=1 ");
        if (ids !=null && ids.size() > 0 ) {
            hql.append(" and c.id in :ids ");
            params.put("ids", ids);
        }
        hql.append(" order by c.createAt desc");
        String querySql = "select c " + hql.toString();
        return this.findEntityList(querySql, 0, params);
    }

    @Override
    public Page<Channel> findChannelByChannelCodes(List<String> codes, Integer pageIndex, Integer pageSize) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder("from Channel c where c.isLocked=false ");
        if (codes !=null && codes.size() > 0 ) {
            hql.append(" and c.channelCode in :channelCode ");
            params.put("channelCode", codes);
        }
        String countSql = "select count(c) " + hql.toString();
        hql.append(" order by c.createAt desc");
        String querySql = "select c " + hql.toString();
        return this.findEntityPagination(querySql, countSql, params, pageIndex, pageSize);
    }
}
