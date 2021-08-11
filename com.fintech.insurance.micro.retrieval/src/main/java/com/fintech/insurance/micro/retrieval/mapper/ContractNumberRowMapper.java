package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.micro.dto.biz.ContractVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/1 15:34
 */
public class ContractNumberRowMapper implements RowMapper<String> {
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        String str = rs.getString("contract_number");
        return str;
    }
}
