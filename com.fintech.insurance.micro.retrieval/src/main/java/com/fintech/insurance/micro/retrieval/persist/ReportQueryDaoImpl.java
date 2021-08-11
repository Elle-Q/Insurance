package com.fintech.insurance.micro.retrieval.persist;

import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.biz.BizReportVO;
import com.fintech.insurance.micro.retrieval.mapper.BizReportMapper;
import com.fintech.insurance.micro.retrieval.persist.base.BaseNativeSQLDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/30 18:41
 */
@Repository
@Transactional(readOnly = true)
public class ReportQueryDaoImpl extends BaseNativeSQLDaoImpl<BizReportVO,BizReportMapper> implements ReportQueryDao  {

    @Override
    public Pagination<BizReportVO> pageBizReportVO(String channelCode, String customerName, Integer companyId, Date startTime, Date endTime, String carNumber,
                                                   ProductType type, ContractStatus contractStatus, Integer pageIndex, Integer pageSize) {
        StringBuilder sql = new StringBuilder(" from busi_contract c  LEFT JOIN busi_requisition_detail brd on c.id = brd.business_contract_id LEFT JOIN busi_requisition br on c.requisition_id=br.id LEFT JOIN busi_channel bc on c.channel_id=bc.id " +
                "LEFT JOIN cust_account_info cai on  (c.customer_id=cai.account_id and bc.channel_code=cai.channel_code) LEFT JOIN data_organization o on bc.organization_id=o.id where c.contract_status != 'init'  ");
        List<Object> list = new ArrayList<Object>();
        // 产品类型
        if (type != null) {
            sql.append(" and br.product_type = ?  ");
            list.add(type.getCode());
        }
        // 渠道编号
        if (StringUtils.isNotBlank(channelCode)) {
            sql.append(" and bc.channel_code like ? ");
            list.add("%" + channelCode + "%");
        }
        //客户名称
        if (StringUtils.isNoneBlank(customerName)) {
            sql.append(" and cai.customer_name like ?");
            list.add("%" + customerName + "%");
        }
        if(companyId != null){
            sql.append(" and bc.organization_id = ? ");
            list.add(companyId);
        }
        //开始时间
        if (startTime != null) {
            sql.append(" and c.created_at >= ?");
            list.add(startTime);
        }
        //结束时间
        if (endTime != null) {
            sql.append(" and c.created_at <= ? ");
            list.add(endTime);
        }
        if(StringUtils.isNoneBlank(carNumber)){
            sql.append(" and brd.car_number like  ? ");
            list.add("%" + carNumber + "%");
        }

        //合同状态
        if (contractStatus != null) {
            sql.append(" and c.contract_status = ? ");
            list.add(contractStatus.getCode());
        }

        String countsql = "select count(c.id) " + sql;
        String querysql = "select c.contract_number,brd.car_number,bc.channel_code,bc.channel_name,o.company_name as company_name,br.requisition_number,cai.customer_name," +
                "br.product_type,c.contract_amount,c.created_at as create_time,br.service_fee_rate,br.other_fee_rate,c.contract_status,c.interest_rate " + sql + " order by c.id desc  ";
        Object[] o = null;
        if(list != null && list.size() > 0){
            o = new Object[list.size()];
            for(int i = 0 ; i < list.size() ; i++){
                o[i]=list.get(i);
            }
        }
        return this.createPage(querysql, countsql,o, pageIndex, pageSize, new BizReportMapper());
    }
}
