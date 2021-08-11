package com.fintech.insurance.micro.retrieval.persist;

import com.fintech.insurance.commons.enums.*;
import com.fintech.insurance.commons.utils.CalculationFormulaUtils;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.biz.*;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.finance.BankVO;
import com.fintech.insurance.micro.dto.retrieval.UserVO;
import com.fintech.insurance.micro.retrieval.mapper.*;
import com.fintech.insurance.micro.retrieval.persist.base.BaseNativeSQLDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/30 18:41
 */
@Repository
@Transactional(readOnly = true)
public class BizQueryDaoImpl extends BaseNativeSQLDaoImpl implements BizQueryDao {

    public List<UserVO> findAllUser() {
        return jdbcTemplate.query("select id, user_name from sys_user", new UserRowMapper());
    }

    @Override
    public Pagination<RefundVO> pageRefundVOByNativeSQL(String customerContractNumber, String customerName, RefundStatus refundStatus,
                                                        String requisitionNumber, String carNumber, Date refundBeginDate,
                                                        Date refundEndDate, Integer pageIndex, Integer pageSize) {
        StringBuilder sb = new StringBuilder(" FROM finance_repayment_plan plan " +
                "LEFT JOIN busi_contract c ON plan.contract_number = c.contract_number " +
                "LEFT JOIN busi_requisition_detail detail ON detail.business_contract_id = c.id " +
                "LEFT JOIN busi_requisition req ON req.id = c.requisition_id " +
                "LEFT JOIN busi_channel channel ON req.channel_id = channel.id " +
                "LEFT JOIN cust_account_info info ON req.customer_account_info_id = info.id " +
                "LEFT JOIN finance_repayment_record record ON plan.id = record.repayment_plan_id " +
                " and record.confirm_status != '" + DebtStatus.FAILED.getCode() + "' where plan.repay_status != ? ");

        List<Object> args = new ArrayList<>();
        args.add(RefundStatus.INIT_REFUND.getCode());
        if (StringUtils.isNotBlank(customerContractNumber)) {
            sb.append(" and c.customer_contract_number like ? ");
            args.add("%" + customerContractNumber + "%");
        }
        if (StringUtils.isNotBlank(customerName)) {
            sb.append(" and info.customer_name like ? ");
            args.add("%" + customerName + "%");
        }
        if (StringUtils.isNotBlank(requisitionNumber)) {
            sb.append(" and req.requisition_number like ? ");
            args.add("%" + requisitionNumber + "%");
        }
        if (StringUtils.isNotBlank(carNumber)) {
            sb.append(" and detail.car_number like ? ");
            args.add("%" + carNumber + "%");
        }
        if (refundStatus != null) {
            sb.append(" and plan.repay_status = ? ");
            args.add(refundStatus.getCode());
        }
        if (refundBeginDate != null) {
            sb.append(" and plan.repay_date >= ? ");
            args.add(refundBeginDate);
        }
        if (refundEndDate != null) {
            sb.append(" and plan.repay_date <= ? ");
            args.add(refundEndDate);
        }

        String countSql = "SELECT count(*) " + sb.toString();

        sb.append(" order by " +
                "case" +
                " when plan.repay_status = 'waiting_refund' then 1 " +
                " when plan.repay_status = 'overdue' then 1 " +
                " when plan.repay_status = 'waiting_withdraw' then 1 " +
                " when plan.repay_status = 'fail_refund' then 1 " +
                " when plan.repay_status = 'has_withdraw' then 2 " +
                " when plan.repay_status = 'has_refund' then 2 " +
                "end, plan.repay_date ");
        String sql = "SELECT plan.id id," +
                " c.contract_number contract_number," +
                " c.customer_contract_number customer_contract_number," +
                " req.product_type product_type," +
                " c.requisition_id requisition_id," +
                " req.requisition_number requisition_number," +
                " info.customer_name customer_name," +
                " plan.current_instalment plan_current_period," +
                " plan.repay_capital_amount repay_capital_amount," +
                " plan.rest_capital_amount rest_capital_amount," +
                " plan.repay_interest_amount repay_interest_amount," +
                " record.overdue_interest_amount overdue_interest_amount," +
                " detail.car_number car_number," +
                " plan.repay_date plan_repay_date," +
                " req.max_overdue_days max_overdue_days," +
                " case when record.repay_time is null then plan.updated_at else record.repay_time end repay_time," +
                " plan.repay_status plan_repay_status," +
                " plan.manual_flag manual_flag," +
                " req.overdue_fine_rate overdue_fine_rate," +
                " record.confirm_status confirm_status," +
                " plan.total_instalment total_instalment," +
                " req.repay_day_type repay_day_type," +
                " c.contract_amount contract_amount" + sb.toString();


        Pagination<RefundVO> page =  this.createPage(sql, countSql, args.toArray(), pageIndex, pageSize, new RepaymentPlanRowMapper());

        // 这里设置逾期罚金和还款总金额
        if (page != null && page.getItems() != null && page.getItems().size() > 0) {
            List<RefundVO> resultList = page.getItems();
            for (int i = 0 ; i < resultList.size(); i++ ) {
                RefundVO refundVO = resultList.get(i);
                if(!RefundStatus.HAS_REFUND.getCode().equals(refundVO.getRefundStatus())) {
                    refundVO.setRefundTime(null);
                }
                // 还款了以实际的为准，未还款逾期再计算罚金
                Double overdueFines = refundVO.getOverdueFines() == null ? CalculationFormulaUtils.ZERO_NUMBER : refundVO.getOverdueFines();
                if(!RefundStatus.HAS_REFUND.getCode().equals(refundVO.getRefundStatus()) && DateCommonUtils.getCurrentDate().after(refundVO.getRepayDate())){
                    overdueFines = this.getOverDueFinesByRefundVO(refundVO);
                }
                refundVO.setOverdueFines(overdueFines);
                // 还款金额 = 本金 + 利息 + 逾期罚金
                refundVO.setRefundAmount(refundVO.getRepayCapitalAmount() + refundVO.getRepayInterestAmount() + overdueFines);

                //车辆分期最后一期展示还款本金（纯展示不做结算用途）
                if(refundVO.getRefundPhase().equals(resultList.size()) && StringUtils.equals(ProductType.CAR_INSTALMENTS.getCode(), refundVO.getProductType())){
                    refundVO.setRepayCapitalAmount(CalculationFormulaUtils.getAssureMoney(new BigDecimal(refundVO.getBorrowAmount()),resultList.size()).doubleValue());
                }
            }
        }
        return page;
    }

