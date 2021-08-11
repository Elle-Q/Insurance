package com.fintech.insurance.micro.thirdparty.service;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * 微信处理的相关业务接口定义
 */
public interface WechatService {

    public static final String WX_OAUTH_STATE_CODES = "wxoauth_state_codes";

    /**
     * 获取微信oauth 2.0授权时请求中的state参数，长度为32的字符串
     * @return
     */
    public String getWechatOauthStateCode();

    /**
     * 检查state code是否合法，一个state code只能使用一次，使用过后则删除
     * @param state
     * @return
     */
    public boolean isWechatOauthStateCodeValid(String state);

    /**
     * 将state code失效
     * @param state
     * @return
     */
    public boolean invalidWechatOauthStateCode(String state);

    /**
     * 用户在授权后发现当前微信帐号没有绑定任何系统中已存在的用户帐号，则将授权用户的信息缓存在服务端，并将缓存的key值返回给前端，在提交用户绑定请求时将该key一并提交到服务端
     * @param user
     * @param appid
     * @return
     */
    public String getFinsuranceMpUserInfoToken(WxMpUser user, String appid);

    /**
     * 根据用户的FinsuranceToken获取授权信息
     * @param token
     * @param appid
     * @return
     */
    public WxMpUser getFinsuranceMpUserInfo(String token, String appid);
}
