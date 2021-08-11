package com.fintech.insurance.micro.api.thirdparty.weixin;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信接入微服务接口定义
 * @author Sean
 * @since 2017-11-10 10:49
 */
@RequestMapping(path="/thirdparty/weixin-integration", produces = MediaType.ALL_VALUE)
public interface WeixinIntegrationServiceAPI {

    public static final String PARAM_APPID = "appid";
    public static final String PARAM_CODE = "code";
    public static final String PARAM_STATE = "state";
    public static final String PARAM_FINSURANCE_MP_USER_KEY = "finsuranceMpUser"; //用户服务端缓存用户信息，前端只需要将该字段回传到服务端则能获取当前用户已缓存的用户信息
    public static final String PARAM_CALLBACK = "callback";
    public static final String PARAM_TARGET_URL = "targetUrl";

    /**
     *
     * @param appid 公众号的app id
     * @param signature 请求签名
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @param echostr 响应字符串
     * @return 如果验证正确则返回echostr，否则则返回任意不为echostr的值
     */
    @GetMapping
    @ResponseBody
    String connectAuth(@RequestParam(name = "appid") String appid,
                       @RequestParam(name = "signature", required = false) String signature,
                       @RequestParam(name = "timestamp", required = false) String timestamp,
                       @RequestParam(name = "nonce", required =  false) String nonce,
                       @RequestParam(name = "echostr", required = false) String echostr);

    /**
     * 接收微信服务器过来的消息，包括事件、文本消息、图文消息等
     * @param appid 公众号的app id
     * @param signature 请求签名
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @param encryptType 消息加密类型，可以为空
     * @param msgSignature 消息加密签名，可以为空
     * @param openid 用户的openid
     * @param request
     * @return
     */
    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    String postMessage(@RequestParam(name = "appid") String appid,
                       @RequestParam(name = "signature") String signature,
                       @RequestParam(name = "timestamp") String timestamp,
                       @RequestParam(name = "nonce") String nonce,
                       @RequestParam(name = "encrypt_type", required = false) String encryptType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature,
                       @RequestParam(name = "openid") String openid,
                       HttpServletRequest request);

    /**
     * 验证客户端微信
     * @return
     */
    @GetMapping(path = "/MP_verify_Nm8mFeogcBA7lvw2.txt")
    @ResponseBody
    String validateCustomerTestMP();

    /**
     * 验证渠道端微信
     * @return
     */
    @GetMapping(path = "/MP_verify_kX7yNSrKn4jmHVFT.txt")
    @ResponseBody
    String validateChannelTestMP();

    /**
     * 验证客户端微信（正式环境）
     * @return
     */
    @GetMapping(path = "/MP_verify_Ms9PpREVDlsRvmXd.txt")
    @ResponseBody
    String validateCustomerProductMP();

    /**
     * 验证渠道端微信（正式环境）
     * @return
     */
    @GetMapping(path = "MP_verify_wxebnlWYWGCK5gg7.txt")
    @ResponseBody
    String validateChannelProductMP();

    /**
     * 验证客户端微信
     * @return
     */
    @GetMapping(path = "/validate/{verifyText}")
    @ResponseBody
    String validateMPVerify(String verifyText);

    /**
     * 微信授权的回调地址
     * @param appid 公众号的app id
     * @param code 用户授权后返回的authorization code，用作换取access token的票据
     * @param state 请求授权时传入时则该值在跳转时由微信自动添加，可用于验证
     * @param callback 授权成功后返回的跳转地址
     * @return
     */
    @GetMapping(path = "/oauth-callback")
    @ResponseBody
    ResponseEntity oauthCallback(@RequestParam(name = "appid") String appid, @RequestParam(name = "code") String code, @RequestParam(name = "state", required = false) String state, @RequestParam(name = "callback", required = false) String callback);

    /**
     * 获取token
     * @param appid
     * @param code
     * @param state
     * @return 获取token或者响应相应的错误给前端
     */
    @GetMapping(path = "/oauth-token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    ResponseEntity oauthToken(@RequestParam(name = "appid") String appid, @RequestParam(name = "code") String code, @RequestParam(name = "state", required = false) String state);

    /**
     * ajax请求微信授权地址
     * @param appid 公众号的app id
     * @param targetUrl 授权成功后页面的跳转地址
     * @return 返回微信授权的跳转地址
     */
    @GetMapping(path = "/ajax-oauth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    ResponseEntity ajaxOauth(@RequestParam(name = "appid") String appid, @RequestParam(name = "targetUrl") String targetUrl);

    /**
     * 请求跳转微信授权页面
     * @param appid 公众号的app id
     * @param targetUrl 授权成功后页面的跳转地址
     * @return 重定向的页面地址
     */
    @GetMapping(path = "/web-oauth")
    ResponseEntity webOauth(@RequestParam(name = "appid") String appid, @RequestParam(name = "targetUrl") String targetUrl);
}
