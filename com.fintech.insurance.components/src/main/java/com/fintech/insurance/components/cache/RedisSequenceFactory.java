package com.fintech.insurance.components.cache;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/14 21:49
 */

import com.fintech.insurance.commons.enums.BizCategory;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class RedisSequenceFactory {
    private static final int BIZ_CATEGORY_SERIAL_CODE_LEN = 3;
    private static final String DEFAULT_DATE_PATTERN = "yyyyMMddHHmmss";


    private RedisTemplate<String, String> stringRedisTemplate;

    public RedisSequenceFactory(RedisTemplate<String, String> stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * @param key
     * @param value
     * @param expireTime
     * @Title: set
     * @Description: set cache.
     */
    public void set(String key, int value, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, stringRedisTemplate.getConnectionFactory());
        counter.set(value);
        counter.expireAt(expireTime);
    }

    /**
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @Title: set
     * @Description: set cache.
     */
    public void set(String key, int value, long timeout, TimeUnit unit) {
        RedisAtomicLong counter = new RedisAtomicLong(key, stringRedisTemplate.getConnectionFactory());
        counter.set(value);
        counter.expire(timeout, unit);
    }

    /**
     * @param key
     * @return
     * @Title: generate
     * @Description: Atomically increments by one the current value.
     */
    public long generate(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, stringRedisTemplate.getConnectionFactory());
        return counter.incrementAndGet();
    }

    /**
     * ????????????????????????: ????????????????????????????????????categoryIdentifyCode???????????????????????????categorySequenceNum???????????????????????????
     *
     * ??????????????????????????????:  VH(?????????????????????)20170101121212(???????????????)03?????????????????????Z(?????????)
     *
     * @param bizCategory ?????????????????????
     * @return
     */
    public String generateSerialNumber(BizCategory bizCategory) {
        String dateStr = DateCommonUtils.dateToStringByFormat(Calendar.getInstance().getTime(), DEFAULT_DATE_PATTERN);

        // ??????redis???????????????ID??? ???????????????????????????????????????????????????ID??? ??????????????????999
        long serialNum = this.generate("serial_number" + bizCategory.getCode() + dateStr);
        if (String.valueOf(serialNum).length() > BIZ_CATEGORY_SERIAL_CODE_LEN) {
            throw new FInsuranceBaseException(106901);
        }
        String serialNumStr = StringUtils.leftPad(String.valueOf(serialNum), BIZ_CATEGORY_SERIAL_CODE_LEN, "0");

        String sequenceStr = StringUtils.join(bizCategory.getCode(), dateStr, serialNumStr);
        String verifyKey = DigestUtils.md5Hex(sequenceStr).substring(0, 1).toUpperCase();

        return sequenceStr + verifyKey;
    }

    /**
     * @param key
     * @return
     * @Title: generate
     * @Description: Atomically increments by one the current value.
     */
    public long generate(String key, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, stringRedisTemplate.getConnectionFactory());
        counter.expireAt(expireTime);
        return counter.incrementAndGet();
    }

    /**
     * @param key
     * @param increment
     * @return
     * @Title: generate
     * @Description: Atomically adds the given value to the current value.
     */
    public long generate(String key, int increment) {
        RedisAtomicLong counter = new RedisAtomicLong(key, stringRedisTemplate.getConnectionFactory());
        return counter.addAndGet(increment);
    }

    /**
     * @param key
     * @param increment
     * @param expireTime
     * @return
     * @Title: generate
     * @Description: Atomically adds the given value to the current value.
     */
    public long generate(String key, int increment, Date expireTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, stringRedisTemplate.getConnectionFactory());
        counter.expireAt(expireTime);
        return counter.addAndGet(increment);
    }
}
