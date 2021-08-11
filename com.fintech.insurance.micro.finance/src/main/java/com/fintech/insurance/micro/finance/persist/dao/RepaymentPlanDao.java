package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.finance.persist.entity.RepaymentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/17 0017 12:00
 */
@Repository
public interface RepaymentPlanDao extends JpaRepository<RepaymentPlan, Integer>, BaseEntityDao<RepaymentPlan, Integer>, RepaymentPlanComplexDao {

    List<RepaymentPlan> getByContractNumberOrderByCurrentInstalmentAsc(String contractNubmer);

    /**
     * 获取指定合同的所有大于指定分期数的还款计划列表
     *
     * @param contractNumber 合同标识
     * @param currentInstalment 最小分期数，作为过滤条件， 如需要所有分期的还款计划， 可以传0
     * @return
     */
    List<RepaymentPlan> findByContractNumberAndCurrentInstalmentGreaterThan(String contractNumber, Integer currentInstalment);

    List<RepaymentPlan> findByContractNumberAndRepayStatus(String contractNumber, RefundStatus refundStatus);

    @Query("select r from RepaymentPlan r where r.contractNumber in :contractNumbers ")
    List<RepaymentPlan> findByContractNumbers(@Param("contractNumbers") List<String> contractNumbers);

    @Query("select r from RepaymentPlan r where r.repayDate = :currentDate and r.repayStatus in :statuses and r.manualFlag = 0")
    List<RepaymentPlan> listByCurrentDateAndStatus(@Param("currentDate") Date currentDate, @Param("statuses") List<RefundStatus> statuses);

    @Query("select r from RepaymentPlan r where r.repayStatus = :refundStatus")
    List<RepaymentPlan> listRepaymentPlanByStatus(@Param("refundStatus") String refundStatus);

    @Query("select r from RepaymentPlan r where r.customerId = :customerId and r.channelId = :channelId and r.repayDate = :currentDate and r.repayStatus in :statuses and r.manualFlag = 0")
    List<RepaymentPlan> listByCustomerIdAndChannelId(@Param("customerId") Integer customerId, @Param("channelId") Integer channelId,
                                                     @Param("currentDate") Date currentDate, @Param("statuses") List<RefundStatus> statuses);

    @Query("select r from RepaymentPlan r where r.customerId = :customerId and r.channelId = :channelId  and r.repayStatus in :statuses and r.manualFlag = false")
    List<RepaymentPlan> listByCustomerIdAndChannelIdAndStatus(@Param("customerId") Integer customerId,
                                                              @Param("channelId") Integer channelId,
                                                              @Param("statuses") List<RefundStatus> statuses);

    @Query("select r from RepaymentPlan r where r.repayStatus in :repayStatus and r.manualFlag = 0")
    List<RepaymentPlan> listPlansForOverdue(@Param("repayStatus") List<RefundStatus> repayStatus);

    @Query("select r from RepaymentPlan r where r.repayDate < :currentDate and r.repayStatus in :repayStatus")
    List<RepaymentPlan> listOverdued(@Param("currentDate") Date currentDate, @Param("repayStatus") List<RefundStatus> repayStatus);

    @Query("select r from RepaymentPlan r where r.contractNumber = :contractCode")
    List<RepaymentPlan> getByContractNumber(@Param("contractCode") String contractCode);

    @Query("select sum(repayCapitalAmount) from RepaymentPlan p where p.contractNumber = :contractNumber and p.currentInstalment >= :currentInstalment")
    Double getSurplusCapitalAmountByContractNumberAndInstalment(@Param("contractNumber") String contractNumber, @Param("currentInstalment") Integer currentInstalment);
}
