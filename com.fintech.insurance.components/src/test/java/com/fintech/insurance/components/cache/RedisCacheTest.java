package com.fintech.insurance.components.cache;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("junit")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CacheTestApplication.class})
@Import(value = {RedisAutoConfiguration.class})
public class RedisCacheTest {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    @Test
    public void testPut() {
        boolean isExist = redisTemplate.hasKey("abc");
        System.out.println(isExist);
    }

    @Test
    public void testStoreObj() throws InterruptedException {
        TestVO vo = new TestVO("testuser", 12);

        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set("com.fintech.testvo", vo);

        Thread.sleep(1000);
        boolean exists = redisTemplate.hasKey("com.fintech.testvo");
        Assert.assertTrue(exists);
        Assert.assertEquals(12, ((TestVO)operations.get("com.fintech.testvo")).getAge());
    }

    @Test
    public void testObj() throws Exception {
        stringRedisTemplate.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
    }
}
