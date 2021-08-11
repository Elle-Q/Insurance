package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.micro.biz.persist.entity.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProductDaoImplTest {

    @Autowired
    ProductDao productDao;

    @Test
    public void queryWeChatProductInfoByChannelId() {
        Page<Product> list = productDao.queryWeChatProductInfoByChannelId(1, ProductType.POLICY_LOANS.getCode(),1, 10);

        Assert.assertTrue(list.getContent().size() > 0);
    }
}