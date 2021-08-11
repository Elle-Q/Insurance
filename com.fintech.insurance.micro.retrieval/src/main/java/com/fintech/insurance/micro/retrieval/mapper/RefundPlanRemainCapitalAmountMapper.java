package com.fintech.insurance.micro.retrieval.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description: (some words)
 * @Author: qxy
 * @Date: 2017/11/30 18:21
 */
public class RefundPlanRemainCapitalAmountMapper implements RowMapper<BigDecimal> {
    @Override
    public BigDecimal mapRow(ResultSet rs, int rowNum) throws SQLException {
        BigDecimal num = rs.getBigDecimal("num");
        return num;
    }
}
