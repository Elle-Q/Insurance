package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.micro.dto.biz.ChannelVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChannelRowMapper implements RowMapper<ChannelVO> {

    @Override
    public ChannelVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ChannelVO vo = new ChannelVO();
        vo.setId(rs.getInt("id"));
        vo.setChannelCode(rs.getString("channel_code"));
        vo.setChannelName(rs.getString("channel_name"));
        vo.setMobile(rs.getString("mobile_phone"));
        vo.setCompanyName(rs.getString("company_name"));
        vo.setCreateAt(rs.getTimestamp("created_at"));
        vo.setChannelName(rs.getString("channel_name"));
        vo.setIsLocked(rs.getInt("is_locked"));
        vo.setBusinessLicence(rs.getString("business_licence"));
        vo.setCompanyId(rs.getInt("organization_id"));
        vo.setAreaCode(rs.getString("area_code"));
        return vo;
    }
}
