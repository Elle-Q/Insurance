package com.fintech.insurance.micro.finance.service;

import com.fintech.insurance.commons.enums.DebtStatus;
import com.fintech.insurance.micro.finance.ServiceTestApplication;
import com.fintech.insurance.micro.finance.model.UserDebtInfoRedisVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/22 17:47
 */
@ActiveProfiles("junit")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceTestApplication.class})
public class UserDebtRedisServiceTest {

    @Autowired
    private UserDebtRedisService userDebtRedisService;

    @Test
    public void testSaveOrUpdate() {
        UserDebtInfoRedisVO userDebtInfoRedisVO = new UserDebtInfoRedisVO();
        userDebtInfoRedisVO.setBankcardNum("abc");
        userDebtInfoRedisVO.setDebtStatus(DebtStatus.CONFIRMED);
        userDebtInfoRedisVO.setAmount(100.00);
        userDebtInfoRedisVO.setDebtOrderNum("11111");
        userDebtRedisService.saveOrUpdate(userDebtInfoRedisVO);

        Assert.assertEquals(Double.valueOf(100.00), userDebtRedisService.countTotalDebtedAmount(userDebtInfoRedisVO.getBankcardNum()));

        UserDebtInfoRedisVO updateFailedVO = new UserDebtInfoRedisVO();
        updateFailedVO.setBankcardNum("abc");
        updateFailedVO.setAmount(100.00);
        updateFailedVO.setDebtOrderNum("11111");
        updateFailedVO.setDebtStatus(DebtStatus.FAILED);

        userDebtRedisService.saveOrUpdate(userDebtInfoRedisVO);
        Assert.assertEquals(Double.valueOf(100.00), userDebtRedisService.countTotalDebtedAmount(userDebtInfoRedisVO.getBankcardNum()));
    }

    @Test
    public void testTotal() {
        String bankcardNum = "test11";

        UserDebtInfoRedisVO vo1 = new UserDebtInfoRedisVO();
        vo1.setBankcardNum(bankcardNum);
        vo1.setDebtOrderNum("11111");
        vo1.setAmount(100.00);

        vo1.setDebtStatus(DebtStatus.CONFIRMED);
        userDebtRedisService.saveOrUpdate(vo1);


        UserDebtInfoRedisVO vo2 = new UserDebtInfoRedisVO();
        vo2.setBankcardNum(bankcardNum);
        vo2.setDebtOrderNum("22222");
        vo2.setAmount(99.00);

        vo2.setDebtStatus(DebtStatus.CONFIRMED);
        userDebtRedisService.saveOrUpdate(vo2);

        Assert.assertEquals(Double.valueOf(199), userDebtRedisService.countTotalDebtedAmount(bankcardNum));
    }

    @Test
    public void testTotal2() {
        String bankcardNum = "test11";

        UserDebtInfoRedisVO vo1 = new UserDebtInfoRedisVO();
        vo1.setBankcardNum(bankcardNum);
        vo1.setDebtOrderNum("11111");
        vo1.setAmount(100.00);

        vo1.setDebtStatus(DebtStatus.CONFIRMED);
        userDebtRedisService.saveOrUpdate(vo1);


        UserDebtInfoRedisVO vo2 = new UserDebtInfoRedisVO();
        vo2.setBankcardNum(bankcardNum);
        vo2.setDebtOrderNum("22222");
        vo2.setAmount(99.00);

        vo2.setDebtStatus(DebtStatus.FAILED);
        userDebtRedisService.saveOrUpdate(vo2);

        Assert.assertEquals(Double.valueOf(100.00), userDebtRedisService.countTotalDebtedAmount(bankcardNum));
    }

    @Test
    public void testClean() {
        String bankcardNum = "test11";

        UserDebtInfoRedisVO vo1 = new UserDebtInfoRedisVO();
        vo1.setBankcardNum(bankcardNum);
        vo1.setDebtOrderNum("11111");
        vo1.setAmount(100.00);

        vo1.setDebtStatus(DebtStatus.CONFIRMED);
        userDebtRedisService.saveOrUpdate(vo1);

        userDebtRedisService.clearAll(bankcardNum);


        UserDebtInfoRedisVO vo2 = new UserDebtInfoRedisVO();
        vo2.setBankcardNum(bankcardNum);
        vo2.setDebtOrderNum("22222");
        vo2.setAmount(99.00);

        vo2.setDebtStatus(DebtStatus.SETTLED);
        userDebtRedisService.saveOrUpdate(vo2);

        Assert.assertEquals(Double.valueOf(99), userDebtRedisService.countTotalDebtedAmount(bankcardNum));
    }
}