    private Double getOverDueFinesByRefundVO(RefundVO refundVO) {
        InstalmentInfo info = getInstalment(refundVO.getContractCode());
        // 如果当前期已退保，并且当前期数 > (已还期数+1), 则不计算逾期费用
        if(RefundStatus.HAS_WITHDRAW.getCode().equals(refundVO.getRefundStatus()) &&
                refundVO.getRefundPhase() > (info.getCompleteInstalment() + 1)) {
            return 0.0;
        }
        Date lastRefundDate = DateCommonUtils.stampToDate(refundVO.getLastRefundDate());

        // 逾期时间止
        Date overdueDeadline = DateCommonUtils.getCurrentDate().before(lastRefundDate)? DateCommonUtils.getCurrentDate() : lastRefundDate;
        long overdueDays = DateCommonUtils.intervalDays(refundVO.getRepayDate(), overdueDeadline);
        Double overdueFines = CalculationFormulaUtils.getOverdueFines(refundVO.getRestCapitalAmount(),
                refundVO.getRepayCapitalAmount(), refundVO.getOverdueRate(), (int)overdueDays);
        return overdueFines;
    }

    @Override
    public Pagination<RequisitionVO> queryRequisition(String requisitionNumber, String status, String type, String channelName, Date submmitStartTime, Date submmitEndTime, String customerName, Integer pageIndex, Integer pageSize) {
        StringBuilder sql = new StringBuilder(" FROM busi_requisition br LEFT JOIN busi_channel bc ON br.channel_id = bc.id " +
                " LEFT JOIN cust_account_info info ON br.customer_account_info_id = info.id AND  info.channel_code = bc.channel_code " +
                " LEFT JOIN busi_product bp ON br.product_id = bp.id " +
                " LEFT JOIN finance_payment_order f ON f.order_number = br.payment_order_number WHERE 1 = 1 ");
        List<Object> args = new ArrayList<>();
        if (StringUtils.isNoneBlank(requisitionNumber)) {
            sql.append(" and br.requisition_number like ?");
            args.add("%" + requisitionNumber + "%");
        }
        if (StringUtils.isNotEmpty(status)) {
            sql.append(" and br.requisition_status = ? ");
            args.add(status);
        }
        if (StringUtils.isNotEmpty(type)) {
            sql.append(" and br.product_type = ? ");
            args.add(type);
        }
        if (StringUtils.isNotEmpty(channelName)) {
            sql.append(" and bc.channel_name like ? ");
            args.add("%" + channelName + "%");
        }
        if (submmitStartTime != null) {
            sql.append(" and br.submission_date >= ? ");
            args.add(submmitStartTime);
        }
        if (submmitEndTime != null) {
            //Date endTime = DateCommonUtils.getAfterDay(submmitEndTime, 1);
            sql.append(" and br.submission_date <= ? ");
            args.add(submmitEndTime);
        }
        if (StringUtils.isNotEmpty(customerName)) {
            sql.append(" and info.customer_name like ? ");
            args.add("%" + customerName + "%");
        }
        sql.append(" and br.requisition_status != ? ");
        args.add(RequisitionStatus.Draft.getCode());
        sql.append(" order by case br.requisition_status when 'failpayment' then 0 when 'auditing' then 1 else 2 end asc, br.submission_date desc ");
        return this.createPage("SELECT br.id as requisition_id, br.requisition_number,br.customer_id," +
                "info.customer_name,br.channel_id,bc.channel_code,bc.channel_name,br.product_type,br.submission_date," +
                "br.requisition_status,br.submission_date,br.total_apply_amount,br.latest_audit_batch, br.product_id," +
                "bp.product_name,f.is_manual " + sql.toString(), "SELECT count(*) " + sql.toString(), args.toArray(), pageIndex, pageSize, new RequisitionRowMapper());
    }

