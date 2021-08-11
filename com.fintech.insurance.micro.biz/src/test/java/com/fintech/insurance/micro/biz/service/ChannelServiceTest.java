package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.micro.biz.ServiceTestApplication;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: qxy
 * @Date: 2017/11/14 9:16
 */
@ActiveProfiles("junit")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceTestApplication.class})
@Transactional
public class ChannelServiceTest {

    @Autowired
    ChannelService channelService;

    @Test
    public void testSave() {
        ChannelVO channelVO = new ChannelVO();
        channelVO.setChannelName("红豆湾湾牛骨");
        channelVO.setMobile("15879865879");
        channelVO.setChannelCode("test");
        channelVO.setBusinessLicence("testBussiness");
        channelVO.setCompanyId(1);
        channelVO.setAreaCode("47");
        channelService.saveChannel(channelVO, 1);
    }

    @Test
    public void testGetDetail() {
        channelService.getDetail(1);
    }

    @Test
    public void testQueryChannel() {
        List<Integer> companyIds = new ArrayList<>();
        List<String> channelCodes = new ArrayList<>();
        channelService.queryChannel(null,null,companyIds,channelCodes,
                1, 20);
    }

    @Test
    public void testList() {
        List<String> l1 = new ArrayList<>();
        l1.add("3");
        l1.add("4");
        l1.add("5");
        List<String> l2 = new ArrayList<>();
        l2.add("3");
        l2.add("4");
        l2.add("6");
        List<String> l3 = new ArrayList<>();

        List<String> l4 = new ArrayList<>();
        l4.addAll(l1);
        l4.addAll(l2);
        for(String s:l4){
            System.out.print(s);
        }
    }
}
