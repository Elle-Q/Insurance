package com.fintech.insurance.micro.finance.service;

import com.alibaba.fastjson.JSONArray;
import com.fintech.insurance.commons.algorithm.ArithmeticUtils;
import com.fintech.insurance.commons.algorithm.ArithmeticVO;
import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.*;
import com.fintech.insurance.commons.exceptions.NullParameterException;
import com.fintech.insurance.commons.utils.CalculationFormulaUtils;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.cache.RedisSequenceFactory;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.dto.biz.*;
import com.fintech.insurance.micro.dto.customer.CustomerBankCardVO;
import com.fintech.insurance.micro.dto.customer.CustomerSimpleVO;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.finance.BankVO;
import com.fintech.insurance.micro.dto.finance.DebtResult;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.dto.finance.OverdueDataVO;
import com.fintech.insurance.micro.dto.support.ConstantConfigVO;
import com.fintech.insurance.micro.feign.biz.ChannelServiceFeign;
import com.fintech.insurance.micro.feign.biz.ContractServiceFeign;
import com.fintech.insurance.micro.feign.biz.RequisitionServiceFeign;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.retrieval.BizQueryFeign;
import com.fintech.insurance.micro.feign.retrieval.RefundQueryFeign;
import com.fintech.insurance.micro.feign.support.ConstantConfigServiceFeign;
import com.fintech.insurance.micro.feign.system.EntityOperationLogServiceFeign;
import com.fintech.insurance.micro.finance.event.RepaymentPlanOverdueEvent;
import com.fintech.insurance.micro.finance.event.RepaymentPlanRefundEvent;
import com.fintech.insurance.micro.finance.event.RepaymentPlanRemindEvent;
import com.fintech.insurance.micro.finance.persist.dao.AccountVoucherDao;
import com.fintech.insurance.micro.finance.persist.dao.RepaymentPlanDao;
import com.fintech.insurance.micro.finance.persist.dao.RepaymentRecordDao;
import com.fintech.insurance.micro.finance.persist.entity.AccountVoucher;
import com.fintech.insurance.micro.finance.persist.entity.RepaymentPlan;
import com.fintech.insurance.micro.finance.persist.entity.RepaymentRecord;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 14:07
 */