    @Override
    public Pagination<ChannelVO> queryChannel(String channelCode, String channelName, String companyName, String mobile, Integer pageIndex, Integer pageSize) {
        StringBuilder sql = new StringBuilder(" FROM busi_channel c LEFT JOIN data_organization o ON c.organization_id = o.id LEFT JOIN sys_user u ON c.channel_code = u.channel_code WHERE 1 = 1 ");
        List<Object> args = new ArrayList<>();
        if (StringUtils.isNoneBlank(channelCode)) {
            sql.append(" and c.channel_code like ?");
            args.add("%" + channelCode + "%");
        }
        if (StringUtils.isNotEmpty(channelName)) {
            sql.append(" and c.channel_name like ? ");
            args.add("%" + channelName + "%");
        }
        if (StringUtils.isNotEmpty(companyName)) {
            sql.append(" and o.company_name like ? ");
            args.add("%" + companyName + "%");
        }
        if (StringUtils.isNotEmpty(mobile)) {
            sql.append(" and u.mobile_phone like ? ");
            args.add("%" + mobile + "%");
        }
        sql.append(" and u.is_channel_admin = 1 order by c.created_at desc");
        return this.createPage("SELECT * " + sql.toString(), "SELECT count(*) " + sql.toString(), args.toArray(), pageIndex, pageSize, new ChannelRowMapper());
    }
    public Pagination<ContractVO> pageContractVOByNativeSQL(String customerContractNumber, ContractStatus contractStatus, String channelName, String customerName,
                                                            String carNumber, Date loadBeginDate, Date loadEndDate, int pageIndex, int pageSize) {
        StringBuilder sb = new StringBuilder(" from busi_contract contract " +
                "LEFT JOIN busi_requisition req ON contract.requisition_id = req.id " +
                "LEFT JOIN busi_requisition_detail reqdetail ON contract.id = reqdetail.business_contract_id " +
                "LEFT JOIN busi_channel channel ON contract.channel_id = channel.id " +
                "LEFT JOIN cust_account_info info on req.customer_account_info_id = info.id " +
                "where req.channel_id = channel.id and contract.customer_contract_number is not null");
        List<Object> args = new ArrayList<Object>();
        if (StringUtils.isNotBlank(customerContractNumber)) {
            sb.append(" and contract.customer_contract_number like ? ");
            args.add("%" + customerContractNumber + "%");
        }
        if (contractStatus != null) {
            sb.append(" and contract.contract_status = ? ");
            args.add(contractStatus.getCode());
        }
        if (StringUtils.isNotBlank(channelName)) {
            sb.append(" and channel.channel_name like ? ");
            args.add("%" + channelName + "%");
        }
        if (StringUtils.isNotBlank(customerName)) {
            sb.append(" and info.customer_name like ? ");
            args.add("%" + customerName + "%");
        }
        if (loadBeginDate != null) {
            sb.append(" and req.loan_time >= ? ");
            args.add(loadBeginDate);
        }
        if (loadEndDate != null) {
            sb.append(" and req.loan_time <= ? ");
            args.add(loadEndDate);
        }
        if(StringUtils.isNotBlank(carNumber)) {
            sb.append(" and reqdetail.car_number like ? ");
            args.add("%" + carNumber + "%");
        }
        sb.append(" and contract.contract_status != 'init' ");

        String countSql = "SELECT count(*) " + sb.toString();
        // 待退保，还款中，已完成，已退保的顺序，然后放款时间
        sb.append(" group by contract.id order by " +
                "case " +
                " when contract.contract_status = 'insreturning' then 1" +
                " when contract.contract_status = 'refunding' then 2 " +
                " when contract.contract_status = 'refunded' then 3 " +
                " when contract.contract_status = 'insreturned' then 3 " +
                "end, req.loan_time desc ");

        String sql = "SELECT contract.id contractId, " +
                "contract.interest_rate interest_rate, " +
                "contract.business_duration business_duration, " +
                "contract.contract_number contract_number, " +
                "contract.customer_contract_number customer_contract_number, " +
                "contract.requisition_id requisition_id, " +
                "contract.contract_amount total_apply_amount, " +
                "req.requisition_number requisition_number, " +
                "reqdetail.car_number car_number, " +
                "channel.channel_code channel_code, " +
                "channel.channel_name channel_name, " +
                "req.product_type product_type, " +
                "info.customer_name customer_name, " +
                "info.customer_mobile customer_mobile, " +
                "req.loan_time loan_time, " +
                "req.max_overdue_days max_overdue_days, " +
                "contract.contract_status contract_status " + sb.toString();

        Pagination<ContractVO> page = this.createPage(sql, countSql, args.toArray(), pageIndex, pageSize, new ContractRowMapper());

        // 这里设置还款期数和全部期数
        if (page != null && page.getItems() != null && page.getItems().size() > 0) {
            List<ContractVO> resultList = page.getItems();
            for (ContractVO contractVO : resultList) {
                InstalmentInfo info = getInstalment(contractVO.getContractCode());
                if (info != null) {
                    contractVO.setRefundPhase(info.getCompleteInstalment() == null ? 0 : info.getCompleteInstalment());
                }
            }
        }
        return page;
    }

