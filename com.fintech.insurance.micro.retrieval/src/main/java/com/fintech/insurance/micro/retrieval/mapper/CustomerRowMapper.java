package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.micro.dto.customer.CustomerVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description: (some words)
 * @Author: qxy
 * @Date: 2017/11/30 18:21
 */
public class CustomerRowMapper implements RowMapper<CustomerVO> {

    @Override
    public CustomerVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        CustomerVO customerVO = new CustomerVO();
        customerVO.setAccountId(rs.getInt("account_id"));
        return customerVO;
    }
}
