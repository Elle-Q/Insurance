package com.fintech.insurance.micro.finance.service;

import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.InstallmentEvent;
import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.biz.ContractVO;
import com.fintech.insurance.micro.dto.biz.RecordVO;
import com.fintech.insurance.micro.dto.biz.RefundVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.dto.finance.OverdueDataVO;
import com.fintech.insurance.micro.finance.persist.entity.RepaymentPlan;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 14:06
 */
public interface RepaymentPlanService {

    /**
     * 获取还款计划
     * @param repaymentPlanId 还款计划标识
     * @return
     */
    FinanceRepaymentPlanVO getRepaymentPlanById(Integer repaymentPlanId);

    Pagination<RefundVO> queryRepaymentPlan(String contractCode, String customerName, RefundStatus refundStatus, String requisitionNumber,
                                            String carNumber, Date refundBeginDate, Date refundEndDate, Integer pageIndex, Integer pageSize);

    Pagination<ContractVO> queryRepaymentPlan(String contractCode, String customerName, String channelName, Integer pageIndex, Integer pageSize);

    Map<String,FinanceRepaymentPlanVO> findAllRepaymentPlanByContractStatus(ContractStatus contractStatus);

    List<FinanceRepaymentPlanVO> getListByContractNumber(String contractNumber);

    void dealWithHuman(Integer repaymentPlanId);

    /**
     * 确认还款操作
     * @param recordVO
     */
    void confirmRefund(RecordVO recordVO);

    /**
     * 确认退保操作
     * @param contractNumber 合同编号
     */
    void confirmReturnInsurance(String contractNumber);

    /**
     * 更新还款计划的状态： 还款计划的状态都在此处进行
     *
     * @param repaymentPlan
     * @param updateEvent
     */
    void updateRepaymentPlanStatusByEvent(RepaymentPlan repaymentPlan, InstallmentEvent updateEvent);

    /**
     * 查询客户还款计划
     * @param customeId     客户id
     * @param days          查询最近天数
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pagination<FinanceRepaymentPlanVO> pageRepaymentPlanByCustomeId(Integer customeId, Integer days, Integer pageIndex, Integer pageSize);

    /**
     * 保存还款计划
     * @param repaymentPlanVO
     * @return
     */
    VoidPlaceHolder createInitRepaymentPlan(FinanceRepaymentPlanVO repaymentPlanVO);

    void withHold(Integer repaymentPlanId);

    //根据合同号删除还款集合
    void deleteRepaymentPlanByContractNumbers(List<String> contractNumberStr);

    /**
     * 当日还款的申请单执行自动扣款
     * @param customerId 如果customerId 不为空， 则对所有用户扣款， 否则只对指定用户进行扣款
     */
    void debitForRepayDay(Integer customerId);

    /**
     * 查询已逾期的还款计划
     * @param refundStatus
     * @return
     */
    List<FinanceRepaymentPlanVO> listRepaymentPlanByStatus(String refundStatus);

    /*
     * 每天24:00扫描已过最大预期天数的订单(过滤掉人工处理的订单)，如果扣款失败， 变更状态为待退保
     * param contractNumbers合同号集合
     */
    void changeStatusToSurrenderByContractNumber(List<String> contractNumbers);

    /**
     * 逾期还款的申请单执行自动扣款
     * @param customerId 如果customerId 不为空， 则对所有用户扣款， 否则只对指定用户进行扣款
     */
    void debitForOverdue(Integer customerId);


    void changeStatusToOverdue();

    /**
     * 对客户还款计划进行还款
     * @param planGroup
     * @return
     */
    boolean debtForPlanGroupByCustomer(List<RepaymentPlan> planGroup, Boolean overdueFlag);

    void sendMsgForRepayDate();

    //放款成功更新还款状态为代还款
    void updateRefundStatusByContractNumber(String repayDayType, String contractNumber);

    void deleteRepaymentPlanByContractNumber(String contractCode);

    /**
     * 查询还款计划
     * @param contractNumber 合同号
     * @param maxOverdueDays 最多逾期天数
     * @param overdueFineRate 逾期罚息率
     * @return
     */
    List<FinanceRepaymentPlanVO> findRepaymentListByContract(String contractNumber, Integer maxOverdueDays, Double overdueFineRate);

    /**
     * 查询当期的还款计划
     * @param contractNumbers 合同号集合
     * @param currentInstalment 当前期数
     * @return
     */
    List<RepaymentPlan> findAllRepaymentPlanByContractNubmers(List<String> contractNumbers, Integer currentInstalment);

    /**
     * 更新还款计划
     * @param repaymentPlanList 还款计划集合
     * @return
     */
    void updateRepaymentPlans(List<RepaymentPlan> repaymentPlanList);

    /**
     * 按还款计划查询逾期信息(注意：vo需传合同编号、还款日、当期本金、剩余本金)
     * @param repaymentPlanVO
     * @return
     */
    OverdueDataVO getOverdueDataVOByRepaymentPlanVO(FinanceRepaymentPlanVO repaymentPlanVO);

    //根据合同查询剩余本金
    BigDecimal getRemainRepayCapitalAmount(String contractNumber);

}
