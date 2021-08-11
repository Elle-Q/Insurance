package com.fintech.insurance.micro.retrieval.persist;

import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.retrieval.mapper.ContractNumberRowMapper;
import com.fintech.insurance.micro.retrieval.mapper.RepaymentPlanVORowMapper;
import com.fintech.insurance.micro.retrieval.persist.base.BaseNativeSQLDaoImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/2 15:28
 */

@Repository
@Transactional(readOnly = true)
public class InsuranceRefundPlanDaoImpl extends BaseNativeSQLDaoImpl implements InsuranceRefundPlanDao {

    @Override
    public List<String> findWaitingForToSurrender() {
        String hql = "select DISTINCT(f.contract_number) from finance_repayment_plan f LEFT JOIN busi_contract b on f.contract_number=b.contract_number LEFT JOIN busi_requisition r on b.requisition_id = r.id where \n" +
                "f.repay_date < date_sub( ? ,interval r.max_overdue_days day) and f.repay_status in ('fail_refund', 'overdue') and f.manual_flag = false ";
        Date date = DateCommonUtils.getCurrentDate();
        return this.createList(hql, new Object[]{date},new ContractNumberRowMapper());
    }

    @Override
    public List<FinanceRepaymentPlanVO> listPlansForOverdue() {
        String sql = "SELECT distinct plan.id id," +
                " c.contract_number contract_number," +
                "plan.channel_id channel_id," +
                "plan.customer_id customer_id," +
                "plan.repay_date repay_date," +
                "req.prepayment_days prepayment_days," +
                " req.product_type product_type," +
                " c.requisition_id requisition_id," +
                " req.requisition_number requisition_number," +
                " info.customer_name customer_name," +
                " plan.current_instalment current_instalment," +
                " plan.repay_capital_amount repay_capital_amount," +
                " plan.repay_interest_amount repay_interest_amount," +
                " record.overdue_interest_amount overdue_interest_amount," +
                " detail.car_number car_number," +
                " plan.repay_date repay_date," +
                " req.max_overdue_days max_overdue_days," +
                " record.repay_time repay_time," +
                " plan.repay_status repay_status," +
                " plan.manual_flag manual_flag FROM finance_repayment_plan plan " +
                "LEFT JOIN busi_contract c ON plan.contract_number = c.contract_number " +
                "LEFT JOIN busi_requisition_detail detail ON detail.business_contract_id = c.id " +
                "LEFT JOIN busi_requisition req ON req.id = c.requisition_id " +
                "LEFT JOIN busi_channel channel ON req.channel_id = channel.id " +
                "LEFT JOIN cust_account_info info ON plan.customer_id = info.account_id and channel.channel_code = info.channel_code " +
                "LEFT JOIN (SELECT overdue_interest_amount, repay_time, repayment_plan_id, confirm_status, MAX(id) as id " +
                    "FROM finance_repayment_record ORDER BY id) record ON plan.id = record.repayment_plan_id " +
                " where plan.repay_date >= date_sub( ? ,interval req.max_overdue_days day) and plan.repay_date < ? " +
                "and plan.repay_status in ('fail_refund', 'overdue', 'waiting_refund') and plan.manual_flag = false" +
                " and (record.confirm_status = 'failed' or record.confirm_status is NULL) ";

        return this.createList(sql, new Object[]{DateCommonUtils.dateToStringByFormat(DateCommonUtils.getCurrentDate(), "yyyy-MM-dd"), DateCommonUtils.dateToStringByFormat(DateCommonUtils.getCurrentDate(), "yyyy-MM-dd")},new RepaymentPlanVORowMapper());
    }

    @Override
    public List<FinanceRepaymentPlanVO> listPlansForRepayDate() {
        String sql = "SELECT distinct plan.id id," +
                " c.contract_number contract_number," +
                "plan.channel_id channel_id," +
                "plan.customer_id customer_id," +
                "plan.repay_date repay_date," +
                "req.prepayment_days prepayment_days," +
                " req.product_type product_type," +
                " c.requisition_id requisition_id," +
                " req.requisition_number requisition_number," +
                " info.customer_name customer_name," +
                " plan.current_instalment current_instalment," +
                " plan.repay_capital_amount repay_capital_amount," +
                " plan.repay_interest_amount repay_interest_amount," +
                " record.overdue_interest_amount overdue_interest_amount," +
                " detail.car_number car_number," +
                " plan.repay_date repay_date," +
                " req.max_overdue_days max_overdue_days," +
                " record.repay_time repay_time," +
                " plan.repay_status repay_status," +
                " plan.manual_flag manual_flag FROM finance_repayment_plan plan " +
                "LEFT JOIN busi_contract c ON plan.contract_number = c.contract_number " +
                "LEFT JOIN busi_requisition_detail detail ON detail.business_contract_id = c.id " +
                "LEFT JOIN busi_requisition req ON req.id = c.requisition_id " +
                "LEFT JOIN busi_channel channel ON req.channel_id = channel.id " +
                "LEFT JOIN cust_account_info info ON plan.customer_id = info.account_id and channel.channel_code = info.channel_code " +
                "LEFT JOIN finance_repayment_record record ON plan.id = record.repayment_plan_id where 1 = 1 " +
                " and plan.repay_date >= date_sub( ? ,interval req.max_overdue_days day) and plan.repay_status in ('overdue') and plan.manual_flag = false";

        return this.createList(sql, new Object[]{DateCommonUtils.dateToStringByFormat(DateCommonUtils.getCurrentDate(), "yyyy-MM-dd")},new RepaymentPlanVORowMapper());
    }

