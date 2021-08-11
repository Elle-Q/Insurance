package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.micro.dto.customer.CustomerVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/2 15:47
 */
public class CustomerVORowMapper implements RowMapper<CustomerVO> {
    @Override
    public CustomerVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        CustomerVO vo = new CustomerVO();
        // 客户id
        vo.setAccountInfoId(rs.getInt("id"));

        // 客户账户id
        vo.setAccountId(rs.getInt("account_id"));
        // 客户名称
        vo.setName(rs.getString("customer_name"));
        // 客户身份证号
        vo.setIdNum(rs.getString("id_number"));
        // 客户电话
        vo.setPhone(rs.getString("customer_mobile"));
        // 所属渠道
        vo.setChannelOf(rs.getString("channel_name"));
        // 所属公司
        vo.setCompanyOf(rs.getString("company_name"));
        // 客户状态
        vo.setStatus(rs.getInt("is_locked") == 1 ? "freeze" : "normal");


        return vo;
    }
}
