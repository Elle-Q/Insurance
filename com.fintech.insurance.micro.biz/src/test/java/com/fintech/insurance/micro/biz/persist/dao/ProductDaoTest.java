package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RepayDayType;
import com.fintech.insurance.commons.enums.RepayType;
import com.fintech.insurance.commons.utils.JacksonUtils;
import com.fintech.insurance.micro.biz.persist.entity.Channel;
import com.fintech.insurance.micro.biz.persist.entity.Product;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import com.fintech.insurance.micro.dto.biz.ProductVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/13 0013 20:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProductDaoTest {
    @Autowired
    ProductDao productDao;
    @Autowired
    ChannelDao channelDao;

    @Test
    public void update() {
        Product p = productDao.getProductById(1);
        Channel c = channelDao.getChannelById(16);
        //Channel c2 = channelDao.getChannelById(12);
        Set<Channel> channelSet = p.getChannelSet();
        if (channelSet == null) {
            channelSet = new HashSet<Channel>();
        }
        Iterator<Channel> it = channelSet.iterator();
        for (int i = 0; i < channelSet.size(); i++) {
            System.out.println(i);
            Channel cc = it.next();
            if (16 == cc.getId()) {
                channelSet.remove(cc);
                // i--;
            }
        }
        //channelSet.add(c);
        // channelSet.add(c);
        p.setChannelSet(channelSet);
        //channelDao.save(c);
        productDao.save(p);
        Assert.assertNotNull(p.getId());
    }

    @Test
    public void save() {
        Product p = new Product();
        //产品名称
        p.setProductName("劳斯莱斯魅影借款");
        p.setProductCode("粤B：368999");
        p.setProductCode("粤B：99954sssdds45599");
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
        Channel c = getChannel();
        //channelDao.getChannelById(1);

        Channel ch = new Channel();
        //产品名称
        ch.setChannelCode("1234ss4d4s45sss");
        ch.setChannelName("123ss44ds4sss5");
        ch.setIdNumber("431ss1021s44d99ss0101010");
        ch.setBusinessLicence("12s3d444ss56");
        ch.setOrganizationId(1);
        ch.setAreaCode("44ds4");
        ch.setLocked(false);
        ch.setCreateBy(1);
        ch.setUpdateBy(1);

        Set<Channel> channelSet = new HashSet<Channel>();
        channelSet.add(ch);
        p.setChannelSet(channelSet);

        productDao.save(p);
        Assert.assertNotNull(p.getId());
    }

    private Channel getChannel() {
        Channel p = new Channel();
        //产品名称
        p.setChannelCode("812345");
        p.setChannelName("812345");
        p.setIdNumber("4311021990101010");
        p.setBusinessLicence("123456");
        p.setOrganizationId(1);
        p.setAreaCode("4311");
        p.setLocked(false);
        p.setCreateBy(1);
        p.setUpdateBy(1);
        return p;
    }

    private ChannelVO getChannelVO(int i) {
        ChannelVO vo = new ChannelVO();
        vo.setId(i);
        vo.setChannelCode(i + "8888");
        vo.setChannelName(i + "粤B：" + "8888");
        vo.setCompanyName(i + "深圳8888" + i + "有限公司");
        return vo;
    }

    private ProductVO getVO(int i) {
        ProductVO vo = new ProductVO();
        vo.setId(i);
        vo.setProductName("大中华区");
        vo.setProductCode("888888");
        vo.setProductType(ProductType.CAR_INSTALMENTS.getCode());
        vo.setIsOnsale(1);
        vo.setRepayType(RepayType.PRINCIPAL_INTEREST.getCode());
        vo.setRepayDayType(RepayDayType.FINAL_PAYMENT.getCode());
        vo.setServiceFeeRate(0.03);
        vo.setOtherFeeRate(0.01);
        vo.setPrepaymentPenaltyRate(0.0005);
        vo.setPrepaymentDays(3);
        vo.setOverdueFineRate(0.0005);
        vo.setMaxOverdueDays(5);
        vo.setLoanRatio(100D);
        vo.setProductDescription("不能逾期产品");
        vo.setCreateAt(new Date());
        vo.setCreateBy(1);
        vo.setUpdateAt(new Date());
        vo.setUpdateBy(1);
        return vo;
    }

    @Test
    public void testToJeson() {
        Integer[] channelIds = new Integer[]{1, 2};
        System.out.print(JacksonUtils.toJson(channelIds));
        System.out.print(JacksonUtils.toJson(channelIds));
    }
}