    @Override
    public List<FinanceRepaymentPlanVO> listPlansByStatus(String code) {
        String sql = "SELECT distinct plan.id id," +
                " c.contract_number contract_number," +
                "plan.channel_id channel_id," +
                "plan.customer_id customer_id," +
                "plan.repay_date repay_date," +
                "req.prepayment_days prepayment_days," +
                " req.product_type product_type," +
                " c.requisition_id requisition_id," +
                " req.requisition_number requisition_number," +
                " info.customer_name customer_name," +
                " plan.current_instalment current_instalment," +
                " plan.repay_capital_amount repay_capital_amount," +
                " plan.repay_interest_amount repay_interest_amount," +
                " record.overdue_interest_amount overdue_interest_amount," +
                " detail.car_number car_number," +
                " plan.repay_date repay_date," +
                " req.max_overdue_days max_overdue_days," +
                " record.repay_time repay_time," +
                " plan.repay_status repay_status," +
                " plan.manual_flag manual_flag FROM finance_repayment_plan plan " +
                "LEFT JOIN busi_contract c ON plan.contract_number = c.contract_number " +
                "LEFT JOIN busi_requisition_detail detail ON detail.business_contract_id = c.id " +
                "LEFT JOIN busi_requisition req ON req.id = c.requisition_id " +
                "LEFT JOIN busi_channel channel ON req.channel_id = channel.id " +
                "LEFT JOIN cust_account_info info ON plan.customer_id = info.account_id and channel.channel_code = info.channel_code " +
                "LEFT JOIN finance_repayment_record record ON plan.id = record.repayment_plan_id where 1 = 1 " +
                " and plan.repay_status = ? and plan.manual_flag = false";

        return this.createList(sql, new Object[]{code},new RepaymentPlanVORowMapper());
    }


    @Override
    public List<FinanceRepaymentPlanVO> listPlansForWaitingRefundAndRepayDate(Date endate) {
        String sql = "SELECT distinct plan.id id," +
                " c.contract_number contract_number," +
                "plan.channel_id channel_id," +
                "plan.customer_id customer_id," +
                "plan.repay_date repay_date," +
                "req.prepayment_days prepayment_days," +
                " req.product_type product_type," +
                " c.requisition_id requisition_id," +
                " req.requisition_number requisition_number," +
                " info.customer_name customer_name," +
                " plan.current_instalment current_instalment," +
                " plan.repay_capital_amount repay_capital_amount," +
                " plan.repay_interest_amount repay_interest_amount," +
                " record.overdue_interest_amount overdue_interest_amount," +
                " detail.car_number car_number," +
                " plan.repay_date repay_date," +
                " req.max_overdue_days max_overdue_days," +
                " record.repay_time repay_time," +
                " plan.repay_status repay_status," +
                " plan.manual_flag manual_flag FROM finance_repayment_plan plan " +
                "LEFT JOIN busi_contract c ON plan.contract_number = c.contract_number " +
                "LEFT JOIN busi_requisition_detail detail ON detail.business_contract_id = c.id " +
                "LEFT JOIN busi_requisition req ON req.id = c.requisition_id " +
                "LEFT JOIN busi_channel channel ON req.channel_id = channel.id " +
                "LEFT JOIN cust_account_info info ON plan.customer_id = info.account_id and channel.channel_code = info.channel_code " +
                "LEFT JOIN finance_repayment_record record ON plan.id = record.repayment_plan_id where 1 = 1 " +
                " and plan.repay_status = ? and plan.repay_date = ? and plan.manual_flag = ?";

        List<Object> args = new ArrayList<>();
        args.add(RefundStatus.WAITING_REFUND.getCode());
        args.add(DateCommonUtils.getBeginDateByDate(endate));
        args.add(false);
        return this.createList(sql, args.toArray(),new RepaymentPlanVORowMapper());
    }
}
