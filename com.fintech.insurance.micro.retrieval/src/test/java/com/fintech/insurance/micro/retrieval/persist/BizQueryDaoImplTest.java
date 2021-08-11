package com.fintech.insurance.micro.retrieval.persist;

import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.biz.RefundVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BizQueryDaoImplTest {
    @Autowired
    BizQueryDao bizQueryDao;

    @Test
    public void pageRefundVOByNativeSQL() {
        Pagination<RefundVO> page = bizQueryDao.pageRefundVOByNativeSQL("", "", null, null, "1", null, null, 1, 2);
        Assert.assertNotNull(page);
    }

}