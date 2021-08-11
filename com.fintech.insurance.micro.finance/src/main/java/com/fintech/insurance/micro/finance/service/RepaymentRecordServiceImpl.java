package com.fintech.insurance.micro.finance.service;

import com.fintech.insurance.commons.enums.DebtStatus;
import com.fintech.insurance.commons.enums.SystemProfile;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.dto.finance.RepaymentRecordVO;
import com.fintech.insurance.micro.finance.persist.dao.RepaymentRecordDao;
import com.fintech.insurance.micro.finance.persist.entity.RepaymentRecord;
import com.fintech.insurance.micro.finance.service.yjf.model.DebtQueryResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/18 0018 15:42
 */
@Service
@Transactional
public class RepaymentRecordServiceImpl implements RepaymentRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(RepaymentRecordServiceImpl.class);

    @Autowired
    private RepaymentRecordDao repaymentRecordDao;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserDebtRedisService userDebtRedisService;

    @Override
    @Transactional(readOnly = true)
    public RepaymentRecordVO getRepaymentRecord(Integer repaymentRecordId) {
        RepaymentRecord repaymentRecord = this.repaymentRecordDao.getById(repaymentRecordId);
        return this.convertToVO(repaymentRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public String getRepaymentBankcardByDebtOrder(String debtSerialNumber) {
        List<RepaymentRecord> records = repaymentRecordDao.listByTransactionSerial(debtSerialNumber);
        if (records.size() == 0) {
            return null;
            //throw new IllegalStateException("Can not find the repayment record by the DebtOrderNo:" + debtOrderNO);
        }
        return records.get(0).getBankAccountNumber();
    }

    @Override
    @Transactional(readOnly = true)
    public void changeRepaymentRecordStatusFromYJF() {
        List<String> statusList = new ArrayList<>();
        statusList.add(DebtStatus.CONFIRMED.getCode());
        statusList.add(DebtStatus.PROCESSING.getCode());
        List<RepaymentRecord> repaymentRecordList = repaymentRecordDao.listByStatus(statusList);//查询confirm和processing的还款记录
        if (null != repaymentRecordList && repaymentRecordList.size() > 0) {
            // 按同一个交易流水进行分组
            Map<String, RepaymentRecord> debtTransactionNumbers = new HashMap<String, RepaymentRecord>();
            for (RepaymentRecord r : repaymentRecordList) {
                if (StringUtils.isNotBlank(r.getTransactionSerial()) && !debtTransactionNumbers.containsKey(r.getTransactionSerial())) {
                    debtTransactionNumbers.put(r.getTransactionSerial(), r);
                }
            }
            Iterator<String> debtNumberIter = debtTransactionNumbers.keySet().iterator();
            while (debtNumberIter.hasNext()) {
                String debtOrderNumber = debtNumberIter.next();
                RepaymentRecord sampleRepaymentRecord = debtTransactionNumbers.get(debtOrderNumber);
                LOG.info("query debt status of Debt order number: {}", debtOrderNumber);

                DebtQueryResponse response;
                DebtStatus resultStatus;
                if (!SystemProfile.PROD.equals(FInsuranceApplicationContext.getSystemProfile())) {// TODO; just to fix the YJF payment issue for testing
                    response = new DebtQueryResponse();
                    response.setResultMessage("MOCKED.");
                    resultStatus = DebtStatus.SETTLED;
                } else {
                    response = paymentService.queryDebtStatusWithYjfResponse(debtOrderNumber, sampleRepaymentRecord.getBankAccountNumber());
                    resultStatus = paymentService.convertDebtQueryResponseToDebtStatus(debtOrderNumber, sampleRepaymentRecord.getBankAccountNumber(), response);
                }

                //response = paymentService.queryDebtStatusWithYjfResponse(debtOrderNumber, sampleRepaymentRecord.getBankAccountNumber());
                //resultStatus = paymentService.convertDebtQueryResponseToDebtStatus(debtOrderNumber, sampleRepaymentRecord.getBankAccountNumber(), response);

                LOG.info("get debt status of Debt order number: {} with {}", debtOrderNumber, resultStatus);
                if (null != resultStatus) {
                    LOG.info("update debt status for all RepaymentRecords with debt number: {} to {}", debtOrderNumber, resultStatus);
                    paymentService.updateRepaymentRecordsDebtStatus(debtOrderNumber, resultStatus, response.getResultMessage());
                    LOG.info("update debt status for all RepaymentRecords with debt number: {} done. ", debtOrderNumber);
                }
            }
        }
    }


    private RepaymentRecordVO convertToVO(RepaymentRecord entity) {

        RepaymentRecordVO vo = null;

        if (entity != null) {
            vo.setId(entity.getId());
            vo.setRepayDate(entity.getRepayDate());
            vo.setRepayTime(entity.getRepayTime());
            vo.setRepayTotalAmount(entity.getRepayTotalAmount());
            vo.setRepayCapitalAmount(entity.getRepayCapitalAmount());
            vo.setRepayInterestAmount(entity.getRepayInterestAmount());
            vo.setOverdueInterestAmount(entity.getOverdueInterestAmount());
            vo.setRepayInterestAmount(entity.getRepayInterestAmount());
            vo.setIsOverdue(entity.getOverdueFlag());
            vo.setIsPrepayment(entity.getPrepaymentFlag());
            vo.setPrepaymentPenaltyAmount(entity.getPrepaymentPenaltyAmount());
            vo.setCustomerVoucher(entity.getCustomerVoucher());
            vo.setTransactionSerial(entity.getTransactionSerial());
            vo.setConfirmStatus(entity.getConfirmStatus());
            vo.setConfirmBy(entity.getConfirmBy());
            vo.setConfirmTime(entity.getConfirmTime());
            vo.setRemark(entity.getRemark());
        }

        return vo;
    }
}
