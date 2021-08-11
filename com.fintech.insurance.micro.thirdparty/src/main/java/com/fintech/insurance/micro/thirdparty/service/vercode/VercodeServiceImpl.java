package com.fintech.insurance.micro.thirdparty.service.vercode;

import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.enums.BizCategory;
import com.fintech.insurance.components.cache.RedisSequenceFactory;
import com.fintech.insurance.micro.dto.thirdparty.ImageVercodeVO;
import com.fintech.insurance.micro.thirdparty.service.VercodeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/13 22:00
 */
@Service
public class VercodeServiceImpl implements VercodeService {

    private static final Logger LOG = LoggerFactory.getLogger(VercodeServiceImpl.class);

    private static final String REDIS_KEY_IMAGE_VERCODE = "image_vercode";

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    private RedisSequenceFactory redisSequenceFactory;

    @Override
    public ImageVercodeVO getVercode(int vercodeLength, boolean isSimpleAlgothrim) throws IOException {
        String vercode = ImageVercodeGenerator.generateVercode(vercodeLength);
        String vercodeImageStr = ImageVercodeGenerator.gen(vercode, isSimpleAlgothrim);
        LOG.info(String.format("generated image code pair, vercode is %s, image string is: %s", vercode, vercodeImageStr));

        // 图片验证码序列号
        String vercodeId = redisSequenceFactory.generateSerialNumber(BizCategory.IMAGE_VERCODE);

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //ops.set(vercodeId, vercode, BasicConstants.IMAGE_VERCODE_EXPIRED_MINUTES, TimeUnit.MINUTES);
        ops.set(vercodeId, vercode, BasicConstants.IMAGE_VERCODE_EXPIRED_DAYS, TimeUnit.DAYS);
        LOG.info("put vercode into cache: {} - {}", vercodeId, vercode);
        ops.get(vercodeId);
        return new ImageVercodeVO(vercodeId, vercodeImageStr);
    }

    @Override
    public boolean verify(String vercodeId, String possibleResult) {
        if (StringUtils.isBlank(possibleResult)) {
            return false;
        }
        if (!stringRedisTemplate.hasKey(vercodeId)) { //已过期或非法的vercodeId
            return false;
        }

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String correctCode = ops.get(vercodeId);
        LOG.debug("expected vercode is %s, actual is %s", correctCode, possibleResult);

        Boolean verifyResult = possibleResult.toLowerCase().equals(correctCode.toLowerCase());
        //对于验证过的验证码，将其从缓存清除
        if (verifyResult) {
            stringRedisTemplate.delete(vercodeId);
        }
        return verifyResult;
    }
}
