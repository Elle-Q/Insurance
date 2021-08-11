package com.fintech.insurance.micro.support.service;

import com.fintech.insurance.commons.enums.AdStatus;
import com.fintech.insurance.micro.dto.support.AdvertisementVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AdvertisementServiceImplTest {
    @Autowired
    AdvertisementService advertisementService;
    @Test
    public void findAllAdvertisement() throws Exception {
        advertisementService.findAllAdvertisement(1, AdStatus.FINISHED, 1, 2);
    }

    @Test
    public void saveOrUpdateAdvertisement() throws Exception {
        AdvertisementVO vo = new AdvertisementVO();
        vo.setId(12);
        vo.setPositionId(1);
        vo.setTitle("广告名称2");
        vo.setUrl("www.youku.com");
        vo.setStartTime(new Date());
        vo.setEndTime(new Date());
        vo.setImgKey("foiasjnklsnsd");
        vo.setSequence(3);

        advertisementService.saveOrUpdateAdvertisement(vo);
    }

    @Test
    public void deleteAdvertisement() throws Exception {
        advertisementService.deleteAdvertisement(12);
    }

    @Test
    public void getAdvertisementById() throws Exception {
    }

    @Test
    public void judgeAdvertisement() throws Exception {
    }

}