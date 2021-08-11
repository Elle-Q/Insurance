package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.micro.dto.finance.BankVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BankVORowMapper implements RowMapper<BankVO> {
    @Override
    public BankVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        BankVO bankVO = new BankVO();
        bankVO.setDailyLimit(rs.getInt("daily_limit"));
        bankVO.setSingleLimit(rs.getInt("single_limit"));

        return bankVO;
    }
}
