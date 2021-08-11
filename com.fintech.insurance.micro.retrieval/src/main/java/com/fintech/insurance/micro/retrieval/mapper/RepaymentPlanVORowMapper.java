package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RepaymentPlanVORowMapper implements RowMapper<FinanceRepaymentPlanVO> {
    @Override
    public FinanceRepaymentPlanVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        FinanceRepaymentPlanVO vo = new FinanceRepaymentPlanVO();
        vo.setId(rs.getInt("id"));
        vo.setChannelId(rs.getInt("channel_id"));
        vo.setCustomerId(rs.getInt("customer_id"));
        vo.setAdvanceRepayDays(rs.getInt("prepayment_days"));
        vo.setRepayDate(rs.getTimestamp("repay_date"));
        return vo;
    }
}
