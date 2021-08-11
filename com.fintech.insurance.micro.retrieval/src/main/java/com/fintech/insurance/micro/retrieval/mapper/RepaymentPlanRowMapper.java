package com.fintech.insurance.micro.retrieval.mapper;

import com.fintech.insurance.micro.dto.biz.RefundVO;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/1 10:14
 */
public class RepaymentPlanRowMapper implements RowMapper<RefundVO> {

    private static final Logger LOG = LoggerFactory.getLogger(RepaymentPlanRowMapper.class);

    @Override
    public RefundVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        RefundVO vo = new RefundVO();
        vo.setRepaymentPlanId(rs.getInt("id"));
        vo.setContractCode(rs.getString("contract_number"));
        vo.setCustomerContractNumber(rs.getString("customer_contract_number"));
        vo.setProductType(rs.getString("product_type"));
        vo.setRequisitionId(rs.getInt("requisition_id"));
        vo.setRequisitionNumber(rs.getString("requisition_number"));
        vo.setCustomerName(rs.getString("customer_name"));
        vo.setRefundPhase(rs.getInt("plan_current_period"));
        vo.setRepayCapitalAmount(rs.getDouble("repay_capital_amount"));
        vo.setRestCapitalAmount(rs.getDouble("rest_capital_amount"));
        vo.setRepayInterestAmount(rs.getDouble("repay_interest_amount"));
        vo.setTotalInstalment(rs.getInt("total_instalment"));
        vo.setBorrowAmount(rs.getDouble("contract_amount"));
        vo.setRepayDayType(rs.getString("repay_day_type"));
        // 逾期罚金
        vo.setOverdueFines(rs.getDouble("overdue_interest_amount"));
        vo.setOverdueRate(rs.getDouble("overdue_fine_rate"));
        // 车牌号/合格证
        vo.setCarNumber(rs.getString("car_number"));
        vo.setRepayDate(rs.getDate("plan_repay_date"));
        Date repaymentDate = rs.getTimestamp("plan_repay_date");
        // 还款日
        if (repaymentDate != null) {
            vo.setRefundDate(repaymentDate.getTime());
            DateUtils.addDays(repaymentDate, rs.getInt("max_overdue_days"));
            vo.setLastRefundDate(DateUtils.addDays(repaymentDate, rs.getInt("max_overdue_days")).getTime());
        }

        LOG.error("now date: " + new Date());
        LOG.error("refund time: " + rs.getTimestamp("repay_time"));
        LOG.error("refund time long: " + rs.getTimestamp("repay_time").getTime());
        LOG.error("String: " + rs.getString("repay_time"));
        // 还款时间
        if (rs.getTimestamp("repay_time") != null) {
            vo.setRefundTime(rs.getTimestamp("repay_time").getTime());
            LOG.error("now date2: " + new Date());
            LOG.error("refund time2: " + rs.getTimestamp("repay_time"));
            LOG.error("refund time long2: " + vo.getRefundTime());
            LOG.error("String2: " + rs.getString("repay_time"));
        }
        // 还款状态
        vo.setRefundStatus(rs.getString("plan_repay_status"));
        // 是否是人工处理状态
        vo.setManualFlag(rs.getBoolean("manual_flag"));
        // 对应的还款记录划扣状态
        vo.setConfirmStatus(rs.getString("confirm_status"));

        return vo;
    }
}
