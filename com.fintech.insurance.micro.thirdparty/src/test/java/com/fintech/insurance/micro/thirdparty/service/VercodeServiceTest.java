package com.fintech.insurance.micro.thirdparty.service;

import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.micro.dto.thirdparty.ImageVercodeVO;
import com.fintech.insurance.micro.thirdparty.ServiceTestApplication;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/14 9:16
 */
@ActiveProfiles("junit")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceTestApplication.class})
//@Import(value = {ServiceTestConfiguration.class, ServiceConfiguration.class})
@Transactional
public class VercodeServiceTest {

    @Autowired
    private VercodeService vercodeService;

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Test
    public void testGetVercode() {
        try {
            ImageVercodeVO vo = vercodeService.getVercode(5, true);
            Assert.assertTrue(StringUtils.isNotBlank(vo.getVercodeId()));
            Assert.assertTrue(StringUtils.isNotBlank(vo.getImage()));

            Assert.assertTrue(stringRedisTemplate.hasKey(vo.getVercodeId()));
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testCheckVercode() {
        Assert.assertFalse(vercodeService.verify("", ""));

        //prepare data for verify
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("test-uuid", "A1B2", BasicConstants.IMAGE_VERCODE_EXPIRED_MINUTES, TimeUnit.MINUTES);

        Assert.assertTrue(vercodeService.verify("test-uuid", "A1B2"));
        Assert.assertTrue(vercodeService.verify("test-uuid", "a1B2"));
        Assert.assertTrue(vercodeService.verify("test-uuid", "a1b2"));
        Assert.assertFalse(vercodeService.verify("test-uuid", "a1B3"));
        Assert.assertFalse(vercodeService.verify("fake-uuid", "a1B3"));
        Assert.assertFalse(vercodeService.verify("fake-uuid", null));
    }
}
