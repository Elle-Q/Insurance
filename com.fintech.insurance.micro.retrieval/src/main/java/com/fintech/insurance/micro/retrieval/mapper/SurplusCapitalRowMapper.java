package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.micro.dto.retrieval.SurplusCapitalVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description: 查询剩余本金
 * @Author: Nicholas
 * @Date: 2018/1/13 20:49
 */
public class SurplusCapitalRowMapper implements RowMapper<SurplusCapitalVO> {
    @Override
    public SurplusCapitalVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        SurplusCapitalVO capitalVO = new SurplusCapitalVO();
        capitalVO.setRepaymentPlanId(rs.getInt("id"));
        capitalVO.setSurplusCapitalAmount(rs.getDouble("surPlus_capital_amount"));
        return capitalVO;
    }
}
