package com.fintech.insurance.components.cache;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/14 21:52
 */
@ActiveProfiles("junit")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CacheTestApplication.class})
@Import(value = {RedisAutoConfiguration.class})
public class RedisSequenceFactoryTest {

    @Autowired
    private RedisSequenceFactory redisSequenceFactory;

    @Test
    public void testPut() {
        Assert.assertNotEquals(0, redisSequenceFactory.generate("hello"));
    }
}
