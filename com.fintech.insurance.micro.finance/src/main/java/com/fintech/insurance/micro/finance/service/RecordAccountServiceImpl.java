package com.fintech.insurance.micro.finance.service;

import com.alibaba.fastjson.JSONArray;
import com.fintech.insurance.commons.enums.AccountType;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.dto.biz.RecordVO;
import com.fintech.insurance.micro.dto.finance.VoucherVO;
import com.fintech.insurance.micro.finance.persist.dao.AccountVoucherDao;
import com.fintech.insurance.micro.finance.persist.dao.PaymentOrderDao;
import com.fintech.insurance.micro.finance.persist.dao.RepaymentRecordDao;
import com.fintech.insurance.micro.finance.persist.entity.AccountVoucher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/20 11:03
 */
@Service
public class RecordAccountServiceImpl implements RecordAccountService {

    private static final Logger logger = LoggerFactory.getLogger(RecordAccountServiceImpl.class);

    @Autowired
    private AccountVoucherDao accountVoucherDao;

    @Autowired
    private RepaymentRecordDao repaymentRecordDao;

    @Autowired
    private PaymentOrderDao paymentOrderDao;

    @Override
    @Transactional
    public void recordAccount(VoucherVO voucherVO) {
        AccountType accountType = null;
        try {
            accountType = AccountType.fromCode(voucherVO.getAccountType());
        } catch (Exception e) {
            throw new FInsuranceBaseException("105001", new Object[]{voucherVO.getAccountType(), AccountType.class.getName()});
        }

        AccountVoucher accountVoucher = accountVoucherDao.getFirstByRequisitionCodeAndAccountType(voucherVO.getRequisitionCode(), AccountType.LOAN.getCode());
        if (accountVoucher != null) {
            accountVoucher.setUpdateAt(new Date());
        } else {
            accountVoucher = new AccountVoucher();
            accountVoucher.setRemark(voucherVO.getRemark());
            accountVoucher.setCreateAt(new Date());
        }

        accountVoucher.setAccountType(accountType.getCode());
        accountVoucher.setRepaymentRecord(repaymentRecordDao.getById(voucherVO.getRepaymentRecordId()));
        accountVoucher.setPaymentOrder(paymentOrderDao.getById(voucherVO.getPaymentOrderId()));
        accountVoucher.setRequisitionId(voucherVO.getRequisitionId());
        accountVoucher.setRequisitionCode(voucherVO.getRequisitionCode());
        accountVoucher.setTransactionSerial(voucherVO.getTransactionSerial());
        accountVoucher.setUserId(voucherVO.getUserId());
        accountVoucher.setVoucher(voucherVO.getVoucher());
        accountVoucher.setAccountAmount(voucherVO.getAccountAmount() == null ? null : new BigDecimal(voucherVO.getAccountAmount()));

        accountVoucherDao.save(accountVoucher);

        logger.info("凭证记录成功");
    }

    @Override
    @Transactional(readOnly = true)
    public RecordVO getRecord(String requisitionCode) {
        if (StringUtils.isBlank(requisitionCode)) {
            return null;
        }
        AccountVoucher accountVoucher = accountVoucherDao.getFirstByRequisitionCodeAndAccountType(requisitionCode, AccountType.SERVICEFEE.getCode());

        if (accountVoucher == null) {
            return null;
        }

        RecordVO recordVO = new RecordVO();

        recordVO.setCode(accountVoucher.getRequisitionCode());
        recordVO.setRemark(accountVoucher.getRemark());
        try {
            Object[] objects = JSONArray.parseArray(accountVoucher.getVoucher(), String.class).toArray();
            String[] imgKeys = new String [objects.length];

            for (int i = 0; i < objects.length; i++) {
                imgKeys[i] = objects[i].toString();
            }

            recordVO.setImgKey(imgKeys);
        } catch (Exception e) {
            logger.error(e.getMessage());
            recordVO.setImgKey(new String[]{accountVoucher.getVoucher()});
        }

        return recordVO;
    }
}