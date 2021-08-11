package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.micro.biz.persist.entity.Channel;
import com.fintech.insurance.micro.biz.persist.entity.Contract;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/13 0013 20:58
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ContractDaoTest {
    @Autowired
    ContractDao contractDao;
    @Autowired
    RequisitionDao requisitionDao;
    @Autowired
    ChannelDao channelDao;

    @Autowired
    TempDao tempDao;

    @Test
    public void save() {
        Contract p = new Contract();
        Requisition r = requisitionDao.getRequisitionById(1);
        //产品名称
        p.setRequisition(r);
        Channel c = channelDao.getChannelById(3);
        p.setContentShowFile("image/file");
        p.setChannel(c);
        p.setCustomerId(1);
        p.setCustomerContractNumber("11111");
        p.setContractNumber("8888");
        p.setContractAmount(new BigDecimal(100));
        p.setBusinessDuration(1);
        p.setStartDate(new Date());
        p.setEndDate(new Date());
        contractDao.save(p);
        Assert.assertNotNull(p.getId());
    }

    @Test
    public void query() {
        Contract contract = contractDao.getById(2);

        Assert.assertNotNull(contract);
    }

    @Test
    public void insert() {
        for (int t = 2; t < 50; t ++) {
            Contract contract = contractDao.getById(t);

            if (contract == null) {
                return;
            }

            Integer i = contract.getBusinessDuration();
            if (i != null) {
                for (int k = 0; k < i; k++) {
                    RepaymentPlanTest plan = new RepaymentPlanTest();
                    plan.setCustomerId(contract.getCustomerId());
                    plan.setContractNumber(contract.getContractNumber());
                    plan.setChannelId(contract.getChannel().getId());
                    plan.setCurrentInstalment(k + 1);
                    plan.setTotalInstalment(i);
                    plan.setRepayDate(new Date());
                    plan.setRepayTotalAmount(new BigDecimal(10000));
                    plan.setRepayInterestAmount(new BigDecimal(2342));
                    plan.setRepayCapitalAmount(new BigDecimal(324234));
                    plan.setRepayStatus(RefundStatus.WAITING_REFUND);
                    plan.setManualFlag(false);

                    tempDao.save(plan);
                    Assert.assertNotNull(plan.getId());
                }
            }
        }
    }

    @Test
    @Transactional
    public void testGetLargestContractNumber() {
        System.out.println("后缀最大的合同编号：" + contractDao.getLargestContractNumberByNumberPrefix("DK17040112017"));
    }
}