@Service
@Transactional
public class RepaymentPlanServiceImpl implements RepaymentPlanService , ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(RepaymentPlanServiceImpl.class);
    @Autowired
    private RepaymentPlanDao repaymentPlanDao;

    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    @Autowired
    private ContractServiceFeign contractServiceFeign;

    @Autowired
    private RepaymentRecordDao repaymentRecordDao;

    @Autowired
    private AccountVoucherDao accountVoucherDao;

    @Autowired
    private EntityOperationLogServiceFeign entityOperationLogServiceFeign;

    @Autowired
    private BizQueryFeign bizQueryFeign;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ChannelServiceFeign channelServiceFeign;

    @Autowired
    RedisSequenceFactory redisSequenceFactory;

    @Autowired
    private RefundQueryFeign refundQueryFeign;

    @Autowired
    private UserDebtRedisService userDebtRedisService;

    @Autowired
    private ConstantConfigServiceFeign constantConfigServiceFeign;

    private ApplicationContext applicationContext;

    @Autowired
    private RequisitionServiceFeign requisitionServiceFeign;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    @Transactional(readOnly = true)
    public FinanceRepaymentPlanVO getRepaymentPlanById(Integer repaymentPlanId) {

        if (repaymentPlanId == null) {
            throw new IllegalArgumentException("Null repaymentPlanId!");
        }

        RepaymentPlan repaymentPlan = repaymentPlanDao.getById(repaymentPlanId);

        return this.convertToVO(repaymentPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<RefundVO> queryRepaymentPlan(String contractCode, String customerName, RefundStatus refundStatus, String requisitionNumber,
                                                   String carNumber, Date refundBeginDate, Date refundEndDate, Integer pageIndex, Integer pageSize) {
        Long begin = null;
        Long end = null;
        if (refundBeginDate != null) {
            begin = refundBeginDate.getTime();
        }
        if (refundEndDate != null) {
            end = refundEndDate.getTime();
        }

        FintechResponse<Pagination<RefundVO>> response = bizQueryFeign.pageRefundVO(contractCode, customerName,
                refundStatus, requisitionNumber, carNumber, begin, end, pageIndex, pageSize);

        if (!response.isOk()) {
            throw new FInsuranceBaseException(105008);
        }
        if (response.getData() == null) {
            throw new FInsuranceBaseException(101515);
        }

        return response.getData();
    }

    //计算逾期罚金
    private Double caculateOverdueFee(Integer repaymentPlanId, String contractNumber, int overdueDays) {
        RepaymentPlan repaymentPlan = repaymentPlanDao.getById(repaymentPlanId);
        if (repaymentPlan == null) {
            return 0.0;
        }
        /*Set<RepaymentRecord> repaymentRecords = repaymentPlan.getRepaymentRecordSet();
        //判断是否有正在处理中的还款记录，如果有，则不计算逾期罚息*/

        RequisitionVO vo = contractServiceFeign.getRequisition(contractNumber);
        // 逾期滞纳每天利率
        Double overdueFineRate = vo.getOverdueFineRate();

        // 剩余本金
        Double repayCapitalAmount = getRemainRepayCapitalAmount(contractNumber).doubleValue();
        // 逾期罚金
        Double result = repayCapitalAmount * overdueFineRate / 10000 * overdueDays;
        if (result < 0) {
            return 0.0;
        }
        return result;
    }


    @Override
    @Transactional(readOnly = true)
    public Pagination<ContractVO> queryRepaymentPlan(String contractCode, String customerName, String channelName, Integer pageIndex, Integer pageSize) {

        // 在customer中找到对应的customer_id
        Pagination<CustomerVO> pageCustomer = customerServiceFeign.pageAllCustomer(customerName, channelName, null, null, null, 1, 1000).getData();
        if (pageCustomer == null || pageCustomer.getItems() == null || pageCustomer.getItems().size() <= 0) {
            return new Pagination<>();
        }
        List<CustomerVO> customerVOList = pageCustomer.getItems();
        List<Integer> customerIdList = new ArrayList<>();
        for (CustomerVO vo : customerVOList) {
            customerIdList.add(vo.getAccountId());
        }

        Page<RepaymentPlan> page = repaymentPlanDao.queryRepaymentPlan(contractCode, customerIdList, pageIndex, pageSize);
        if (page == null || page.getContent() == null || page.getContent().size() <= 0) {
            return new Pagination<>();
        }
        List<RepaymentPlan> planList = page.getContent();
        List<ContractVO> resultList = new ArrayList<>();
        java.util.Map<Integer, CustomerVO> customerVOMap = new HashMap<>();
        for (CustomerVO customerVO : customerVOList) {
            if (customerVO != null && customerVO.getAccountId() != null) {
                customerVOMap.put(customerVO.getAccountId(), customerVO);
            }
        }

        for (RepaymentPlan plan : planList) {
            ContractVO contractVO = new ContractVO();
            // 合同编号
            contractVO.setContractCode(plan.getContractNumber());
            // 总期数
            contractVO.setTotalPhase(plan.getTotalInstalment());
            // 已还期数，查找该合同的已还款数量
            List<RepaymentPlan> refundPlan = repaymentPlanDao.queryCompletePlan(plan.getContractNumber());
            contractVO.setRefundPhase(refundPlan == null ? 0 : refundPlan.size());

            // 客户名称，渠道名称
            CustomerVO vo = customerVOMap.get(plan.getCustomerId());
            contractVO.setCustomerName(vo.getName());
            contractVO.setChannelName(vo.getChannelOf());

            resultList.add(contractVO);
        }

        return Pagination.createInstance(pageIndex, pageSize, page.getTotalElements(), resultList);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String,FinanceRepaymentPlanVO> findAllRepaymentPlanByContractStatus(ContractStatus contractStatus) {
        List<RepaymentPlan> list = repaymentPlanDao.findRepaymentPlanByContractStatus(contractStatus);
        Map<String,FinanceRepaymentPlanVO> voMap = new HashMap<String,FinanceRepaymentPlanVO>();
        if(list != null && list.size() > 0 ) {
            for (RepaymentPlan plan : list){
                if(StringUtils.isNoneBlank(plan.getContractNumber())) {
                    voMap.put(plan.getContractNumber(),convertToVO(plan));
                }
            }
        }
        return voMap;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinanceRepaymentPlanVO> getListByContractNumber(String contractNumber) {
        List<RepaymentPlan> entityList = repaymentPlanDao.getByContractNumberOrderByCurrentInstalmentAsc(contractNumber);
        List<FinanceRepaymentPlanVO> voList = new ArrayList<FinanceRepaymentPlanVO>();
        for (RepaymentPlan plan : entityList) {
            FinanceRepaymentPlanVO repaymentPlanVO = this.convertToVO(plan);
            if(repaymentPlanVO != null) {
                voList.add(repaymentPlanVO);
            }
        }
        return voList;
    }

    @Override
    @Transactional
    public void dealWithHuman(Integer repaymentPlanId) {
        // 找到该合同还款计划的已还款的最后一期的下一期，设置人工处理标识
        RepaymentPlan repaymentPlan = repaymentPlanDao.getById(repaymentPlanId);
        repaymentPlan.setManualFlag(true);
        repaymentPlanDao.save(repaymentPlan);
    }

    private Double getOverdueFeeByRepaymentPlanAndOverdueDays(RepaymentPlan plan, int overdueDays){
        // 分期还款计划对应的申请单
        FintechResponse<RequisitionVO> response = requisitionServiceFeign.getRequisitionByContractNumber(plan.getContractNumber());
        if (!response.isOk() || response.getData() == null) {
            return 0.0;
        }
        Double overdueFines = CalculationFormulaUtils.getOverdueFines(plan.getRestCapitalAmount().doubleValue(),
                plan.getRepayCapitalAmount().doubleValue(), response.getData().getOverdueFineRate(), overdueDays);
        return overdueFines;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void confirmRefund(RecordVO recordVO) {
        // 找到该合同还款计划的已还款的最后一期的下一期，设置人工处理标识
        RepaymentPlan repaymentPlan = repaymentPlanDao.getById(recordVO.getRepaymentPlanId());

        if (null == repaymentPlan) {
            throw new FInsuranceBaseException(101516);
        }
        //已近退保，已近完成,还在初始状态不允许人工去人还款
        if(repaymentPlan.getRepayStatus() == RefundStatus.HAS_WITHDRAW || repaymentPlan.getRepayStatus() == RefundStatus.HAS_REFUND
                || repaymentPlan.getRepayStatus() == RefundStatus.INIT_REFUND){
            throw new FInsuranceBaseException(101517);
        }
        if (repaymentPlan.getManualFlag()) { // 还款计划设置了人工处理标识，上传凭证
            RepaymentRecord record = new RepaymentRecord();
            // 设置逾期罚金
            record.setRepaymentPlan(repaymentPlan);
            record.setRepayDate(new Date());
            record.setRepayTime(new Date());
            record.setRepayCapitalAmount(repaymentPlan.getRepayCapitalAmount().longValue());
            record.setRepayInterestAmount(repaymentPlan.getRepayInterestAmount().longValue());
            record.setOverdueInterestAmount(this.getOverdueFeeByRepaymentPlanAndOverdueDays(repaymentPlan, recordVO.getOverdueDays()).longValue());
            // 还款总金额 = 罚息 + 本金 + 利息
            record.setRepayTotalAmount(record.getRepayCapitalAmount() + record.getRepayInterestAmount() + record.getOverdueInterestAmount());

            record.setOverdueFlag(true);
            record.setPrepaymentFlag(false);

            record.setConfirmBy(FInsuranceApplicationContext.getCurrentUserId());//设置操作人
            record.setConfirmTime(new Date());
            record.setConfirmStatus(DebtStatus.SETTLED.getCode());
            record.setTransactionSerial("manual refund");

            record.setPrepaymentPenaltyAmount(0L);

            repaymentRecordDao.save(record);

            // 设置已还款
            updateRepaymentPlanStatusByEvent(repaymentPlan, InstallmentEvent.ConfirmPaymentByCSEvent);

            AccountVoucher accountVoucher = new AccountVoucher();
            accountVoucher.setAccountType(AccountType.REFUND.getCode());
            accountVoucher.setRepaymentRecord(record);
            accountVoucher.setTransactionSerial(redisSequenceFactory.generateSerialNumber(BizCategory.VH));
            accountVoucher.setUserId(FInsuranceApplicationContext.getCurrentUserId());
            accountVoucher.setVoucher(JSONArray.toJSON(recordVO.getImgKey()).toString());
            accountVoucher.setAccountAmount(new BigDecimal(record.getRepayTotalAmount().doubleValue()));
            accountVoucher.setRemark(recordVO.getRemark());
            FintechResponse<ContractVO> contractVOResponse = contractServiceFeign.getContractDetailByContractNumber(repaymentPlan.getContractNumber());
            if(contractVOResponse.isOk() && contractVOResponse.getData() != null) {
                accountVoucher.setRequisitionCode(contractVOResponse.getData().getRequisitionNumber());
                accountVoucher.setRequisitionId(contractVOResponse.getData().getRequisitionId());
            }
            accountVoucherDao.save(accountVoucher);

            // 生成操作记录
            OperationRecordVO operationrRecordVO = new OperationRecordVO();
            operationrRecordVO.setOperationRemark(recordVO.getRemark());
            operationrRecordVO.setEntityType(RepaymentPlan.class.getSimpleName());
            operationrRecordVO.setOperationType(OperationType.REFUND.getCode());
            // 拿到订单id
            FintechResponse<RequisitionVO> response = requisitionServiceFeign.getProductTypeAndRepayDayTypeByContractNumber(repaymentPlan.getContractNumber());
            if (response == null || response.getData() == null) {
                throw new FInsuranceBaseException(104505);
            }
            RequisitionVO requisitionVO = response.getData();
            operationrRecordVO.setEntityId(requisitionVO.getId());
            operationrRecordVO.setUserId(FInsuranceApplicationContext.getCurrentUserId());
            entityOperationLogServiceFeign.createLog(operationrRecordVO);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void confirmReturnInsurance(String contractNumber) {
        List<RepaymentPlan> repaymentPlans = repaymentPlanDao.getByContractNumber(contractNumber);
        if (0 == repaymentPlans.size()) {
            throw new FInsuranceBaseException(105931);
        }
        for (RepaymentPlan repaymentPlan : repaymentPlans) {
            if (repaymentPlan.getRepayStatus() != RefundStatus.HAS_REFUND) {
                repaymentPlan.setRepayStatus(RefundStatus.HAS_WITHDRAW);
                repaymentPlanDao.save(repaymentPlan);
            }
        }
        contractServiceFeign.updateContractStatus(contractNumber, ContractStatus.InsuranceReturned.getCode());
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<FinanceRepaymentPlanVO> pageRepaymentPlanByCustomeId(Integer customeId, Integer days, Integer pageIndex, Integer pageSize) {
        Page<Object[]> pageObject = repaymentPlanDao.pageWaitingForPayRepaymentPlanByCustomeId(customeId, days, pageIndex, pageSize);
        List<FinanceRepaymentPlanVO> repaymentPlanVOS = new ArrayList<>();
        if (null != pageObject && pageObject.getContent().size() > 0){
            for (Object[] r : pageObject.getContent()) {
                FinanceRepaymentPlanVO repaymentPlanVO = new FinanceRepaymentPlanVO();
                repaymentPlanVO.setRepayDate((Date)r[0]);
                repaymentPlanVO.setRepayTotalAmount(((BigDecimal)r[1]).doubleValue());
                repaymentPlanVO.setId((Integer)r[2]);
                repaymentPlanVOS.add(repaymentPlanVO);
            }
        }
        return Pagination.createInstance(pageIndex, pageSize, pageObject != null ? pageObject.getTotalElements() : 0, repaymentPlanVOS);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public VoidPlaceHolder createInitRepaymentPlan(FinanceRepaymentPlanVO repaymentPlanVO) {
        if(repaymentPlanVO == null || repaymentPlanVO.getTotalInstalment() == null || repaymentPlanVO.getTotalInstalment() <= 0){
            throw new FInsuranceBaseException(105928);
        }
        List<RepaymentPlan> plans = new ArrayList<RepaymentPlan>();
        for(int i = 1; i<= repaymentPlanVO.getTotalInstalment(); i++ ){
            RepaymentPlan newRepaymentPlan = createInitRepaymentPlan(repaymentPlanVO, i);
            plans.add(newRepaymentPlan);
        }

        // 按还款计划的分期期数降序排列以计算剩余本金
        Collections.sort(plans, new Comparator<RepaymentPlan>() {
            @Override
            public int compare(RepaymentPlan o1, RepaymentPlan o2) {
                return o2.getCurrentInstalment().compareTo(o1.getCurrentInstalment());
            }
        });
        BigDecimal restCapital = new BigDecimal(0.0d);
        for (RepaymentPlan plan : plans) {
            plan.setRestCapitalAmount(restCapital);
            restCapital = restCapital.add(plan.getRepayCapitalAmount());
            repaymentPlanDao.save(plan);
        }

        return new VoidPlaceHolder();
    }

    //创建还款记录参数
    private RepaymentPlan createInitRepaymentPlan(FinanceRepaymentPlanVO repaymentPlanVO, Integer repayBusinessDuration){
        RepaymentPlan repaymentPlan = new RepaymentPlan();
        //客户id,关联cust_account表的主键
        repaymentPlan.setCustomerId(repaymentPlanVO.getCustomerId());
        //合同编号
        repaymentPlan.setContractNumber(repaymentPlanVO.getContractNumber());
        //渠道id
        repaymentPlan.setChannelId(repaymentPlanVO.getChannelId());
        //还款日
        repaymentPlan.setRepayDate(CalculationFormulaUtils.getRepayDate(new Date(), repayBusinessDuration, repaymentPlanVO.getAdvanceRepayDays(), repaymentPlanVO.getType()));
        try {
            //分期还款本金金额
            repaymentPlan.setRepayCapitalAmount(CalculationFormulaUtils.getRepayPrincipal(new BigDecimal(repaymentPlanVO.getRepayTotalAmount()), repaymentPlanVO.getTotalInstalment(), repayBusinessDuration, repaymentPlanVO.getInterestRate(), repaymentPlanVO.getType(), repaymentPlanVO.getProductType()));
            //分期还款利息金额
            repaymentPlan.setRepayInterestAmount(CalculationFormulaUtils.getRepayInterest(new BigDecimal(repaymentPlanVO.getRepayTotalAmount()), repaymentPlanVO.getTotalInstalment(), repayBusinessDuration, repaymentPlanVO.getInterestRate(), repaymentPlanVO.getType(), repaymentPlanVO.getProductType()));
        }catch (NullParameterException e) {
            LOG.error("createInitRepaymentPlan failed with customerId=[" + repaymentPlanVO.getCustomerId() + "]," +
                    "channelId=[" + repaymentPlanVO.getChannelId() + "],repayBusinessDuration=[" + repayBusinessDuration + "]",e);
            throw new FInsuranceBaseException(104107);
        }
        //分期还款当期总金额，单位为分
        repaymentPlan.setRepayTotalAmount(repaymentPlan.getRepayCapitalAmount().add(repaymentPlan.getRepayInterestAmount()));
        //还款记录所在的期数
        repaymentPlan.setCurrentInstalment(repayBusinessDuration);
        //总期数
        repaymentPlan.setTotalInstalment(repaymentPlanVO.getTotalInstalment());
        //先临时设置还款计划的剩余本金为0
        repaymentPlan.setRestCapitalAmount(BasicConstants.NUMBER_BIGDECIMAL_FORMAT_ZERO);
        //还款状态
        repaymentPlan.setRepayStatus(RefundStatus.INIT_REFUND);
        //人工干预标识
        repaymentPlan.setManualFlag(false);
        // 创建时间
        repaymentPlan.setCreateAt(new Date());
        repaymentPlanDao.save(repaymentPlan);
        return repaymentPlan;
    }

    public void withHold(Integer repaymentPlanId) {
        RepaymentPlan repaymentPlan = repaymentPlanDao.getById(repaymentPlanId);
        if (repaymentPlan == null) {
            throw new FInsuranceBaseException(105008);
        }

        // 检查所有还款记录，是否有已经还款成功过或正在处理中的扣款记录
        List<RepaymentRecord> repaymentRecords = repaymentRecordDao.listByRepaymentPlan_Id(repaymentPlanId);
        if (null != repaymentRecords && !repaymentRecords.isEmpty()) {
            for (RepaymentRecord record : repaymentRecords) {
                if (!DebtStatus.FAILED.getCode().equals(record.getConfirmStatus())) {
                    throw new FInsuranceBaseException(105017);
                }
            }
        }
        FintechResponse<CustomerSimpleVO> response = customerServiceFeign.getCustomerSimpleInfo(repaymentPlan.getCustomerId());
        if (response == null || response.getData() == null) {
            throw new FInsuranceBaseException(103007);
        }
        CustomerSimpleVO simpleVO = response.getData();
        if (response.getData() == null) {
            throw new FInsuranceBaseException(101515);
        }

        // 检查用户当日是否扣款次数可用
        if (!userDebtRedisService.isDebtAvailable(simpleVO.getBankCardNumber())) {
            throw new FInsuranceBaseException(105016);
        }

        Double debtAmount = 0.0;//扣款金额
        Double overdueFine = 0.0;//逾期罚金

        // 拿到订单，查看订单的还款日类型，产品类型
        FintechResponse<RequisitionVO> fintechResponse = requisitionServiceFeign
                .getProductTypeAndRepayDayTypeByContractNumber(repaymentPlan.getContractNumber());
        if (fintechResponse == null || fintechResponse.getData() == null) {
            throw new FInsuranceBaseException(104534);
        }
        RequisitionVO vo = fintechResponse.getData();
        // 计算出该期需要还款金额
        Date currentDate = DateCommonUtils.getCurrentDate();
        if (currentDate.after(repaymentPlan.getRepayDate())) { // 已逾期计算逾期罚金
            FinanceRepaymentPlanVO financeRepaymentPlanVO = new FinanceRepaymentPlanVO(repaymentPlan.getId(), repaymentPlan.getContractNumber(),
                    repaymentPlan.getRepayDate(), repaymentPlan.getRestCapitalAmount().doubleValue(), repaymentPlan.getRepayCapitalAmount().doubleValue());
            OverdueDataVO overdueDataVO = this.getOverdueDataVOByRepaymentPlanVO(financeRepaymentPlanVO);
            if (null != overdueDataVO) {
                overdueFine = overdueDataVO.getOverdueFines();
            }
        }
        debtAmount = repaymentPlan.getRepayTotalAmount().doubleValue() + overdueFine;
        // 发起扣款请求
        DebtResult debtResult = getDebtResultByDebtAmount(repaymentPlan, simpleVO, debtAmount, overdueFine, vo.getRequisitionNumber());
        if (null == debtResult || !debtResult.getIsSuccess()) {
            // 扣款失败
            throw new FInsuranceBaseException(105010);
        }
        LOG.info("发起扣款指令成功, repaymentPlanId = " + repaymentPlanId);
    }

    //开启一个事物提交扣款保存记录
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public DebtResult getDebtResultByDebtAmount(RepaymentPlan repaymentPlan, CustomerSimpleVO simpleVO,  Double debtAmount, Double overdueFine, String requisitionNumber){
        DebtResult debtResult =  paymentService.debtAmount(simpleVO.getCustomerName(),
                simpleVO.getIdNumber(), simpleVO.getBankCardNumber(), simpleVO.getPhone(), debtAmount, YjfDebtInfoPrefix.REQUSITION.getSuffix() + requisitionNumber);
        if(debtResult != null){
            RepaymentRecord record = new RepaymentRecord();
            record.setRepaymentPlan(repaymentPlan);
            record.setRepayDate(repaymentPlan.getRepayDate());
            record.setRepayTime(new Date());
            record.setRepayTotalAmount(debtAmount.longValue());
            record.setRepayCapitalAmount(repaymentPlan.getRepayCapitalAmount().longValue());
            record.setRepayInterestAmount(repaymentPlan.getRepayInterestAmount().longValue());
            record.setOverdueInterestAmount(overdueFine.longValue());
            record.setOverdueFlag(!(overdueFine == 0.0));
            record.setPrepaymentFlag(false);
            record.setPrepaymentPenaltyAmount(0L);
            record.setBankAccountNumber(simpleVO.getBankCardNumber());
            record.setTransactionSerial(debtResult.getRequestSerialNum());
            record.setRepayBatchNo(null);// 人工扣款不设批次号
            record.setConfirmStatus(debtResult.getIsSuccess() ? DebtStatus.PROCESSING.getCode() : DebtStatus.FAILED.getCode());
            record.setCreateAt(new Date());
            if (!debtResult.getIsSuccess()) {
                record.setRemark(debtResult.getFailedMessage());
            }
            repaymentRecordDao.save(record);

            if(!debtResult.getIsSuccess()){
                // 更新还款记录和还款计划状态
                this.updateRepaymentPlanStatusByEvent(repaymentPlan, InstallmentEvent.AutoRefundFailedEvent);
                //当天第一次执行代扣失败推送短信给用户
                LOG.info("the withold is the first debt on today: {}", userDebtRedisService.isFirstDebtOnToday(record.getBankAccountNumber(), record.getTransactionSerial()));
                if (userDebtRedisService.isFirstDebtOnToday(record.getBankAccountNumber(), record.getTransactionSerial())
                        && (RefundStatus.OVERDUE == repaymentPlan.getRepayStatus() || RefundStatus.WAITING_WITHDRAW == repaymentPlan.getRepayStatus())) {
                    //逾期还款第一次还款失败,或者失败支付待退保发送微信消息给用户
                    RepaymentPlanOverdueEvent repaymentPlanOverdueEvent = new RepaymentPlanOverdueEvent(repaymentPlan.getCustomerId(),
                            repaymentPlan.getRepayDate(), Math.abs(DateCommonUtils.intervalDays(repaymentPlan.getRepayDate(), DateCommonUtils.getCurrentDate())),
                            record.getRepayTotalAmount(), record.getOverdueInterestAmount().intValue());
                    applicationContext.publishEvent(repaymentPlanOverdueEvent);
                }
            }
        }
        return debtResult;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRepaymentPlanByContractNumbers(List<String> contractNumberStr) {
        if(contractNumberStr == null || contractNumberStr.size() < 1){
            return;
        }
        List<RepaymentPlan> repaymentPlanList = repaymentPlanDao.findByContractNumbers(contractNumberStr);
        if(repaymentPlanList != null && repaymentPlanList.size() > 0){
            for(RepaymentPlan plan : repaymentPlanList) {
                repaymentPlanDao.deleteEntity(plan);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void debitForRepayDay(Integer customerId) {
        Date repayDate = DateCommonUtils.getToday();
        List<RefundStatus> statuses = new ArrayList<>();
        statuses.add(RefundStatus.WAITING_REFUND);
        statuses.add(RefundStatus.FAIL_REFUND);
        LOG.info("Debt for repay date on {} for customer: {}", DateCommonUtils.getDateFormat(repayDate), customerId);
        List<RepaymentPlan> repaymentPlanList = repaymentPlanDao.listByCurrentDateAndStatus(repayDate, statuses);//当日还款的还款计划(包括还款中和还款失败的还款计划)，过滤人工处理
        LOG.info("preparing to group repaymentPlanList with size[" + repaymentPlanList.size() + "]");

        // 对代还款的还款计划按用户（银行卡）进行分组
        Map<String, List<RepaymentPlan>> planGroupMap = new HashMap<String, List<RepaymentPlan>>();
        for (RepaymentPlan repaymentPlan : repaymentPlanList) {
            LOG.info("retrieval repayment plan: {}, customer ID: {}", repaymentPlan.getId(), repaymentPlan.getCustomerId());
            if (null != customerId && !customerId.equals(repaymentPlan.getCustomerId())) {
                continue;
            }
            String key = String.format("%s", repaymentPlan.getCustomerId());
            if (!planGroupMap.containsKey(key)) {
                planGroupMap.put(key, new ArrayList<RepaymentPlan>());
            }
            planGroupMap.get(key).add(repaymentPlan);
        }

        LOG.info("preparing to debt for " + planGroupMap.keySet().size() + " group planList");

        // 对每个分组进行银行卡扣款
        for (List<RepaymentPlan> planGroup : planGroupMap.values()) {
            LOG.info("preparing to debt for current groupPlanList with size[ " + planGroup.size() + "]");
            try {
                this.debtForPlanGroupByCustomer(planGroup, false);
                LOG.info("debt for current group for customer: {}", planGroup.get(0).getCustomerId());
            } catch (Exception e) {
                LOG.error("Failed to debt for plan group: " + e.getMessage(), e);
            }
        }
    }

    // 对分组进行银行卡扣款
    @Override
    @Transactional(readOnly = true)
    public boolean debtForPlanGroupByCustomer(List<RepaymentPlan> planGroup, Boolean overdueFlag) {
        FintechResponse<ChannelVO> channelVOFintechResponse = channelServiceFeign.getChannelDetail(planGroup.get(0).getChannelId());
        if (!channelVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(channelVOFintechResponse);
        }
        ChannelVO channelVO = channelVOFintechResponse.getData();
        CustomerVO customerVO = null;
        if (null != channelVO) {
            FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getByCustomerIdAndChannelCode(planGroup.get(0).getCustomerId(), channelVO.getChannelCode());
            if (!customerVOFintechResponse.isOk()) {
                throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
            }
            if (null != customerVOFintechResponse.getData()) {
                customerVO = customerVOFintechResponse.getData();//客户信息
            }
        }
        LOG.info("current groupRepaymentPlanList with customerId[" + planGroup.get(0).getCustomerId() +
                "] and channelId[" + planGroup.get(0).getChannelId());

        if (null != customerVO) {
            //1. 当日用户可用额度信息
            Long todayAvailableDebtAmout = findBestArrayToDebtByPlanList(customerVO);
            if (todayAvailableDebtAmout <= 0) {//判断用户当日是否还有可扣款额度，没有则发送微信推送消息
                LOG.info("customer {} no available debt amout: {}", customerVO.getAccountInfoId(), todayAvailableDebtAmout);
                return false;
            }

            //2. 计算最佳合并扣款组合
            List<RepaymentPlan> debtPlans = this.buildCombineDebtPlans(planGroup, todayAvailableDebtAmout);

            //对最优组合扣款扣款
            if (debtPlans.isEmpty()) {//当前额度已用完，尚有还款计划需要还， 推送消息给用户
                /*Double todayDebtAmout = 0.0;
                for (RepaymentPlan r : planGroup) {
                    todayDebtAmout += r.getRepayTotalAmount().doubleValue();
                }
                RepaymentPlanRefundEvent repaymentPlanRefundEvent = RepaymentPlanRefundEvent.refundFailEvent(customerVO.getAccountId(),
                        todayDebtAmout, new Date(), "没有可用额度");
                applicationContext.publishEvent(repaymentPlanRefundEvent);

                LOG.error("consider the todayAvailableDebtAmout: {}, no repayment plan could be work.", todayAvailableDebtAmout);
                return false;*/
                // 在无可用额度的情况下，依然把所有的还款计划全部合并做一下扣款： 可以通过还款的失败来更新还款计划的状态，以此来通知系统的用户以及客户.
                LOG.info("No avaiale debt amout, but do that for making the refund failedL, customer: {}, with plan size: {}", customerVO.getAccountId(), planGroup.size());
                this.debtForPlanList(planGroup, customerVO, overdueFlag);
                LOG.info("No avaiale debt amout, but do that success for making the refund failedL, customer: {}", customerVO.getAccountId());
            } else {
                LOG.info("the last step : preparing to debt for customer {} with {} debtPlans", customerVO.getAccountInfoId(), debtPlans.size());
                this.debtForPlanList(debtPlans, customerVO, overdueFlag);
                LOG.info("debt done for customer Account Info Id: {}", customerVO.getAccountInfoId());
            }
        }
        return true;
    }

    //当日用户可用额度信息
    private Long findBestArrayToDebtByPlanList(CustomerVO customerVO) {
        CustomerBankCardVO customerBankCardVO = customerServiceFeign.getCustomerBankCard(customerVO.getAccountId()).getData();
        BankVO bankVO = null;
        Long todayAvailableDebtAmout = 0L;
        if (null != customerBankCardVO) {
            if (!userDebtRedisService.isDebtAvailable(customerBankCardVO.getAccountNumber())) {
                LOG.info("the customer with accountId[" + customerVO.getAccountId() + "] has no times to debt for today" );
                return 0L;
            }
            bankVO = bizQueryFeign.getBankInfoByCode(customerBankCardVO.getAccountBank()).getData();
            LOG.info("the customer with accountId[" + customerVO.getAccountId() + "] has bankCard which code is :" + customerBankCardVO.getAccountBank());

            Long dailyLimit = 0L;//银行当日限额
            Long singleLimit = 0L;//银行单笔限额
            if (null != bankVO) {
                dailyLimit = (null == bankVO.getDailyLimit() || 0 == bankVO.getDailyLimit()) ? Long.MAX_VALUE : bankVO.getDailyLimit().longValue();
                singleLimit = (null == bankVO.getSingleLimit() || 0 == bankVO.getSingleLimit()) ? Long.MAX_VALUE : bankVO.getSingleLimit().longValue();
            }

            LOG.info("dailyLimit is : {} and singleLimit is : {}", dailyLimit, singleLimit);

            // 该银行卡当日已使用扣款额度
            Long todayUsedAmount = userDebtRedisService.countTotalDebtedAmount(customerBankCardVO.getAccountNumber()).longValue();
            LOG.info("today used amount is : {}", todayUsedAmount);

            todayAvailableDebtAmout = Math.min(singleLimit, dailyLimit - todayUsedAmount);
            LOG.info("today available debtAmout is : {}", todayAvailableDebtAmout);
        }
        return todayAvailableDebtAmout;
    }

    //2. 计算合并扣款的最佳组合
    private List<RepaymentPlan> buildCombineDebtPlans(List<RepaymentPlan> planGroup, Long availableDebtAmount) {
        List<ArithmeticVO> arithmeticVOS = new ArrayList<ArithmeticVO>();
        String groupPlanIds = "";
        for (RepaymentPlan plan : planGroup) {
            groupPlanIds += plan.getId() + ",";
            LOG.info("plan: {}, Amount: {}", plan.getId(), plan.getRepayTotalAmount());
            arithmeticVOS.add(new ArithmeticVO(plan.getId(), plan.getRepayTotalAmount().longValue()));
        }
        LOG.info("buildCombineDebtPlans for plan with id: ", groupPlanIds);
        List<ArithmeticVO> bestDebtVOs = ArithmeticUtils.dpNew(availableDebtAmount, arithmeticVOS, 0);
        List<Integer> debtPlanIds = new ArrayList<Integer>();
        for (ArithmeticVO vo : bestDebtVOs) {
            debtPlanIds.add(vo.getEntityId());
        }
        LOG.info("current best debt plans: {}", StringUtils.join(debtPlanIds.iterator(), ", "));

        // 需要发起合并扣款的还款计划
        String combineDebtPlanIds = "";
        List<RepaymentPlan> debtPlans = new ArrayList<RepaymentPlan>();
        for (RepaymentPlan plan : planGroup) {
            if (debtPlanIds.contains(plan.getId())) {
                debtPlans.add(plan);
                combineDebtPlanIds += plan.getId() + ",";
            }
        }
        LOG.info("success to build combine DebtPlans with id: ", combineDebtPlanIds);
        return debtPlans;
    }

    //针对客户还款计划进行扣款
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void debtForPlanList(List<RepaymentPlan> repaymentPlans, CustomerVO customerVO, Boolean overdueFlag) {
        CustomerBankCardVO customerBankCardVO = customerServiceFeign.getCustomerBankCard(customerVO.getAccountId()).getData();
        Double totalAmount = 0.0;//批次还款总金额
        Double overdueFee = 0.0;//逾期罚金
        String batchNo = redisSequenceFactory.generateSerialNumber(BizCategory.DEBT_BATCH);//还款批次号

        StringBuffer debtContractInfo = new StringBuffer(YjfDebtInfoPrefix.REPAYMENTPLAN.getSuffix());
        StringBuilder debtPlanIds = new StringBuilder();
        if (null != customerBankCardVO) {
            List<RepaymentRecord> repaymentRecords = new ArrayList<RepaymentRecord>();
            for (RepaymentPlan plan : repaymentPlans) {
                if (overdueFlag) {//计算逾期罚金
                    FinanceRepaymentPlanVO financeRepaymentPlanVO = new FinanceRepaymentPlanVO(plan.getId(), plan.getContractNumber(),
                            plan.getRepayDate(), plan.getRestCapitalAmount().doubleValue(), plan.getRepayCapitalAmount().doubleValue());
                    OverdueDataVO overdueDataVO = this.getOverdueDataVOByRepaymentPlanVO(financeRepaymentPlanVO);
                    if (null != overdueDataVO) {
                        overdueFee = overdueDataVO.getOverdueFines();
                    }
                }
                totalAmount += plan.getRepayTotalAmount().doubleValue() + overdueFee;

                LOG.info("current repaymentPlan {}: isOverdue :{}, and the overdueFee is {}, customer with id :{}" +
                        " should pay totalAmount :{}", plan.getId(), overdueFlag, overdueFee, customerVO.getAccountId(), totalAmount);

                // 生成还款记录
                RepaymentRecord repaymentRecord = this.createRepaymentRecord(plan, overdueFlag, customerBankCardVO, batchNo, overdueFee);
                repaymentRecords.add(repaymentRecord);

                // 构造扣款涉及到的合同编号
                debtContractInfo.append(plan.getId()).append(",");
                debtPlanIds.append(plan.getId()).append(",");
            }
            LOG.info("Debt amout for customer: {}, amount: {}, relative contracts: {}, relative repayment plans:{}", customerVO.getName(),
                    totalAmount, debtContractInfo, debtPlanIds.toString());

            DebtResult debtResult = paymentService.debtAmount(customerVO.getName(), customerVO.getIdNum(),
                    customerBankCardVO.getAccountNumber(), customerBankCardVO.getAccountMobile(),
                    totalAmount, debtContractInfo.toString());

            LOG.info("current debtStatus is :{}", debtResult.getIsSuccess());

            // 更新还款记录状态
            this.updateRepaymentPlanAndRecordStatus(debtResult, repaymentRecords);
            for (RepaymentRecord repaymentRecord : repaymentRecords) {
                LOG.info("update debtStatus success for repayment records: ", repaymentRecord.getId());
            }
        }
        LOG.info("Batch debt for user: {} success", customerVO.getAccountInfoId());
    }

    //生成还款记录
    private RepaymentRecord createRepaymentRecord(RepaymentPlan r, Boolean overdueFlag, CustomerBankCardVO customerBankCardVO, String batchNo, Double overdueFee) {
        RepaymentRecord record = new RepaymentRecord();
        record.setRepaymentPlan(r);
        record.setRepayDate(r.getRepayDate());
        record.setRepayTime(DateCommonUtils.getToday());
        record.setRepayTotalAmount(r.getRepayTotalAmount().longValue());
        record.setRepayCapitalAmount(r.getRepayCapitalAmount().longValue());
        record.setRepayInterestAmount(r.getRepayInterestAmount().longValue());
        record.setOverdueInterestAmount(overdueFee.longValue());
        record.setOverdueFlag(overdueFlag);
        record.setPrepaymentFlag(false);
        record.setPrepaymentPenaltyAmount(0L);
        record.setBankAccountNumber(customerBankCardVO.getAccountNumber());
        record.setRepayBatchNo(batchNo);
        record.setConfirmStatus(DebtStatus.PROCESSING.getCode());
        record.setCreateAt(new Date());

        return record;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinanceRepaymentPlanVO> listRepaymentPlanByStatus(String refundStatus) {
        List<RepaymentPlan> entityList = repaymentPlanDao.listRepaymentPlanByStatus(refundStatus);
        if (entityList == null || entityList.size() <= 0) {
            return Collections.emptyList();
        }
        List<FinanceRepaymentPlanVO> voList = new ArrayList<>();
        for (RepaymentPlan plan : entityList) {
            FinanceRepaymentPlanVO repaymentPlanVO = convertToVO(plan);
            if(repaymentPlanVO != null) {
                voList.add(repaymentPlanVO);
            }
        }
        return voList;
    }

    public void changeStatusToSurrenderByContractNumber(List<String> contractNumbers) {
        List<RepaymentPlan> repaymentPlanList = repaymentPlanDao.findWaitingForToSurrenderByContractNubmers(contractNumbers);
        String toSurrenderRepaymentPlanIds = "";
        if (null != repaymentPlanList || repaymentPlanList.size() > 0) {
            for (RepaymentPlan r : repaymentPlanList) {
                toSurrenderRepaymentPlanIds += r.getId() + ",";
                LOG.info("preparing change status for RepaymentPlan[" + r.getId() + "] who's status is :" +
                        r.getRepayStatus().getCode() + " to new status : " + RefundStatus.WAITING_WITHDRAW.getCode());
                 r.setUpdateAt(new Date());
                 r.setRepayStatus(RefundStatus.WAITING_WITHDRAW);
                repaymentPlanDao.save(r);
            }
        }

        LOG.info("success changing status to waiting_withdraw for repaymentPlanList with id : {}", toSurrenderRepaymentPlanIds);

    }

    @Override
    public void debitForOverdue(Integer customerId) {
        LOG.info("Debt for overdue for customer: {}", customerId);
        List<FinanceRepaymentPlanVO> repaymentPlanVOList  = refundQueryFeign.listPlansForOverdue().getData();//已逾期的还款计划，过滤人工处理
        LOG.info("preparing to debt repaymentPlanForOverdueList's size is " + repaymentPlanVOList.size());

        // 对待还款的还款计划按用户（银行卡）进行分组
        Map<String, List<FinanceRepaymentPlanVO>> planGroupMap = new HashMap<String, List<FinanceRepaymentPlanVO>>();
        for (FinanceRepaymentPlanVO repaymentPlanVO : repaymentPlanVOList) {
            if (null != customerId && !customerId.equals(repaymentPlanVO.getCustomerId())) {
                continue;
            }
            String key = String.format("%s", repaymentPlanVO.getCustomerId());
            if (!planGroupMap.containsKey(key)) {
                planGroupMap.put(key, new ArrayList<FinanceRepaymentPlanVO>());
            }
            planGroupMap.get(key).add(repaymentPlanVO);
        }

        LOG.info("preparing to debt for " + planGroupMap.keySet().size() + " group planList");

        // 对每个分组进行银行卡扣款
        for (List<FinanceRepaymentPlanVO> planGroup : planGroupMap.values()) {
            List<RepaymentPlan> repaymentPlanList = new ArrayList<>();
            List<String> planGroupIds = new ArrayList<String>();
            for (FinanceRepaymentPlanVO repaymentPlanVO : planGroup) {
                repaymentPlanList.add(repaymentPlanDao.getById(repaymentPlanVO.getId()));
                planGroupIds.add(String.valueOf(repaymentPlanVO.getId()));
            }
            LOG.info("current planGroup with id : {} ", StringUtils.join(planGroupIds.iterator(), ","));
            try {
                boolean result = this.debtForPlanGroupByCustomer(repaymentPlanList, true);
                LOG.info("debt status: {}", result);
            } catch (Exception e) {
                LOG.error("send debt request failed on " + e.getMessage(), e);
            }
        }
    }

    private void updateRepaymentPlanAndRecordStatus(DebtResult debtResult, List<RepaymentRecord> debtRecords) {
        Double debtAmount = 0.0;
        Double overdueInterestAmount = 0.0;
        for (RepaymentRecord repaymentRecord : debtRecords) {
            debtAmount += repaymentRecord.getRepayTotalAmount();
            overdueInterestAmount += repaymentRecord.getOverdueInterestAmount();
            repaymentRecord.setConfirmStatus(debtResult.getIsSuccess() ? DebtStatus.PROCESSING.getCode() : DebtStatus.FAILED.getCode());//更改还款记录状态为失败
            repaymentRecord.setTransactionSerial(debtResult.getRequestSerialNum());
            repaymentRecord.setUpdateAt(new Date());
            repaymentRecord.setRemark(debtResult.getIsSuccess() ? "" : debtResult.getFailedMessage());
            repaymentRecordDao.save(repaymentRecord);
            // 还款计划状态
            RepaymentPlan rp = repaymentRecord.getRepaymentPlan();
            if (!debtResult.getIsSuccess()) {
                // 更新还款计划状态
                this.updateRepaymentPlanStatusByEvent(rp, InstallmentEvent.AutoRefundFailedEvent);
            }
        }

        if (!debtResult.getIsSuccess()) {
            //逾期还款当天执行代扣失败时推送短信给用户
            try {
                //在扣款失败后查看还款计划的状态是否为已逾期，如果为已逾期则发送微信消息给客户
                if (userDebtRedisService.isFirstDebtOnToday(debtRecords.get(0).getBankAccountNumber(), debtRecords.get(0).getTransactionSerial())
                        && debtRecords.get(0).getOverdueFlag()) { //逾期还款第一次还款失败发送微信消息给用户
                    RepaymentPlanOverdueEvent repaymentPlanOverdueEvent = new RepaymentPlanOverdueEvent(debtRecords.get(0).getRepaymentPlan().getCustomerId(),
                            debtRecords.get(0).getRepaymentPlan().getRepayDate(), Math.abs(DateCommonUtils.intervalDays(debtRecords.get(0).getRepaymentPlan().getRepayDate(),
                            DateCommonUtils.getCurrentDate())), debtAmount, overdueInterestAmount);
                    applicationContext.publishEvent(repaymentPlanOverdueEvent);
                }else {
                    //正常代扣失败推送微信消息和短信
                    RepaymentPlanRefundEvent repaymentPlanRefundEvent = RepaymentPlanRefundEvent.refundFailEvent(debtRecords.get(0).getRepaymentPlan().getCustomerId(),
                            debtAmount, debtRecords.get(0).getRepaymentPlan().getRepayDate(), debtResult.getFailedMessage());
                    applicationContext.publishEvent(repaymentPlanRefundEvent);
                }

                LOG.info("代扣失败发送微信推送消息");

            } catch (Exception e) { // 不触发事务回滚
                LOG.error(e.getMessage(), e);
            }
            LOG.info("fail to debt the error is :{}", debtResult.getFailedMessage());
        }

        LOG.info("update repayment plan and record success for batch debt result.");
    }

    @Override
    public void changeStatusToOverdue() {
        String toSurrenderRepaymentPlanIds = "";
        List<RefundStatus> refundStatuses = new ArrayList<>();
        refundStatuses.add(RefundStatus.WAITING_REFUND);
        refundStatuses.add(RefundStatus.FAIL_REFUND);
        List<RepaymentPlan> repaymentPlanList = repaymentPlanDao.listOverdued(DateCommonUtils.getCurrentDate(), refundStatuses);//查询已逾期的还款计划
        if (null != repaymentPlanList && repaymentPlanList.size() > 0) {
            for (RepaymentPlan r : repaymentPlanList) {//将状态变为已逾期
                LOG.info("preparing change status for RepaymentPlan[" + r.getId() + "] who's status is :" +
                        r.getRepayStatus().getCode() + " to new status : " + RefundStatus.OVERDUE.getCode());
                r.setRepayStatus(RefundStatus.OVERDUE);
                r.setUpdateAt(new Date());
                repaymentPlanDao.save(r);
            }
        }
        LOG.info("success changing status to overdue for repaymentPlanList with id : {}", toSurrenderRepaymentPlanIds);
    }

    //转换还款计划vo
    private FinanceRepaymentPlanVO convertToVO(RepaymentPlan entity) {

        FinanceRepaymentPlanVO vo = null;
        if (entity != null) {
            vo = new FinanceRepaymentPlanVO();
            vo.setId(entity.getId());
            vo.setCustomerId(entity.getCustomerId());
            vo.setContractNumber(entity.getContractNumber());
            vo.setChannelId(entity.getChannelId());
            vo.setRepayDate(entity.getRepayDate());
            vo.setRepayCapitalAmount(entity.getRepayCapitalAmount().doubleValue());
            vo.setRepayInterestAmount(entity.getRepayInterestAmount().doubleValue());
            vo.setRestCapitalAmount(entity.getRestCapitalAmount().doubleValue());
            vo.setCurrentInstalment(entity.getCurrentInstalment());
            vo.setTotalInstalment(entity.getTotalInstalment());
            vo.setManualFlag(entity.getManualFlag());

            if ((entity.getRepayStatus() == RefundStatus.WAITING_REFUND && DateCommonUtils.getToday().after(entity.getRepayDate()))) {
                vo.setRepayStatus(RefundStatus.OVERDUE);
            } else {
                vo.setRepayStatus(entity.getRepayStatus());
            }

            // 是否已经逾期或者曾经逾期
            Boolean overdueFlag = false;
            Double overdueFine = 0.0;
            long overdueDays = 0;
            // 获取未失败的还款记录
            RepaymentRecord record = this.getRepaymentRecordOfNonFailure(entity);
            if (record != null) {
                if (StringUtils.equals(record.getConfirmStatus(), DebtStatus.CONFIRMED.getCode()) ||
                        StringUtils.equals(record.getConfirmStatus(), DebtStatus.SETTLED.getCode())) { // 已还款，设置还款时间
                    vo.setActualRepayDate(record.getRepayTime());
                }
                if (record.getRepayTime() != null &&
                        DateCommonUtils.getBeginDateByDate(record.getRepayTime()).after(DateCommonUtils.getBeginDateByDate(entity.getRepayDate()))) {
                    overdueFlag = true;
                    overdueDays = DateCommonUtils.getDays(vo.getRepayDate(), record.getRepayTime());
                }
                overdueFine = record.getOverdueInterestAmount().doubleValue();
            } else {
                // 不是是第一期期初还款并且逾期
                if(!RefundStatus.HAS_REFUND.equals(entity.getRepayStatus()) &&
                        DateCommonUtils.getToday().after(DateCommonUtils.getBeginDateByDate(vo.getRepayDate()))) {
                    OverdueDataVO overdueDataVO = this.getOverdueDataVOByRepaymentPlanVO(vo);
                    overdueFlag = true;
                    overdueDays = overdueDataVO.getOverdueDays();
                    overdueFine = overdueDataVO.getOverdueFines();
                }
            }

            vo.setOverdueDays(overdueDays);
            vo.setOverdueFine(overdueFine);
            vo.setOverdueFlag(overdueFlag);

            //还款总金额 = 本金 + 利息 + 罚息
            vo.setRepayTotalAmount(entity.getRepayCapitalAmount().add(entity.getRepayInterestAmount()).doubleValue() + overdueFine);
        }
        return vo;
    }

    //转换还款计划vo
    private FinanceRepaymentPlanVO convertToRepaymentPlanVO(RepaymentPlan entity, Integer maxOverdueDays, Double overdueFineRate ) {

        FinanceRepaymentPlanVO vo = null;
        if (entity != null) {
            vo = new FinanceRepaymentPlanVO();
            vo.setId(entity.getId());
            vo.setCustomerId(entity.getCustomerId());
            vo.setContractNumber(entity.getContractNumber());
            vo.setChannelId(entity.getChannelId());
            vo.setRepayDate(entity.getRepayDate());
            vo.setRepayCapitalAmount(entity.getRepayCapitalAmount().doubleValue());
            vo.setRepayInterestAmount(entity.getRepayInterestAmount().doubleValue());
            vo.setRestCapitalAmount(entity.getRestCapitalAmount().doubleValue());
            vo.setCurrentInstalment(entity.getCurrentInstalment());
            vo.setTotalInstalment(entity.getTotalInstalment());
            vo.setManualFlag(entity.getManualFlag());

            if ((entity.getRepayStatus() == RefundStatus.WAITING_REFUND && DateCommonUtils.getToday().after(entity.getRepayDate()))) {
                vo.setRepayStatus(RefundStatus.OVERDUE);
            } else {
                vo.setRepayStatus(entity.getRepayStatus());
            }

            // 是否已经逾期或者曾经逾期
            Boolean overdueFlag = false;
            Double overdueFine = 0.0;
            long overdueDays = 0;
            // 获取未失败的还款记录
            RepaymentRecord record = this.getRepaymentRecordOfNonFailure(entity);
            if (record != null) {
                if (StringUtils.equals(record.getConfirmStatus(), DebtStatus.CONFIRMED.getCode()) ||
                        StringUtils.equals(record.getConfirmStatus(), DebtStatus.SETTLED.getCode())) { // 已还款，设置还款时间
                    vo.setActualRepayDate(record.getRepayTime());
                }
                if (record.getRepayTime() != null &&
                        DateCommonUtils.getBeginDateByDate(record.getRepayTime()).after(DateCommonUtils.getBeginDateByDate(entity.getRepayDate()))) {
                    overdueFlag = true;
                    overdueDays = DateCommonUtils.getDays(vo.getRepayDate(), record.getRepayTime());
                }
                overdueFine = record.getOverdueInterestAmount().doubleValue();
            } else {
                if(DateCommonUtils.getToday().after(DateCommonUtils.getBeginDateByDate(vo.getRepayDate()))) {
                    OverdueDataVO overdueDataVO = this.getOverdueDataVOByRepaymentPlan(vo, maxOverdueDays, overdueFineRate);
                    overdueFlag = true;
                    overdueDays = overdueDataVO.getOverdueDays();
                    overdueFine = overdueDataVO.getOverdueFines();
                }
            }
            vo.setOverdueDays(overdueDays);
            vo.setOverdueFine(overdueFine);
            //还款总金额 = 本金 + 利息 + 罚息
            vo.setRepayTotalAmount(entity.getRepayCapitalAmount().add(entity.getRepayInterestAmount()).doubleValue() + overdueFine);
            vo.setOverdueFlag(overdueFlag);
        }
        return vo;
    }

    private RepaymentRecord getRepaymentRecordOfNonFailure(RepaymentPlan entity) {
        Set<RepaymentRecord> set = entity.getRepaymentRecordSet();
        if (set == null || set.size() == 0) {
            return null;
        }
        for (RepaymentRecord record : set) {
            if(!DebtStatus.FAILED.getCode().equals(record.getConfirmStatus())) {
                return record;
            }
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateRepaymentPlanStatusByEvent(RepaymentPlan repaymentPlan, InstallmentEvent updateEvent) {
        if (null == repaymentPlan || null == updateEvent) {
            throw new IllegalArgumentException("Repayment Plan or targetStatus cannot be null");
        }
        if (null == repaymentPlan.getRepayStatus()) {
            throw new IllegalStateException(String.format("the repayment plan [%s] does not have status."));
        }
        RefundStatus oldStatus = repaymentPlan.getRepayStatus();
        LOG.info("updateRepaymentPlanStatusByEvent with oldStatusCode=["+ oldStatus == null ? "" : oldStatus.getCode() + "]," +
                "repaymentPlanId=[" + repaymentPlan.getId() + "],updateEvent= [" + updateEvent.getCode() +"]");
        RefundStatus targetStatus = oldStatus.transfer(updateEvent);
        if(targetStatus == RefundStatus.FAIL_REFUND && DateCommonUtils.getToday().after(repaymentPlan.getRepayDate())){
            //已逾期还款失败不改状态
            targetStatus = RefundStatus.OVERDUE;
            //获取业务申请单最大的逾期天数
            FintechResponse<RequisitionVO> requisitionVOFintechResponse = requisitionServiceFeign.getRequisitionByContractNumber(repaymentPlan.getContractNumber());
            if(!requisitionVOFintechResponse.isOk()){
                throw FInsuranceBaseException.buildFromErrorResponse(requisitionVOFintechResponse);
            }
            if(requisitionVOFintechResponse.getData() == null || requisitionVOFintechResponse.getData().getMaxOverdueDays() == null){
                throw new FInsuranceBaseException(104929);
            }
            //大于最大的期限变成待退保
            if(DateCommonUtils.getToday().after(DateUtils.addDays(repaymentPlan.getRepayDate(), requisitionVOFintechResponse.getData().getMaxOverdueDays()))){
                targetStatus = RefundStatus.WAITING_WITHDRAW;
            }
        }

        LOG.info("updateRepaymentPlanStatusByEvent with targetStatusCode=["+ targetStatus == null ? "" : targetStatus.getCode() + "]," +
                "repaymentPlanId=[" + repaymentPlan.getId() + "],updateEvent= [" + updateEvent.getCode() +"]");
        repaymentPlan.setRepayStatus(targetStatus);
        LOG.info("Update repayment plan [{}] status from {} to {} by {}", repaymentPlan.getId(),
                repaymentPlan.getRepayStatus().name(), targetStatus.name(), updateEvent.getCode());

        //根据还款计划的当前状态处理联动逻辑
        //如果是待退保和已退保， 更新所有后期还款计划以及合同的状态为待退保和已退保
        if (RefundStatus.HAS_WITHDRAW == targetStatus || RefundStatus.WAITING_WITHDRAW == targetStatus) {
            if (repaymentPlan.getCurrentInstalment() != repaymentPlan.getTotalInstalment()) {
                List<RepaymentPlan> leavingPlans = repaymentPlanDao.findByContractNumberAndCurrentInstalmentGreaterThan(
                        repaymentPlan.getContractNumber(), repaymentPlan.getCurrentInstalment());
                for (RepaymentPlan afterwardPlan : leavingPlans) {
                    afterwardPlan.setRepayStatus(targetStatus);
                }
            }
            //更新合同的状态
            contractServiceFeign.updateContractStatus(repaymentPlan.getContractNumber(), RefundStatus.HAS_WITHDRAW == targetStatus ?
                            ContractStatus.InsuranceReturned.getCode() : ContractStatus.InsuranceReturning.getCode());

        } else if (RefundStatus.HAS_REFUND == targetStatus) {
            // 如果是最后一期还款完成， 需要更新合同的状态为已完成
            if(repaymentPlan.getCurrentInstalment().equals(repaymentPlan.getTotalInstalment())) {
                contractServiceFeign.updateContractStatus(repaymentPlan.getContractNumber(), ContractStatus.Refunded.getCode());
            }
        }
        repaymentPlanDao.save(repaymentPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public void sendMsgForRepayDate() {
        FintechResponse<ConstantConfigVO> constantConfigVOFintechResponse = constantConfigServiceFeign.getAheadRemindDays();
        if (!constantConfigVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(constantConfigVOFintechResponse);
        }
        ConstantConfigVO constantConfigVO = null;
        if (null != constantConfigVOFintechResponse .getData()) {
            constantConfigVO = constantConfigVOFintechResponse.getData();
        }
        Date date = null;
        if (null != constantConfigVO) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, (Integer) constantConfigVO.getConfigValue());
            date = calendar.getTime();
        }

        //待还款的还款计划，过滤人工处理
        FintechResponse<List<FinanceRepaymentPlanVO>> repaymentPlanListResponse = refundQueryFeign.listPlansForWaitingRefundAndRepayDate(date);
        if (!repaymentPlanListResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(repaymentPlanListResponse);
        }
        List<FinanceRepaymentPlanVO> repaymentPlanVOList = new ArrayList<>();
        if (null != repaymentPlanListResponse.getData()) {
            repaymentPlanVOList = repaymentPlanListResponse.getData();
        }

        // 对待还款的还款计划按用户（银行卡）进行分组
        Map<String, List<RepaymentPlan>> planGroupMap = new HashMap<String, List<RepaymentPlan>>();
        for (FinanceRepaymentPlanVO repaymentPlanVO : repaymentPlanVOList) {
            String key = String.valueOf(repaymentPlanVO.getCustomerId());
            if (!planGroupMap.containsKey(key)) {
                planGroupMap.put(key, new ArrayList<RepaymentPlan>());
            }
            RepaymentPlan repaymentPlan = repaymentPlanDao.getById(repaymentPlanVO.getId());
            planGroupMap.get(key).add(repaymentPlan);
        }

        // 对每个分组进行信息推送
        for (List<RepaymentPlan> planGroup : planGroupMap.values()) {
            LOG.info("preparing to send msg for current groupPlanList with size[ " + planGroup.size() + "]");
            this.sendMsgForGroup(planGroup);
        }
    }

    //对客户分组还款计划进行微信短信推送
    private void sendMsgForGroup(List<RepaymentPlan> planGroup) {
        FintechResponse<ChannelVO> channelVOFintechResponse = channelServiceFeign.getChannelDetail(planGroup.get(0).getChannelId());
        if (!channelVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(channelVOFintechResponse);
        }
        ChannelVO channelVO = channelVOFintechResponse.getData();
        if (null != channelVO) {
            CustomerVO customerVO = null;
            CustomerBankCardVO customerBankCardVO = null;
            FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getByCustomerIdAndChannelCode(planGroup.get(0).getCustomerId(), channelVO.getChannelCode());//客户信息
            if (!customerVOFintechResponse.isOk()) {
                throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
            }
            if (null != customerVOFintechResponse.getData()) {
                customerVO = customerVOFintechResponse.getData();//客户信息
            }
            if (null != customerVO) {
                FintechResponse<CustomerBankCardVO> customerBankCardVOFintechResponse = customerServiceFeign.getCustomerBankCard(customerVO.getAccountId());
                if (null != customerBankCardVOFintechResponse.getData()) {
                    customerBankCardVO = customerBankCardVOFintechResponse.getData();//客户银行卡信息
                }
            }

            LOG.info("current groupRepaymentPlanList[" + planGroup.get(0).getId() + "] with customerId[" + planGroup.get(0).getCustomerId() +
                    "] and channelId[" + planGroup.get(0).getChannelId());

            if (null != customerBankCardVO) {
                LOG.info("还款日提醒 SENDING SMS/WECHAT_MSG to customer: {}, repay date: {}", customerBankCardVO.getAccountNumber(), DateCommonUtils.getDateFormat(planGroup.get(0).getRepayDate()));
                RepaymentPlanRemindEvent repaymentPlanRemindEvent = new RepaymentPlanRemindEvent(planGroup, planGroup.get(0).getRepayDate(), customerBankCardVO.getAccountNumber());
                this.applicationContext.publishEvent(repaymentPlanRemindEvent);
            }
        }
    }

    @Override
    public void updateRefundStatusByContractNumber(String repayDayType, String contractNumber) {
        List<RepaymentPlan> repaymentPlanList = repaymentPlanDao.getByContractNumberOrderByCurrentInstalmentAsc(contractNumber);
        if(repaymentPlanList == null || repaymentPlanList.isEmpty()){
            LOG.error("updateRefundStatusByContractNumber failed  null repaymentPlanList with contractNumber=[" + contractNumber + "]");
            throw new FInsuranceBaseException(101516);
        }
        for(int i = 0; i < repaymentPlanList.size(); i++ ){
            RepaymentPlan repaymentPlan = repaymentPlanList.get(i);
            if(StringUtils.equals(RepayDayType.INITIAL_PAYMENT.getCode(), repayDayType) && i==0){
                //期初还款已近还款完成
                repaymentPlan.setRepayStatus(RefundStatus.HAS_REFUND);
            }else  if(repaymentPlan.getRepayStatus() == RefundStatus.INIT_REFUND) {
                //确认放款还款状态变成初始状态
                repaymentPlan.setRepayStatus(RefundStatus.WAITING_REFUND);
            }
            repaymentPlanDao.save(repaymentPlan);
        }
    }

    @Override
    public void deleteRepaymentPlanByContractNumber(String contractCode) {
        if(contractCode == null){
            return;
        }
        List<RepaymentPlan> repaymentPlanList = repaymentPlanDao.getByContractNumber(contractCode);
        if(repaymentPlanList != null && repaymentPlanList.size() > 0){
            for (RepaymentPlan repaymentPlan : repaymentPlanList) {
                repaymentPlanDao.deleteEntity(repaymentPlan);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinanceRepaymentPlanVO> findRepaymentListByContract(String contractNumber, Integer maxOverdueDays, Double overdueFineRate) {
        List<RepaymentPlan> entityList = repaymentPlanDao.getByContractNumberOrderByCurrentInstalmentAsc(contractNumber);
        List<FinanceRepaymentPlanVO> voList = new ArrayList<FinanceRepaymentPlanVO>();
        for (RepaymentPlan plan : entityList) {
            FinanceRepaymentPlanVO repaymentPlanVO = this.convertToRepaymentPlanVO(plan, maxOverdueDays, overdueFineRate);
            if(repaymentPlanVO != null) {
                voList.add(repaymentPlanVO);
            }
        }
        return voList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepaymentPlan> findAllRepaymentPlanByContractNubmers(List<String> contractNumbers, Integer currentInstalment) {
        List<RepaymentPlan> entityList = repaymentPlanDao.findByContractNumberAndCurrentInstalment(contractNumbers, currentInstalment);
        return entityList;
    }

    @Override
    public void updateRepaymentPlans(List<RepaymentPlan> repaymentPlanList) {
        repaymentPlanDao.save(repaymentPlanList);
    }

    @Override
    @Transactional(readOnly = true)
    public OverdueDataVO getOverdueDataVOByRepaymentPlanVO(FinanceRepaymentPlanVO repaymentPlanVO) {
        OverdueDataVO overdueDataVO = new OverdueDataVO();
        if(repaymentPlanVO == null){
            return overdueDataVO;
        }
        overdueDataVO.setRepaymentPlanId(repaymentPlanVO.getId());
        // 分期还款计划对应的申请单
        FintechResponse<RequisitionVO> response = requisitionServiceFeign.getRequisitionByContractNumber(repaymentPlanVO.getContractNumber());
        if (!response.isOk() || response.getData() == null) {
            overdueDataVO.setOverdueFines(0.0);
            overdueDataVO.setOverdueDays(0);
        } else {
            RequisitionVO requisitionVO = response.getData();
            Date lastRefundDate = DateCommonUtils.getAfterDay(repaymentPlanVO.getRepayDate(), requisitionVO.getMaxOverdueDays());
            // 逾期时间止
            Date overdueDeadline = DateCommonUtils.getCurrentDate().before(lastRefundDate) ? DateCommonUtils.getCurrentDate() : lastRefundDate;
            long overdueDays = DateCommonUtils.intervalDays(repaymentPlanVO.getRepayDate(), overdueDeadline );
            //逾期罚息 = 剩余本金 * 罚息率 * 罚息天数
            Double overdueFines = CalculationFormulaUtils.getOverdueFines(repaymentPlanVO.getRestCapitalAmount(),
                    repaymentPlanVO.getRepayCapitalAmount(), response.getData().getOverdueFineRate(), (int)overdueDays);
            overdueDataVO.setOverdueDays(overdueDays);
            overdueDataVO.setOverdueFines(overdueFines);
        }
        return overdueDataVO;
    }

    //获取逾期罚息
    private OverdueDataVO getOverdueDataVOByRepaymentPlan(FinanceRepaymentPlanVO repaymentPlanVO, Integer maxOverdueDays, Double overdueFineRate) {
        OverdueDataVO overdueDataVO = new OverdueDataVO();
        if(repaymentPlanVO == null || maxOverdueDays == null || overdueFineRate == null){
            return overdueDataVO;
        }
        Date lastRefundDate = DateCommonUtils.getAfterDay(repaymentPlanVO.getRepayDate(), maxOverdueDays);
        // 逾期时间止
        Date overdueDeadline = DateCommonUtils.getCurrentDate().before(lastRefundDate) ? DateCommonUtils.getCurrentDate() : lastRefundDate;
        long overdueDays = DateCommonUtils.intervalDays(repaymentPlanVO.getRepayDate(), overdueDeadline );
        Double surplusCapitalAmount = repaymentPlanVO.getRestCapitalAmount() + repaymentPlanVO.getRepayCapitalAmount();
        //逾期罚息 = 剩余本金 * 罚息率 * 罚息天数
        Double overdueFines = surplusCapitalAmount * overdueFineRate * overdueDays / 10000;
        overdueDataVO.setOverdueDays(overdueDays);
        overdueDataVO.setOverdueFines(overdueFines);

        return overdueDataVO;
    }


    @Override
    @Transactional(readOnly = true)
    public BigDecimal getRemainRepayCapitalAmount(String contractNumber) {
        BigDecimal bigDecimal = new BigDecimal(0);
        if(StringUtils.isBlank(contractNumber)){
            return bigDecimal;
        }
        List<RepaymentPlan> repaymentPlanVOList = repaymentPlanDao.getByContractNumberOrderByCurrentInstalmentAsc(contractNumber);
        if(repaymentPlanVOList == null || repaymentPlanVOList.size() < 1){
            return bigDecimal;
        }
        for(RepaymentPlan repaymentPlan : repaymentPlanVOList){
            if(repaymentPlan.getRepayStatus() != RefundStatus.HAS_REFUND
                    && repaymentPlan.getRepayStatus() != RefundStatus.INIT_REFUND
                    && repaymentPlan.getRepayStatus() != RefundStatus.HAS_WITHDRAW ){
                bigDecimal = bigDecimal.add(repaymentPlan.getRepayCapitalAmount());
            }
        }
        return bigDecimal;
    }
}
