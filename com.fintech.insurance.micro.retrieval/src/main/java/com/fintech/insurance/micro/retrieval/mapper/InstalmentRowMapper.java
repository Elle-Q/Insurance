package com.fintech.insurance.micro.retrieval.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/1 16:24
 */
public class InstalmentRowMapper implements RowMapper<InstalmentInfo> {
    @Override
    public InstalmentInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        InstalmentInfo info = new InstalmentInfo();
        info.setCompleteInstalment(rs.getInt("complete_instalment"));
        info.setTotalInstalment(rs.getInt("total_instalment"));
        return info;
    }
}
