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
public class ContractRowMapper implements RowMapper<ContractVO> {
    @Override
    public ContractVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ContractVO vo = new ContractVO();

        // 合同id
        vo.setContractId(rs.getInt("contractId"));
        // 合同号
        vo.setContractCode(rs.getString("contract_number"));
        // 真实合同编码
        vo.setCustomerContractNumber(rs.getString("customer_contract_number"));
        // 业务单id
        vo.setRequisitionId(rs.getInt("requisition_id"));
        // 业务单号
        vo.setRequisitionNumber(rs.getString("requisition_number"));
        // 车牌号
        vo.setCarNumber(rs.getString("car_number"));
        // 渠道编码
        vo.setChannelCode(rs.getString("channel_code"));
        // 渠道名称
        vo.setChannelName(rs.getString("channel_name"));

        //渠道用户手机号码
        // 这里没有设置哦
        // vo.setChannelUserMobile(rs.getString(""));

        // 产品类型
        vo.setProductType(rs.getString("product_type"));
        // 客户名称
        vo.setCustomerName(rs.getString("customer_name"));
        //客户手机号码
        vo.setCustomerMobile(rs.getString("customer_mobile"));
        // 放款时间
        vo.setLoanDate(rs.getTimestamp("loan_time"));
        // 借款金额
        vo.setBorrowAmount(rs.getDouble("total_apply_amount"));
        // 还款总期数
        vo.setTotalPhase(rs.getInt("business_duration"));

        // 已还款期数
        // 在businessService中设置

        // 订单状态
        vo.setContractStatus(rs.getString("contract_status"));
        // 最大逾期天数
        vo.setMaxOverdueDays(rs.getInt("max_overdue_days"));

        // 合同月利率
        vo.setInterestRate(rs.getDouble("interest_rate"));

        return vo;
    }
}
