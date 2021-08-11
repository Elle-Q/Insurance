package com.fintech.insurance.micro.thirdparty.service.weixin.provider;

import com.fintech.insurance.micro.thirdparty.model.weixin.WxOauthUser;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * 公众号用户查询的接口定义
 */
public interface UserProvider {

    /**
     * 根据oauth 2.0授权信息获取用户信息
     * @param appid
     * @param openid
     * @param unionId
     * @return 用户信息
     */
    WxOauthUser getOauthUserId(String appid, String openid, String unionId);

    /**
     * 存储微信用户的信息
     * @param appid
     * @param wxMpUser
     * @param id
     */
    boolean saveOauthUserInfo(String appid, WxMpUser wxMpUser, Integer id);

    /**
     * 生成用户访问接口的jwt token
     * @param appid
     * @param wxOauthUser
     * @return 用于请求接口的jwt token
     */
    String generateJWTToken(String appid, WxOauthUser wxOauthUser);

    /**
     * 生成用户访问接口的jwt token
     * @param appid
     * @param userId
     * @return
     */
    String generateJWTToken(String appid, Integer userId);

    /**
     * 获取token的过期时间
     * @return
     */
    int getMpTokenExpireSeconds();
}
