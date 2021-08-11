package com.fintech.insurance.micro.thirdparty.service.sms;

import com.fintech.insurance.micro.dto.thirdparty.sms.SMSCacheVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/22 0022 14:19
 */
@Service
public class SMSCacheServiceImpl implements SMSCacheService {

    @Autowired
    private RedisTemplate<String, Object> valueOperations;

    @Override
    public void setCache(SMSCacheVO smsCacheVO, long timeout, TimeUnit unit) {
        //添加一个 key
        ValueOperations<String, Object> value = valueOperations.opsForValue();
        String cacheVerification = smsCacheVO.getPhoneNumber()+smsCacheVO.getEventCode();
        value.set(cacheVerification, smsCacheVO, timeout, unit);
    }

    @Override
    public SMSCacheVO getCache(String cacheVerification) {
        //添加一个 key
        ValueOperations<String, Object> value = valueOperations.opsForValue();
        return (SMSCacheVO) value.get(cacheVerification);
    }
}
