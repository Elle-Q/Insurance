package com.fintech.insurance.micro.thirdparty.service.weixin;

import com.fintech.insurance.micro.thirdparty.service.WechatService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

/**
 * 微信处理的相关业务
 */
@Service
@Configuration
public class WechatServiceImpl implements WechatService {

    private static final Logger logger = LoggerFactory.getLogger(WechatServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> valueOperations;

    @Value("${fintech.insurance.cache.expire:3600}") //默认缓存一个小时
    private int cacheExpireSeconds;

    @Override
    public String getWechatOauthStateCode() {
        String state = DigestUtils.md5DigestAsHex((RandomStringUtils.randomNumeric(32) + System.currentTimeMillis()).getBytes());
        logger.info("The requested state code is " + state);
        this.valueOperations.opsForSet().add(WechatService.WX_OAUTH_STATE_CODES, state);
        return state;
    }

    @Override
    public boolean isWechatOauthStateCodeValid(String state) {
        if (StringUtils.isEmpty(state)) {
            logger.error("Invalid empty state code");
            return false;
        } else {
            return this.valueOperations.opsForSet().isMember(WechatService.WX_OAUTH_STATE_CODES, state);
        }
    }

    @Override
    public boolean invalidWechatOauthStateCode(String state) {
        if (StringUtils.isEmpty(state)) {
            return true;
        } else {
            this.valueOperations.opsForSet().remove(WechatService.WX_OAUTH_STATE_CODES, state);
            return true;
        }
    }

    @Override
    public String getFinsuranceMpUserInfoToken(WxMpUser user, String appid) {
        if (user == null) {
            return "";
        } else {
            String tokenSource = user.getOpenId();
            tokenSource += user.getUnionId();
            tokenSource += String.valueOf(System.currentTimeMillis());//附上事件
            String token = DigestUtils.md5DigestAsHex(tokenSource.getBytes());
            this.valueOperations.opsForValue().set(DigestUtils.md5DigestAsHex((token + appid).getBytes()), user, this.cacheExpireSeconds, TimeUnit.SECONDS);
            return token;
        }
    }

    @Override
    public WxMpUser getFinsuranceMpUserInfo(String token, String appid) {
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(appid)) {
            return null;
        } else {
            String encodedToken = DigestUtils.md5DigestAsHex((token + appid).getBytes());
            return (WxMpUser) this.valueOperations.opsForValue().get(encodedToken);
        }
    }
}
