package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ResourceType;
import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetailResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/13 0013 14:26
 */
public class RequisitionDetailResourceDaoImpl extends BaseEntityDaoImpl<RequisitionDetailResource, Integer> implements RequisitionDetailResourceComplexDao {

    @Override
    public List<RequisitionDetailResource> listRequisitionDetailResource(Integer requisitionDetailId, ResourceType resourceType) {
        StringBuilder hql = new StringBuilder(" from RequisitionDetailResource r where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (requisitionDetailId != null) {
            hql.append(" and r.requisitionDetail.id = :requisitionDetailId");
            params.put("requisitionDetailId", requisitionDetailId);
        }
        if (resourceType != null) {
            hql.append(" and r.resourceType = :resourceType ");
            params.put("resourceType", resourceType.getCode());
        }
        hql.append(" order by r.resourceType,r.displaySequence ");
        String querySql = "select r " + hql.toString();
        return this.findEntityList(querySql, 0, params);
    }
}
