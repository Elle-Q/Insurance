package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.micro.dto.biz.LoanVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/2 13:53
 */
public class LoanVORowMapper implements RowMapper<LoanVO> {
    @Override
    public LoanVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        LoanVO vo = new LoanVO();
        // 业务单id
        vo.setRequisitionId(rs.getInt("id"));
        // 业务单号
        vo.setRequisitionNumber(rs.getString("requisition_number"));
        // 渠道编号
        vo.setChannelCode(rs.getString("channel_code"));
        // 渠道名称
        vo.setChannelName(rs.getString("channel_name"));
        // 产品类型
        vo.setProductType(rs.getString("product_type"));
        // 客户名称
        vo.setCustomerName(rs.getString("customer_name"));
        // 放款总金额
        vo.setLoanAmount(rs.getDouble("total_apply_amount"));
        // 服务费 = (服务费率 + 其他费率) * 总金额
        Double serviceFeeRate = rs.getDouble("service_fee_rate");
        serviceFeeRate = serviceFeeRate == null ? 0.0 : serviceFeeRate;
        Double otherFeeRate = rs.getDouble("other_fee_rate");
        otherFeeRate = otherFeeRate == null ? 0.0 : otherFeeRate;
        Double totalApplyAmount = rs.getDouble("total_apply_amount");
        totalApplyAmount = totalApplyAmount == null ? 0.0 : totalApplyAmount;
        vo.setServiceFee((serviceFeeRate + otherFeeRate) / 10000 * totalApplyAmount);
        // 订单状态
        vo.setRequisitionStatus(rs.getString("requisition_status"));
        return vo;
    }
}
