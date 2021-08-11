package com.fintech.insurance.micro.thirdparty.service.sms;

import com.fintech.insurance.micro.dto.thirdparty.sms.SMSCacheVO;

import java.util.concurrent.TimeUnit;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/22 0022 14:18
 */
public interface SMSCacheService {

    /**
     * 存储短信数据缓存
     * @param smsCacheVO
     */
    void setCache(SMSCacheVO smsCacheVO, long timeout, TimeUnit unit);

    /**
     * 读取短信数据缓存
     * @param cacheVerification
     * @return
     */
    SMSCacheVO getCache(String cacheVerification);
}
