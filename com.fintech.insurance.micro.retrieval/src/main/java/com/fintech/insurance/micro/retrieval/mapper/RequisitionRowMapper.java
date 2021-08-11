package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RequisitionRowMapper implements RowMapper<RequisitionVO> {

    @Override
    public RequisitionVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        RequisitionVO vo = new RequisitionVO();
        vo.setId(rs.getInt("requisition_id"));
        vo.setRequisitionNumber(rs.getString("requisition_number"));
        vo.setCustomerId(rs.getInt("customer_id"));
        vo.setCustomerName(rs.getString("customer_name") == null ? "" : rs.getString("customer_name"));
        vo.setChannelId(rs.getInt("channel_id"));
        vo.setChannelCode(rs.getString("channel_code"));
        vo.setChannelName(rs.getString("channel_name"));
        vo.setProductType(rs.getString("product_type"));
        vo.setRequisitionStatus(rs.getString("requisition_status"));
        vo.setSubmmitTime(rs.getDate("submission_date"));
        vo.setBorrowAmount(rs.getDouble("total_apply_amount"));
        vo.setLatestAuditBatch(rs.getString("latest_audit_batch"));
        vo.setProductId(rs.getInt("product_id"));
        vo.setProductName(rs.getString("product_name"));
        vo.setManualFlag(rs.getInt("is_manual"));
        return vo;
    }
}
