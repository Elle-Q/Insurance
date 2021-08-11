package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.retrieval.CountRequisitionVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description: (some words)
 * @Author: qxy
 * @Date: 2017/11/30 18:21
 */
public class CountRequisitionMapper implements RowMapper<Integer> {
    @Override
    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer num = rs.getInt("num");
        return num;
    }
}
