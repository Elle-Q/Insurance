package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.micro.api.support.OrganizationServiceAPI;
import com.fintech.insurance.micro.biz.service.ChannelService;
import com.fintech.insurance.micro.biz.service.ContractService;
import com.fintech.insurance.micro.biz.service.RequisitionService;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import com.fintech.insurance.micro.dto.biz.ProductChannelVO;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
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
 * @Date: 2017/11/21 0021 17:40
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ChannelController.class})
public class ChannelControllerTest extends BaseControllerTests{

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RequisitionService requisitionService;

    @MockBean
    private ContractService contractService;

    @MockBean
    private ChannelService channelService;

    @MockBean
    SysUserServiceFeign sysUserServiceFeign;

    @MockBean
    OrganizationServiceAPI organizationServiceAPI;

    @Override
    public void setup() throws Exception {
        super.setup();


        Mockito.when(this.channelService.queryChannelByProductIdAndBelongFlag(Mockito.any(Integer.class), Mockito.any(Boolean.class)))
                .thenAnswer(new Answer<List<ChannelVO>>() {
                    @Override
                    public List<ChannelVO> answer(InvocationOnMock invocation) throws Throwable {
                        List<ChannelVO> channelVOList = new ArrayList<ChannelVO>();
                        ChannelVO c = new ChannelVO();
                        c.setId(random.nextInt());
                        c.setCreateAt(new Date());
                        channelVOList.add(c);
                        return channelVOList;
                    }
                });

        Mockito.doNothing().when(this.channelService).addProductChannel(Mockito.any(ProductChannelVO.class));
        Mockito.doNothing().when(this.channelService).deleteProductChannel(Mockito.any(ProductChannelVO.class));

    }

    @Test
    public void queryWaitingForAddChannel() {
        List<ChannelVO> list = channelService.queryChannelByProductIdAndBelongFlag(1,true);
    }

    @Test
    public void addProductChannel() {
        ProductChannelVO pcVO = new ProductChannelVO();
        pcVO.setChannelId(1);
        pcVO.setProductId(1);
        channelService.addProductChannel(pcVO);
    }

    @Test
    public void deleteProductChannel() {
        ProductChannelVO pcVO = new ProductChannelVO();
        pcVO.setChannelId(1);
        pcVO.setProductId(1);
        channelService.deleteProductChannel(pcVO);
    }

}