package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.biz.LoanVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class LoanServiceImplTest {
    @Autowired
    LoanService loanService;

    @Test
    public void pageLoanInfo() throws Exception {
        Pagination<LoanVO> page = loanService.pageLoanInfo("12343454", null, ProductType.CAR_INSTALMENTS,"渠道","客户",1, Integer.MAX_VALUE);

        Assert.assertNotNull(page.getItems());

    }

    @Test
    public void recordLoan() throws Exception {
    }

}