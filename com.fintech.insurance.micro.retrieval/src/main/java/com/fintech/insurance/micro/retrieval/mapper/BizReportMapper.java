package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.commons.utils.NumberFormatorUtils;
import com.fintech.insurance.micro.dto.biz.BizReportVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * @Description: (some words)
 * @Author: YongNeng Liu
 * @Date: 2017/11/30 18:21
 */
public class BizReportMapper implements RowMapper<BizReportVO>  {

    private static final String SYMBOL = "%";

    @Override
    public BizReportVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        BizReportVO vo = new BizReportVO();
        vo.setCustomerName(rs.getString("customer_name"));
        vo.setCompanyName(rs.getString("company_name"));
        vo.setContractNumber(rs.getString("contract_number"));
        vo.setChannelCode(rs.getString("channel_code"));
        vo.setChannelName(rs.getString("channel_name"));
        vo.setRequisitionNumber(rs.getString("requisition_number"));
        vo.setProductTypeCode(rs.getString("product_type"));
        vo.setBorrowAmount(NumberFormatorUtils.convertFinanceNumberToShowString(rs.getDouble("contract_amount")));
        vo.setCreateTime(rs.getTimestamp("create_time"));
        vo.setInterestRate(NumberFormatorUtils.convertFinanceNumberToShowString(rs.getDouble("interest_rate")) + SYMBOL);
        vo.setServiceFeeRate(NumberFormatorUtils.convertFinanceNumberToShowString(rs.getDouble("service_fee_rate")) + SYMBOL);
        vo.setOtherFeeRate(NumberFormatorUtils.convertFinanceNumberToShowString(rs.getDouble("other_fee_rate")) + SYMBOL);
        vo.setBorrowFeeAmount(NumberFormatorUtils.convertFinanceNumberToShowString((rs.getBigDecimal("service_fee_rate").add(rs.getBigDecimal("other_fee_rate"))).multiply(rs.getBigDecimal("contract_amount")).doubleValue()/10000));
        vo.setContractStatusCode(rs.getString("contract_status"));
        vo.setCarNumber(rs.getString("car_number"));
        return vo;
    }

}
