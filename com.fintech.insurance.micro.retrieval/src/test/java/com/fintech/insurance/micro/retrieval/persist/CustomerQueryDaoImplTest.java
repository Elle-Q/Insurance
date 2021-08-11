package com.fintech.insurance.micro.retrieval.persist;

import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerQueryDaoImplTest {
    @Autowired
    private CustomerQueryDao customerQueryDao;

    @Test
    public void pageCustomerVO() {
        Pagination<CustomerVO> list = customerQueryDao.pageCustomerVO("", "", "", "", null, 1, 20);


        Assert.assertNotNull(list);
    }

}