package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RepayDayType;
import com.fintech.insurance.commons.enums.RepayType;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.biz.persist.dao.ProductDao;
import com.fintech.insurance.micro.biz.persist.entity.Product;
import com.fintech.insurance.micro.biz.service.ChannelService;
import com.fintech.insurance.micro.biz.service.ProductService;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import com.fintech.insurance.micro.dto.biz.ProductVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Administrator
 * @Date: 2017/11/20 0020 9:04
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ProductController.class})
public class ProductControllerTest extends BaseControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductDao productDao;

    @MockBean
    private ChannelService channelService;


    @Override
    public void setup() throws Exception {
        super.setup();
        Mockito.when(this.productService.save(Mockito.any(ProductVO.class))).thenAnswer(new Answer<Integer>(){
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return random.nextInt();
            }
        });
        Mockito.when(this.productService.getProductVOById(Mockito.any(Integer.class)))
                .thenAnswer(new Answer<ProductVO>() {
                    @Override
                    public ProductVO answer(InvocationOnMock invocation) throws Throwable {
                        ProductVO productVO = new ProductVO();
                        productVO.setId(random.nextInt());
                        productVO.setCreateAt(new Date());
                        return productVO;
                    }
                });
        Mockito.when(this.productDao.getProductById(Mockito.any(Integer.class)))
                .thenAnswer(new Answer<Product>() {
                    @Override
                    public Product answer(InvocationOnMock invocation) throws Throwable {
                        Product product = new Product();
                        product.setId(random.nextInt());
                        product.setCreateAt(new Date());
                        return product;
                    }
                });
        Mockito.when(this.productDao.getById(Mockito.any(Integer.class)))
                .thenAnswer(new Answer<Product>() {
                    @Override
                    public Product answer(InvocationOnMock invocation) throws Throwable {
                        Product product = new Product();
                        product.setId(random.nextInt());
                        product.setCreateAt(new Date());
                        return product;
                    }
                });
        Mockito.when(this.channelService.queryChannelByProductIdAndBelongFlag(Mockito.any(Integer.class),Mockito.any(Boolean.class)))
                .thenAnswer(new Answer<List<ChannelVO>>() {
                    @Override
                    public List<ChannelVO> answer(InvocationOnMock invocation) throws Throwable {
                        List<ChannelVO> channelVOList = new ArrayList<ChannelVO>();
                        ChannelVO c =new ChannelVO();
                        c.setId(random.nextInt());
                        c.setCreateAt(new Date());
                        channelVOList.add(c);
                        return channelVOList;
                    }
                });

        Mockito.when(this.productService.queryProduct(Mockito.any(String.class),Mockito.any(String.class),
                Mockito.any(String.class),Mockito.any(Integer.class),Mockito.any(Integer.class),Mockito.any(Integer.class)))
                .thenAnswer(new Answer<Pagination<ProductVO>>() {
                    @Override
                    public Pagination<ProductVO> answer(InvocationOnMock invocation) throws Throwable {
                        Pagination<ProductVO> page = new Pagination<ProductVO>();
                        List<ProductVO> list = new ArrayList<ProductVO>();
                        ProductVO p =new ProductVO();
                        p.setId(random.nextInt());
                        p.setCreateAt(new Date());
                        list.add(p);
                        page.setItems(list);
                        page.setPageIndex(1);
                        page.setPagesCount(1);
                        page.setTotalRowsCount(1);
                        page.setPageSize(10);
                        return page;
                    }
                });

        Mockito.when(this.productDao.save(Mockito.any(Product.class))).thenAnswer(new Answer<Product>() {
            @Override
            public Product answer(InvocationOnMock invocation) throws Throwable {

                Product p =new Product();
                p.setId(random.nextInt());
                p.setCreateAt(new Date());
                return p;
            }
        });

    }
    @Test
    public void save() {

        ProductVO p = new ProductVO();
        //产品名称
        p.setProductName("劳斯莱斯魅影借款");
        p.setProductCode("粤B："+ System.currentTimeMillis());
        p.setProductType(ProductType.CAR_INSTALMENTS.getCode());
        p.setRepayType(RepayType.PRINCIPAL_INTEREST.getCode());
        p.setRepayDayType(RepayDayType.FINAL_PAYMENT.getCode());
        p.setServiceFeeRate(1D);
        p.setOtherFeeRate(2D);
        p.setPrepaymentPenaltyRate(1D);
        p.setPrepaymentDays(2);
        p.setOverdueFineRate(0.05);
        p.setLoanRatio(100D);
        p.setMaxOverdueDays(5);
        p.setProductDescription("奔驰宝马劳斯莱斯你选啥");
        p.setCreateBy(1);

        productService.save(p);
        Assert.assertNotNull(p.getId());
    }

    @Test
    public void onShelves() {

        Product p = productDao.getProductById(1);
        p.setOnsale(true);
        productDao.save(p);
        Assert.assertNotNull(p.getId());
    }

    @Test
    public void downShelves() {
        Product p = productDao.getProductById(1);
        p.setOnsale(true);
        productDao.save(p);
        Assert.assertNotNull(p.getId());
    }

    @Test
    public void getProductInfoById() {
        ProductVO p = productService.getProductVOById(1);
        List<ChannelVO> channelVOList= channelService.queryChannelByProductIdAndBelongFlag(1,false);
    }

    @Test
    public void queryProduct() {
        Pagination<ProductVO> page = productService.queryProduct(null, null, null,
                null, 1,10);
    }

}