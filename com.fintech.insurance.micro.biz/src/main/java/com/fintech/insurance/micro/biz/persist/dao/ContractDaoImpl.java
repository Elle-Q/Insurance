package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.biz.persist.entity.Contract;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/13 0013 14:26
 */
public class ContractDaoImpl extends BaseEntityDaoImpl<Contract, Integer> implements ContractComplexDao {

    @Override
    public Page<Contract> pageContract(List<String> contractCodes, ContractStatus contractStatus, Date beginDate, Date endDate, int pageIndex, int pageSize) {
        StringBuilder hql = new StringBuilder(" from Contract c where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();

        if (contractCodes != null) {
            hql.append(" and c.contractNumber in :contractCodes");
            params.put("contractCodes", contractCodes);
        }

        if (contractStatus != null) {
            hql.append(" and c.contractStatus = :contractStatus ");
            params.put("contractStatus", contractStatus.getCode());
        }
        if (beginDate != null) {
            hql.append(" and c.startDate >= :beginDate");
            params.put("beginDate", beginDate);
        }
        if (endDate != null) {
            hql.append(" and c.endDate <= :endDate");
            params.put("endDate", endDate);
        }

        String countSql = "select count(c) " + hql.toString();
        hql.append(" order by c.createAt desc");
        // 按照同和状态排序
        String querySql = "select c " + hql.toString();
        return this.findPagination(querySql, countSql, params, pageIndex, pageSize);
    }

    @Override
    public Page<Contract> findAllContract(String channelCode, List<Integer> customerIds, Date startTime, Date endTime, ProductType type,
                                          Integer companyId, ContractStatus contractStatus, Integer pageIndex, Integer pageSize) {
        StringBuilder hql = new StringBuilder(" from Contract c where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();

        // 产品类型
        if (type != null) {
            hql.append(" and c.requisition.productType = :productType ");
            params.put("productType", type.getCode());
        }
        // 渠道编号
        if (StringUtils.isNotBlank(channelCode)) {
            hql.append(" and c.channel.channelCode = :channelCode");
            params.put("channelCode", channelCode);
        }
        //客户名称
        if (customerIds != null && customerIds.size() > 0) {
            hql.append(" and c.customerId in :customerId");
            params.put("customerId", customerIds);
        }
        //开始时间
        if (startTime != null) {
            hql.append(" and c.createAt >= :startTime");
            params.put("startTime", startTime);
        }
        //结束时间
        if (endTime != null) {
            hql.append(" and c.createAt <= :endTime");
            params.put("endTime", endTime);
        }
        if(companyId !=null ){
            hql.append(" and c.channel.organizationId = :companyId ");
            params.put("companyId", companyId);
        }
        //合同状态

        if (contractStatus != null) {
            hql.append(" and c.contractStatus = :contractStatus ");
            params.put("contractStatus", contractStatus.getCode());
        }
        hql.append(" and c.contractStatus != 'init' ");
        hql.append(" order by c.requisition.id,c.createAt desc ");
        String querySql = "select c " + hql.toString();
        String countSql = "select count(*) " + hql.toString();
        return this.findPagination(querySql, countSql,params, pageIndex, pageSize);
    }

    @Override
    public List<Contract> getByRequisitionNumberAndCustomerIds(String requisitionNumber, List<Integer> customerIds) {

        StringBuilder sb = new StringBuilder(" from Contract c where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.isNotBlank(requisitionNumber)) {
            sb.append(" and c.requisition.requisitionNumber = :requisitionNumber ");
            params.put("requisitionNumber",  requisitionNumber );
        }

        if (customerIds != null) {
            sb.append(" and c.requisition.customerId in :customerIds ");
            params.put("customerIds", customerIds);
        }

        return this.findEntityList("select c " + sb.toString(), Integer.MAX_VALUE, params);
    }

    @Override
    public Page<Contract> pageContractByCustomerIdAndContractStatus(Integer customerId, ContractStatus contractStatus, Integer pageIndex, Integer pageSize) {
        StringBuilder sb = new StringBuilder(" from Contract c where 1 = 1 ");
        Map<String, Object> map = new HashMap<>();
        if (customerId != null) {
            sb.append(" and customerId = :customerId ");
            map.put("customerId", customerId);
        }
        if (contractStatus != null) {
            sb.append(" and contractStatus = :contractStatus ");
            map.put("contractStatus", contractStatus.getCode());
        }
        sb.append(" and contractStatus != 'init' ");
        String countHql = "select count(c) " + sb.toString();
        sb.append(" order by c.requisition.id,c.createAt desc ");
        String queryHql = "select c" + sb.toString();
        return this.findEntityPagination(queryHql, countHql, map, pageIndex, pageSize);
    }

    @Override
    public Page<Contract> pageContractByNormalChannelUserIdAndContractStatus(Integer channelUserId, ContractStatus contractStatus, Integer pageIndex, Integer pageSize) {
        StringBuilder sb = new StringBuilder(" from Contract c where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (channelUserId != null) {
            sb.append(" and c.requisition.channelUserId = :channelUserId ");
            params.put("channelUserId", channelUserId);
        }
        if (contractStatus != null) {
            sb.append(" and contractStatus = :contractStatus ");
            params.put("contractStatus", contractStatus.getCode());
        }
        sb.append(" and contractStatus != 'init' ");
        String countHql = "select count(c) " + sb.toString();
        sb.append(" order by c.requisition.id,c.createAt desc ");
        String queryHql = "select c" + sb.toString();
        return this.findEntityPagination(queryHql, countHql, params, pageIndex, pageSize);
    }

    @Override
    public List<Contract> findByContractNumbers(List<String> contractNumbers) {
        StringBuilder sb = new StringBuilder(" from Contract c where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        sb.append(" and c.contractNumber in :contractNumbers ");
        params.put("contractNumbers", contractNumbers);

      return this.findEntityList(sb , 0, params);
    }

    @Override
    public String getLargestContractNumberByNumberPrefix(String contractNumberPrefix) {
        StringBuilder sb = new StringBuilder(" from Contract c where c.contractNumber like :contractNumberPrefix order by c.contractNumber desc");
        Map<String, Object> params = new HashMap<>();
        params.put("contractNumberPrefix", contractNumberPrefix + "%");
        Contract contract = this.findFirstEntity(sb, params);
        return contract == null? StringUtils.EMPTY : contract.getContractNumber();
    }
}