    @Override
    public Pagination<LoanVO> pageLoanVOByNativeSQL(String requisitionNumber, RequisitionStatus requisitionStatus,
               ProductType productType, String channelName, String customerName, Integer pageIndex, Integer pageSize) {
        StringBuilder sb = new StringBuilder(" from busi_requisition req " +
                "left JOIN busi_channel channel ON req.channel_id = channel.id " +
                "left JOIN cust_account_info info ON req.customer_account_info_id = info.id WHERE 1 = 1 ");
        List<Object> args = new ArrayList<>();
        if (StringUtils.isNotBlank(requisitionNumber)) {
            sb.append(" and requisition_number like ? ");
            args.add("%" + requisitionNumber + "%");
        }
        if (requisitionStatus != null) {
            sb.append(" and requisition_status = ? ");
            args.add(requisitionStatus.getCode());
        } else {
            sb.append(" and (requisition_status = 'waitloan' or requisition_status = 'loaned') ");
        }
        if (productType != null) {
            sb.append(" and product_type = ? ");
            args.add(productType.getCode());
        }
        if (StringUtils.isNotBlank(channelName)) {
            sb.append(" and channel_name like ? ");
            args.add("%" + channelName + "%");
        }
        if (StringUtils.isNotBlank(customerName)) {
            sb.append(" and customer_name like ? ");
            args.add("%" + customerName + "%");
        }
        String countSql = "SELECT count(*) " + sb.toString();
        sb.append(" order by audit_success_time desc ");

        String sql = "select req.id id, " +
                "req.requisition_number requisition_number, " +
                "channel.channel_name channel_name, " +
                "channel.channel_code channel_code, " +
                "req.product_type product_type, " +
                "info.customer_name customer_name, " +
                "req.total_apply_amount total_apply_amount, " +
                "req.service_fee_rate service_fee_rate, " +
                "req.other_fee_rate other_fee_rate, " +
                "req.requisition_status requisition_status " + sb.toString();

        Pagination<LoanVO> page = this.createPage(sql, countSql, args.toArray(), pageIndex, pageSize, new LoanVORowMapper());

        // 支付凭证
        if (page != null && page.getItems() != null && page.getItems().size() > 0) {
            List<LoanVO> loanVOList = page.getItems();
            for (LoanVO vo : loanVOList) {
                RecordVO recordVO = getRecord(vo.getRequisitionNumber());
                vo.setRecordVO(recordVO);
            }
        }

        return page;
    }

