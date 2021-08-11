package com.fintech.insurance.micro.thirdparty.controller.wexin;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.commons.beans.WeixinConfigBean;
import com.fintech.insurance.commons.utils.URLUtils;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.api.thirdparty.weixin.WeixinIntegrationServiceAPI;
import com.fintech.insurance.micro.dto.common.ClientTokenVO;
import com.fintech.insurance.micro.dto.common.OauthAccountMissingVO;
import com.fintech.insurance.micro.thirdparty.model.weixin.WxOauthUser;
import com.fintech.insurance.micro.thirdparty.service.weixin.provider.UserProvider;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信平台集成
 */
@Controller
public class WeixinController extends AbstractWeixinController implements WeixinIntegrationServiceAPI {

    private static final Logger logger = LoggerFactory.getLogger(WeixinController.class);

    @Override
    public String connectAuth(String appid, String signature, String timestamp, String nonce, String echostr) {
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(signature) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(nonce) || StringUtils.isEmpty(echostr)) {
            //请求非法
            return this.getExceptionCodeMessage(106201);
        } else {
            WxMpService mpService = this.getWxMpService(appid);
            if (mpService == null) {
                //公众号不被支持
                return this.getExceptionCodeMessage(AbstractWeixinController.WXMP_UNSUPPORTED, appid);
            } else {
                //检查签名是否正确
                if (!mpService.checkSignature(timestamp, nonce, signature)) {
                    //消息签名不正确
                    return this.getExceptionCodeMessage(106203);
                } else {
                    return echostr;
                }
            }
        }
    }

    @Override
    public String postMessage(@RequestParam(name = "appid") String appid,
                              @RequestParam(name = "signature") String signature,
                              @RequestParam(name = "timestamp") String timestamp,
                              @RequestParam(name = "nonce") String nonce,
                              @RequestParam(name = "encrypt_type", required = false) String encryptType,
                              @RequestParam(name = "msg_signature", required = false) String msgSignature,
                              @RequestParam(name = "openid") String openid,
                              HttpServletRequest request) {
        String requestBody = null;
        try {
            requestBody = new String(StreamUtils.copyToByteArray(request.getInputStream()));
        } catch (IOException e) {
            logger.info("Fail to get request body from the request", e);
        }
        if (StringUtils.isEmpty(requestBody)) {
            return null;
        }
        logger.info(String.format("\n接收微信请求：signature=%s, encType=%s, msgSignature=%s, timestamp=%s, nonce=%s, appid=%s, openid=%s, requestBody=\n%s\n", signature, encryptType, msgSignature, timestamp, nonce, appid, openid, requestBody));
        WxMpService wxMpService = this.getWxMpService(appid);
        WxMpMessageRouter msgRouter = this.getWxMpMessageRouter(appid);
        if (wxMpService == null || msgRouter == null) {
            return null;//公众号未接入
        } else {
            //验证消息签名
            if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
                //消息签名不正确
                return null; //消息签名不正确
            } else {
                WxMpXmlMessage inMessage = null;
                if (encryptType == null) {
                    //消息以明文传输
                    inMessage = WxMpXmlMessage.fromXml(requestBody);
                } else if ("aes".equalsIgnoreCase(encryptType)) {
                    //AES加密的消息
                    try {
                        inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxMpService.getWxMpConfigStorage(), timestamp, nonce, msgSignature);
                    } catch (Exception e) {
                        logger.info("Fail to decrypt the incoming weixin message", e);
                    }
                }
                if (inMessage == null) {
                    return null;
                } else {
                    try {
                        WxMpXmlOutMessage outMessage = msgRouter.route(inMessage);
                        if (outMessage != null) {
                            logger.info(String.format("\n组装之前的回复消息:\n%s", outMessage.toXml()));
                            String out = outMessage.toEncryptedXml(wxMpService.getWxMpConfigStorage());
                            logger.info(String.format("\n组装回复消息:\n%s", out));
                            return out;
                        } else {
                            return null;
                        }
                    } catch (Exception e) {
                        logger.info(e.getMessage(), e);
                        return null;
                    }
                }
            }
        }
    }

    @Override
    public String validateCustomerTestMP() {
        return "Nm8mFeogcBA7lvw2";
    }

    @Override
    public String validateChannelTestMP() {
        return "kX7yNSrKn4jmHVFT";
    }

    @Override
    public String validateCustomerProductMP() {
        return "Ms9PpREVDlsRvmXd";
    }

    @Override
    public String validateChannelProductMP() {
        return "wxebnlWYWGCK5gg7";
    }

    @Override
    public String validateMPVerify(String verifyText) {
        if (StringUtils.isEmpty(verifyText)) {
            return "";
        } else {
            String prefix = verifyText.substring(0, verifyText.indexOf("."));
            String[] segments = prefix.split("_");
            if (segments.length == 0) {
                return prefix;
            } else {
                return segments[segments.length - 1];
            }
        }
    }

    @Override
    public ResponseEntity oauthToken(String appid, String code, String state) {
        //检查该请求是否合法
        boolean isValidCallback = this.wechatService.isWechatOauthStateCodeValid(state);
        if (isValidCallback) {
            //合法则删除state
            this.wechatService.invalidWechatOauthStateCode(state);
            //检查appid
            WxMpService mpService = this.getWxMpService(appid);
            WeixinConfigBean configBean = this.getWeixinConfigBean(appid);
            if (mpService == null || configBean == null) {
                return ResponseEntity.ok(JSON.toJSONString(FintechResponse.responseData(AbstractWeixinController.WXMP_UNSUPPORTED, this.getExceptionCodeMessage(AbstractWeixinController.WXMP_UNSUPPORTED, appid), null))); //公众号未接入
            } else {
                try {
                    WxMpOAuth2AccessToken oAuth2AccessToken = mpService.oauth2getAccessToken(code);
                    WxMpUser user = mpService.oauth2getUserInfo(oAuth2AccessToken, AbstractWeixinController.WXMP_API_LANG);
                    //检查该用户是否为一个合法的用户
                    UserProvider userProvider = this.getUserProvider(appid);
                    if (userProvider == null) {
                        return ResponseEntity.ok(FintechResponse.responseData(106209, this.getExceptionCodeMessage(106209), null)); //无法获取用户信息，请检查微信公众号配置是否配置了正确的User Provider实现
                    } else {
                        WxOauthUser wxOauthUser = userProvider.getOauthUserId(appid, user.getOpenId(), user.getUnionId());
                        if (wxOauthUser == null) { //用户不存在，需要跳转到验证用户手机号码的界面
                            Map<String, String> parameters = new HashMap<>();
                            parameters.put(WeixinIntegrationServiceAPI.PARAM_FINSURANCE_MP_USER_KEY, this.wechatService.getFinsuranceMpUserInfoToken(user, appid));
                            parameters.put(WeixinIntegrationServiceAPI.PARAM_APPID, appid);
                            String validationUserUri = URLUtils.appendQueryParameters(configBean.getUserValidationUrl(), parameters);
                            OauthAccountMissingVO missingVO = new OauthAccountMissingVO();
                            missingVO.setFinsuranceMpUser(parameters.get(WeixinIntegrationServiceAPI.PARAM_FINSURANCE_MP_USER_KEY));
                            missingVO.setAppid(appid);
                            missingVO.setUserValidationUrl(validationUserUri);
                            return ResponseEntity.ok(JSON.toJSON(FintechResponse.responseData(106215, this.getExceptionCodeMessage(106215), missingVO)));//需要跳转到用户验证的页面
                        } else {
                            if (wxOauthUser.isFoundByUnionid()) {//如果是根据unionid查找的用户则需要保存授权信息
                                userProvider.saveOauthUserInfo(appid, user, wxOauthUser.getId());
                            }
                            //保存成功则登录，响应token
                            ClientTokenVO tokenVO = new ClientTokenVO();
                            tokenVO.setToken(userProvider.generateJWTToken(appid, wxOauthUser));
                            tokenVO.setTokenExpireSeconds(userProvider.getMpTokenExpireSeconds());
                            return ResponseEntity.ok(JSON.toJSONString(FintechResponse.responseData(tokenVO)));
                        }
                    }
                } catch (WxErrorException e) {
                    logger.info("Fail to handle the weixin oauth 2.0 callback", e);
                    return ResponseEntity.ok(JSON.toJSONString(FintechResponse.responseData(106210, this.getExceptionCodeMessage(106210), null)));
                } catch (URISyntaxException e) {
                    logger.info("Invalid redirect url format, please check the configuration for further processing", e);
                    return ResponseEntity.ok(JSON.toJSONString(FintechResponse.responseData(106213, this.getExceptionCodeMessage(106213), null)));//跳转地址错误
                }
            }
        } else {
            return ResponseEntity.ok(JSON.toJSONString(FintechResponse.responseData(106211, this.getExceptionCodeMessage(106211), null)));//授权回调请求失效
        }
    }

    @Override
    public ResponseEntity oauthCallback(String appid, String code, String state, String callback) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_APPID, appid);
        params.put(PARAM_CODE, code);
        params.put(PARAM_STATE, state);
        logger.info("the requested code from weixin is " + code);
        logger.info("the request state code from weixin is " + state);
        try {
            String originalURL = new String(Base64.decodeBase64(callback));
            String finalRedirectUrl = originalURL + "&" + URLUtils.buildQueryString(params);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(finalRedirectUrl));
            logger.info("The redirect url is " + finalRedirectUrl);
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
        } catch (URISyntaxException e) {
            logger.info("fail to redirect to frontend redirect page", e);
            return ResponseEntity.unprocessableEntity().body(this.getExceptionCodeMessage(106212));
        }
    }

    @Override
    public ResponseEntity ajaxOauth(String appid, String targetUrl) {
        WxMpService mpService = this.getWxMpService(appid);
        WeixinConfigBean configBean = this.getWeixinConfigBean(appid);
        if (mpService != null && configBean != null) {
            try {
                String state = this.wechatService.getWechatOauthStateCode(); //可将该state存储入redis，用于回掉验证请求是否合法
                Map<String, String> params = new HashMap<>();
                params.put(PARAM_APPID, appid);
                //将地址转换为前端中转页面的地址
                if (StringUtils.isNotEmpty(targetUrl)) {
                    targetUrl = URLDecoder.decode(targetUrl);
                }
                String finalTargetUrl = URLUtils.appendQueryParameter(configBean.getRedirectCallbackUrl(), PARAM_TARGET_URL, URLEncoder.encode(StringUtils.isEmpty(targetUrl) ? configBean.getIndexUrl() : targetUrl));
                params.put(PARAM_CALLBACK, Base64.encodeBase64String(finalTargetUrl.getBytes()));
                return ResponseEntity.ok(JSON.toJSONString(FintechResponse.responseData(mpService.oauth2buildAuthorizationUrl(URLUtils.appendQueryParameters(configBean.getCallbackUrl(), params), "snsapi_userinfo", state))));
            } catch (Exception e) {
                logger.info("Fail to start ajax oauth due to exception found", e);
                return ResponseEntity.ok(JSON.toJSONString(FintechResponse.responseData(106210, this.getExceptionCodeMessage(106210), null)));
            }
        } else {
            return ResponseEntity.ok(JSON.toJSONString(FintechResponse.responseData(AbstractWeixinController.WXMP_UNSUPPORTED, this.getExceptionCodeMessage(AbstractWeixinController.WXMP_UNSUPPORTED, appid), null)));//公众号未接入
        }
    }

    @Override
    public ResponseEntity webOauth(String appid, String targetUrl) {
        HttpServletRequest request = this.getCurrentRequest();
        if (request == null) {
            logger.info("application is in unknown status, and now the current request cannot be found in the request context holder");
            return ResponseEntity.unprocessableEntity().body(this.getExceptionCodeMessage(106210));
        } else {
            WxMpService mpService = this.getWxMpService(appid);
            WeixinConfigBean configBean = this.getWeixinConfigBean(appid);
            if (mpService != null && configBean != null) {
                try {
                    String state = this.wechatService.getWechatOauthStateCode(); //可将该state存储入redis，用于回掉验证请求是否合法
                    Map<String, String> params = new HashMap<>();
                    params.put(PARAM_APPID, appid);
                    //将地址转换为前端中转页面的地址
                    if (StringUtils.isNotEmpty(targetUrl)) {
                        targetUrl = URLDecoder.decode(targetUrl);
                    }
                    String finalTargetUrl = URLUtils.appendQueryParameter(configBean.getRedirectCallbackUrl(), PARAM_TARGET_URL, URLEncoder.encode(StringUtils.isEmpty(targetUrl) ? configBean.getIndexUrl() : targetUrl));
                    params.put(PARAM_CALLBACK, Base64.encodeBase64String(finalTargetUrl.getBytes()));
                    String redirectUrl = mpService.oauth2buildAuthorizationUrl(URLUtils.appendQueryParameters(configBean.getCallbackUrl(), params), "snsapi_userinfo", state);
                    HttpHeaders headers = new HttpHeaders();
                    try {
                        headers.setLocation(new URI(redirectUrl));
                        return new ResponseEntity(headers, HttpStatus.FOUND);
                    } catch (URISyntaxException e) {
                        logger.info("Invalid oauth callback url " + redirectUrl);
                        return ResponseEntity.unprocessableEntity().body(this.getExceptionCodeMessage(106212));
                    }
                } catch (Exception e) {
                    logger.info("Fail to start web oauth due to exception found", e);
                    return ResponseEntity.unprocessableEntity().body(this.getExceptionCodeMessage(106210));
                }
            } else {
                return ResponseEntity.ok(JSON.toJSONString(FintechResponse.responseData(AbstractWeixinController.WXMP_UNSUPPORTED, this.getExceptionCodeMessage(AbstractWeixinController.WXMP_UNSUPPORTED, appid), null)));//公众号未接入
            }
        }
    }
}
