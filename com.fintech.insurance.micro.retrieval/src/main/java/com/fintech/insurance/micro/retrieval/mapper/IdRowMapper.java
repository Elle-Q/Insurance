package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IdRowMapper implements RowMapper<RequisitionVO> {
    @Override
    public RequisitionVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            RequisitionVO vo = new RequisitionVO();
            vo.setId(rs.getInt("requisition_id"));
            return vo;
            }
}
