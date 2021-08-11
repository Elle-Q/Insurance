package com.fintech.insurance.micro.finance.service;

import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.biz.RefundVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.dto.finance.OverdueDataVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RepaymentPlanServiceImplTest {
    @Test
    public void queryRepaymentPlan1() throws Exception {
    }

    @Test
    public void queryRepaymentPlan2() throws Exception {
    }

    @Test
    public void getListByContractNumber() throws Exception {
    }

    @Autowired
    RepaymentPlanService repaymentPlanService;

    @Test
    public void queryRepaymentPlan() throws Exception {
        Pagination<RefundVO> page = repaymentPlanService.queryRepaymentPlan("", "", null, "", null,  null, null, 1, 2);
        page.getItems();
    }

    @Test
    public void testGetOverdueDataVOByRepaymentPlanVO() throws Exception {
        FinanceRepaymentPlanVO repaymentPlanVO = new FinanceRepaymentPlanVO();
        repaymentPlanVO.setContractNumber("CK011205018010001");
        repaymentPlanVO.setCurrentInstalment(4);
        repaymentPlanVO.setRepayDate(DateCommonUtils.getToday());
        OverdueDataVO overdueDataVO = repaymentPlanService.getOverdueDataVOByRepaymentPlanVO(repaymentPlanVO);
        Assert.assertEquals(overdueDataVO.getOverdueFines(), new Double(0));
    }

}