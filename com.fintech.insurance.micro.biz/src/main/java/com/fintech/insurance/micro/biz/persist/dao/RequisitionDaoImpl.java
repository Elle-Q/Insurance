package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: qxy
 * @Date: 2017/11/16 0013 14:26
 */
public class RequisitionDaoImpl extends BaseEntityDaoImpl<Requisition, Integer> implements RequisitionComplexDao {

    @Override
    public Page<Requisition> queryRequisition(String requisitionNumber, RequisitionStatus requisitionStatus, ProductType productType, String channelName,
                                              Date beginDate, Date endDate, List<Integer> customerIds, Integer pageIndex, Integer pageSize) {
        StringBuilder hql = new StringBuilder(" from Requisition r where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(requisitionNumber)) {
            hql.append(" and r.requisitionNumber like :requisitionNumber");
            params.put("requisitionNumber", "%" + requisitionNumber + "%");
        }
        if (requisitionStatus != null) {
            hql.append(" and r.requisitionStatus = :requisitionStatus ");
            params.put("requisitionStatus", requisitionStatus.getCode());
        }

        if (productType != null) {
            hql.append(" and r.productType = :productType ");
            params.put("productType", productType.getCode());
        }
        if (StringUtils.isNotEmpty(channelName)) {
            hql.append(" and r.channel.channelName like :channelName ");
            params.put("channelName", "%" + channelName + "%");
        }
        if (beginDate != null) {
            hql.append(" and r.createAt >= :beginDate ");
            params.put("beginDate", beginDate);
        }
        if (endDate != null) {
            Date endTime = DateCommonUtils.getAfterDay(endDate, 1);
            hql.append(" and r.createAt <= :endTime ");
            params.put("endTime", endTime);
        }
       if (customerIds.size() > 0) {
            hql.append(" and r.customerId in (:customerIds) ");
           params.put("customerIds", customerIds);
       }

        String countSql = "select count(r) " + hql.toString();
        hql.append(" order by case r.requisitionStatus when 'failpayment' then 0 when 'auditing' then 1 else 2 end asc, r.createAt desc");
        String querySql = "select r " + hql.toString();
        return this.findPagination(querySql, countSql, params, pageIndex, pageSize);
    }

    @Override
    public Page<Requisition> queryRequisition(String requisitionNumber, String requisitionStatus, ProductType productType, String channelName, List<Integer> customerIds, Integer pageIndex, Integer pageSize) {
        StringBuilder hql = new StringBuilder(" from Requisition r where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNoneBlank(requisitionNumber)) {
            hql.append(" and r.requisitionNumber like :requisitionNumber");
            params.put("requisitionNumber", "%" + requisitionNumber + "%");
        }
        if (StringUtils.isNotBlank(requisitionStatus)) {
            hql.append(" and r.requisitionStatus = :requisitionStatus ");
            params.put("requisitionStatus", requisitionStatus);
        }

        if (productType != null) {
            hql.append(" and r.productType = :productType ");
            params.put("productType", productType.getCode());
        }
        if (StringUtils.isNotEmpty(channelName)) {
            hql.append(" and r.channel.channelName like :channelName ");
            params.put("channelName", "%" + channelName + "%");
        }
        if (customerIds.size() > 0) {
            hql.append(" and r.customerId in (:customerIds) ");
            params.put("customerIds", customerIds);
        }
        String countSql = "select count(r) " + hql.toString();
        hql.append(" order by r.auditSuccessTime desc ");
        String querySql = "select r " + hql.toString();
        return this.findPagination(querySql, countSql, params, pageIndex, pageSize);
    }

    @Override
    public Page<Requisition> pageRequisitionByCustomerId(List<Integer> customerIdInt, List<Integer> channelUserIdInt, String productType, String requisitionStatus, Integer pageIndex, Integer pageSize) {
        StringBuilder hql = new StringBuilder(" from Requisition r where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (customerIdInt.size() > 0) {
            hql.append(" and r.customerId in (:customerIds) and (r.isChannelApplication = false or (r.isChannelApplication = true and r.requisitionStatus != 'draft'))");
            params.put("customerIds", customerIdInt);
        }
        if (channelUserIdInt.size() > 0) {
            hql.append(" and r.channelUserId in (:channelUserIdInt) and (r.isChannelApplication = true or (r.isChannelApplication = false and r.requisitionStatus != 'draft'))");
            params.put("channelUserIdInt", channelUserIdInt);
        }
        if (StringUtils.isNotBlank(productType.trim())) {
            hql.append(" and r.productType = :productType");
            params.put("productType", productType);
        }
        if (StringUtils.isNotBlank(requisitionStatus.trim())) {
            hql.append(" and r.requisitionStatus = :requisitionStatus ");
            params.put("requisitionStatus", requisitionStatus);
        }
        String countSql = "select count(r) " + hql.toString();
        hql.append(" order by case r.requisitionStatus when 'draft' then 0 else 2 end asc, r.submissionDate desc");
        String querySql = "select r " + hql.toString();
        return this.findPagination(querySql, countSql, params, pageIndex, pageSize);
    }

    @Override
    public Requisition getAcquiescenceChannelByRequisitionIdAndCustomerId(Integer requisitionId, Integer customerId) {
        StringBuilder hql = new StringBuilder(" from Requisition r where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (requisitionId != null) {
            hql.append(" and r.id = :requisitionId ");
            params.put("requisitionId", requisitionId);
        }else if (customerId != null) {
            hql.append(" and r.id = (select max(t.id) from Requisition t where t.customerId = :customerId group by t.customerId ) ");
            params.put("customerId", customerId);
        }
        String querySql = "select r " + hql.toString();
        return this.findFirstEntity(querySql, params);
    }

    @Override
    public Integer countRequisitionByStatus(List<Integer> idInts, Integer customerId, String requisitionStatus) {
        StringBuilder hql = new StringBuilder(" from Requisition r where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (idInts.size() > 0) {
            hql.append(" and r.channelUserId in :idInts and (r.isChannelApplication = true or (r.isChannelApplication = false and r.requisitionStatus != 'draft')) ");
            params.put("idInts", idInts);
        }
        if (customerId != null) {
            hql.append(" and r.customerId = :customerId and (r.isChannelApplication = false or (r.isChannelApplication = true and r.requisitionStatus != 'draft') )");
            params.put("customerId", customerId);
        }
        if (StringUtils.isNotEmpty(requisitionStatus)) {
            hql.append(" and r.requisitionStatus = :requisitionStatus ");
            params.put("requisitionStatus", requisitionStatus);
        }
        String querySql = "select count(*) " + hql.toString();
        return Integer.parseInt(this.findFirstRecord(querySql, params).toString());
    }
}