    @Override
    public Integer countRequisitionByStatus(String channelUserIds, String customerId, String requisitionStatus) {
        if (StringUtils.isNotEmpty(customerId)) {
            return jdbcTemplate.query("select COUNT(*) as num, requisition_status from busi_requisition " +
                    "WHERE customer_id = ?  " +
                    "and requisition_status = ? ", new Object[]{customerId, requisitionStatus}, new CountRequisitionMapper()).get(0);
        } else {
            return jdbcTemplate.query("select COUNT(*) as num, requisition_status from busi_requisition " +
                    " WHERE channel_user_id in (?) " +
                    " and requisition_status = ? ", new Object[]{channelUserIds.split(","), requisitionStatus}, new CountRequisitionMapper()).get(0);
        }
    }

    @Override
    public BankVO getBankInfoByCode(String accountBankCode) {
        StringBuilder sb = new StringBuilder("select * from finance_enterprise_bank where enterprise_bank_code = ? and application_code = 'DEBT' ");

        List<BankVO> resultList = jdbcTemplate.query(sb.toString(), new Object[]{accountBankCode}, new BankVORowMapper());

        BankVO bankVO = null;
        if (resultList != null && resultList.size() > 0) {
            bankVO = resultList.get(0);
        }
        return bankVO;
    }

    public List<CustomerVO> listCustomerByName(String customerName) {
        return jdbcTemplate.query("select account_id from cust_account_info WHERE customer_name LIKE '%?%'", new Object[]{customerName}, new CustomerRowMapper());
    }

    private RecordVO getRecord(String requisitionNumber) {
        StringBuilder sb = new StringBuilder("select * from finance_account_voucher where requisition_code like ? and account_type = ? ");

        List<RecordVO> resultList = jdbcTemplate.query(sb.toString(), new Object[]{requisitionNumber, AccountType.SERVICEFEE.getCode()}, new RecordVORowMapper());

        RecordVO recordVO = null;
        if (resultList != null && resultList.size() > 0) {
            recordVO = resultList.get(0);
        }
        return recordVO;
    }

    private InstalmentInfo getInstalment(String contractNumber) {
        StringBuilder sb = new StringBuilder("select max(current_instalment) as complete_instalment, total_instalment from finance_repayment_plan" +
                " where contract_number = ? and repay_status = 'has_refund'");
        List<InstalmentInfo> resultList = jdbcTemplate.query(sb.toString(), new Object[]{contractNumber}, new InstalmentRowMapper());
        InstalmentInfo info = null;
        if (resultList == null || resultList.size() <= 0) {
            info = new InstalmentInfo();
            info.setCompleteInstalment(0);
        } else {
            info = resultList.get(0);
        }
        return info;
    }

    @Override
    public List<RequisitionVO> listRequisitionForWaitpaymentAndFail(Date preEndDay) {
        String sql = "SELECT br.id as requisition_id FROM busi_requisition br LEFT JOIN finance_payment_order f" +
                " ON f.order_number = br.payment_order_number WHERE f.is_manual = 0 and br.requisition_status" +
                " in ('waitpayment', 'failpayment') and br.audit_success_time <= ?";

        return this.createList(sql, new Object[]{preEndDay},new IdRowMapper());

    }
}
