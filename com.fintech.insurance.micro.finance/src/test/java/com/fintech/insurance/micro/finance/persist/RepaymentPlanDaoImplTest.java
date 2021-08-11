package com.fintech.insurance.micro.finance.persist;

import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.micro.finance.persist.dao.RepaymentPlanDao;
import com.fintech.insurance.micro.finance.persist.entity.RepaymentPlan;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RepaymentPlanDaoImplTest {
    @Autowired
    RepaymentPlanDao repaymentPlanDao;

    @Test
    public void queryRepaymentPlan() throws Exception {
        Page<RepaymentPlan> page = repaymentPlanDao.queryRepaymentPlan(null, null,1, Integer.MAX_VALUE);

        Assert.assertNotNull(page.getContent());
    }

    @Test
    public void queryRepaymentPlan1() throws Exception {
    }

    @Test
    public void queryCompletePlan() throws Exception {
    }

    @Test
    public void findRepaymentPlanByContractStatus() throws Exception {
    }

    @Test
    public void testGetSurplusCapitalAmount() throws Exception {
        repaymentPlanDao.listRepaymentPlanByStatus("init_refund");
//        Double surplusCapitalAmount = repaymentPlanDao
//                .getSurplusCapitalAmountByContractNumberAndInstalment("CK011205018010001",4);
//        System.out.println("surplusCapitalAmount:" + surplusCapitalAmount);
    }

    @Test
    public void testListOverdued() throws Exception {
        List<RefundStatus> refundStatuses = new ArrayList<>();
        refundStatuses.add(RefundStatus.WAITING_REFUND);
        refundStatuses.add(RefundStatus.FAIL_REFUND);
        List<RepaymentPlan> repaymentPlans = repaymentPlanDao.listOverdued(DateCommonUtils.getCurrentDate(), refundStatuses);
        System.out.println(repaymentPlans.size());
    }


}