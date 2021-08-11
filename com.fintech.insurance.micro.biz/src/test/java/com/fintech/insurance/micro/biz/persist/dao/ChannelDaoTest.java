package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RepayDayType;
import com.fintech.insurance.commons.enums.RepayType;
import com.fintech.insurance.micro.biz.persist.entity.Channel;
import com.fintech.insurance.micro.biz.persist.entity.Product;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/14 0014 9:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ChannelDaoTest {
    @Autowired
    ChannelDao channelDao;
    @Autowired
    ProductDao productDao;

    @Test
    public void update() {
        Product p = productDao.getProductById(1);
        Channel c = channelDao.getChannelById(3);
        Set<Product> productSet = new HashSet<Product>();
        productSet.add(p);
        c.setProductSet(productSet);
        channelDao.save(c);
        productDao.save(p);
        Assert.assertNotNull(p.getId());
    }

    @Test
    public void save() {
        Channel p = new Channel();
        //产品名称
        p.setChannelCode("d12345");
        p.setChannelName("d12345");
        p.setIdNumber("4311021990101010");
        p.setBusinessLicence("123456");
        p.setOrganizationId(1);
        p.setAreaCode("4311");
        p.setLocked(false);
        p.setCreateBy(1);
        p.setUpdateBy(1);
        //channelDao.save(p);
        //private Integer updateBy;
        Product product = productDao.getProductById(1);
        Set<Product> productSet = new HashSet<Product>();
        productSet.add(product);
        p.setProductSet(productSet);
        channelDao.save(p);
        Assert.assertNotNull(p.getId());
    }

    private Product getProduct() {
        Product p = new Product();
        //产品名称
        p.setProductName("劳斯莱斯鬼影借款");
        p.setProductCode("粤B：b6666");
        p.setProductType(ProductType.CAR_INSTALMENTS.getCode());

        p.setOnsale(true);
        p.setRepayType(RepayType.PRINCIPAL_INTEREST.getCode());
        p.setRepayDayType(RepayDayType.FINAL_PAYMENT.getCode());
        p.setServiceFeeRate(1D);
        p.setOtherFeeRate(2D);
        p.setPrepaymentPenaltyRate(1D);
        p.setPrepaymentDays(2);
        p.setOverdueFineRate(0.05);
        p.setMaxOverdueDays(5);
        p.setProductDescription("奔驰宝马劳斯莱斯你选啥");
        p.setCreateBy(1);
        return p;
    }

    @Test
    public void test() {
        Product p = productDao.getProductById(13);
        System.out.print(p.getChannelSet());
    }

    private ChannelVO getChannelVO(int i) {
        ChannelVO vo = new ChannelVO();
        vo.setId(i);
        vo.setChannelCode(i + "8888");
        vo.setChannelName(i + "粤B：" + "8888");
        vo.setCompanyName(i + "深圳8888" + i + "有限公司");
        return vo;
    }
}