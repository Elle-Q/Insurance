package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.finance.persist.entity.PaymentOrderDetail;
import com.fintech.insurance.micro.finance.persist.entity.RepaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/18 15:58
 */
@Repository
public interface RepaymentRecordDao extends JpaRepository<RepaymentRecord, Integer>, BaseEntityDao<RepaymentRecord, Integer>, RepaymentRecordComplexDao {

    @Query("select r from RepaymentRecord r where r.repaymentPlan.id = :planId")
    List<RepaymentRecord> listByRepaymentPlan_Id(@Param("planId") Integer planId);

    @Query("select r from RepaymentRecord r where r.repayBatchNo = :repayBatchNo ")
    List<RepaymentRecord> listByRepayBatchNo(@Param("repayBatchNo") String repayBatchNo);

    /**
     * 根据交易流水找到同一批次的扣款记录
     * @param transactionSerial
     * @return
     */
    @Query("select r from RepaymentRecord r where r.transactionSerial = :transactionSerial ")
    List<RepaymentRecord> listByTransactionSerial(@Param("transactionSerial") String transactionSerial);

    //@Query("select r from RepaymentRecord r where r.confirmStatus in :statusList and r.repaymentPlan.manualFlag = 0")
    @Query("select r from RepaymentRecord r where r.confirmStatus in :statusList ")
    List<RepaymentRecord> listByStatus(@Param("statusList") List<String> statusList);

    List<RepaymentRecord> listByPlanIdAndStatus(List<Integer> planIds);
}
