package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RepayDayType;
import com.fintech.insurance.commons.enums.RepayType;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.micro.biz.persist.entity.Channel;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/14 0014 13:38
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RequisitionDaoTest {

    @Autowired
    RequisitionDao requisitionDao;
    @Autowired
    ChannelDao channelDao;

    @Autowired
    ProductDao productDao;

    @Test
    public void save() {
        Requisition r = new Requisition();
        r.setCustomerId(1);
        Channel channel = channelDao.getChannelById(3);
        r.setChannel(channel);
        r.setProduct(productDao.getById(1));
        r.setTotalCommercialAmount(1000000000);
        r.setProductType(ProductType.CAR_INSTALMENTS.getCode());
        r.setChannelApplication(true);
        r.setChannelUserId(1);
        r.setRequisitionStatus(RequisitionStatus.Auditing.getCode());
        r.setRequisitionNumber("12343454");
        r.setRepayType(RepayType.PRINCIPAL_INTEREST.getCode());
        r.setRepayDayType(RepayDayType.FINAL_PAYMENT.getCode());
        r.setServiceFeeRate(1D);
        //r.setInterestType(InterestType.INTEREST_BY_DAYS.getCode());
        //r.setInterestRate(1D);
        r.setOtherFeeRate(2D);
        r.setPrepaymentPenaltyRate(1D);
        r.setPrepaymentDays(2);
        r.setOverdueFineRate(0.05);
//        r.setLoanRatio(100D);
        r.setMaxOverdueDays(5);

        r.setPaymentOrderNumber("123444");
        r.setTotalApplyAmount(1000000000);
        r.setTotalCompulsoryAmount(1000000000);
        r.setTotalTaxAmount(1000000000);
        r.setTotalApplyAmount(1000000000);
        r.setBusinessDuration(1);
        r.setSubmissionDate(new Date());
        r.setLatestAuditBatch("11111");
        r.setLoanAccountType("personal");
        r.setLoanAccountNumber("123444");
        r.setLoanAccountBank("CCB");
        r.setLoanAccountBankBranch("nanshang");
        r.setLoanAccountName("薪乐宝");
        r.setCreateBy(1);
        r.setUpdateBy(1);
        requisitionDao.save(r);
        Assert.assertNotNull(r.getId());
    }
}