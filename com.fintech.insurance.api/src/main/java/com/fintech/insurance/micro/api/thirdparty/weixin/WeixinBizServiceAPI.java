package com.fintech.insurance.micro.api.thirdparty.weixin;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.common.ClientTokenVO;
import com.fintech.insurance.micro.dto.common.CreateWxMpMenuVO;
import com.fintech.insurance.micro.dto.common.TemplateMessageVO;
import com.fintech.insurance.micro.dto.common.WechatQRCodeVO;
import com.fintech.insurance.micro.dto.thirdparty.WeixinKefuMsgContentVO;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 微信集成后提供的业务方法
 * @author Sean
 * @since 2017-11-10 14:17
 */
@RequestMapping(path="/thirdparty/weixin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface WeixinBizServiceAPI {

    /**
     * 将用户的授权信息绑定到指定id的帐户，用于验证账号成功之后
     * @param appid
     * @param finsuranceMpUser
     * @param userId
     * @return
     */
    @PostMapping(path = "/bind-oauth")
    FintechResponse<ClientTokenVO> bindOauthInfoWithUser(@RequestParam(name = "appid") String appid, @RequestParam(name = "finsuranceMpUser") String finsuranceMpUser, @RequestParam(name = "userId") Integer userId);

    /**
     * 获取缓存的用户授权信息
     * @param appid
     * @param finsuranceMpUser
     * @return
     */
    @GetMapping(path = "/get-financempuser-info")
    FintechResponse<WxMpUser> getFinanceMpUserInfo(@RequestParam(name = "appid") String appid, @RequestParam(name = "finsuranceMpUser") String finsuranceMpUser);

    /**
     * 获取指定公众号的app id
     * @param appid 需要获取凭证的公众号的app id
     * @return 返回该公众号的凭证
     */
    @GetMapping(path = "/access-token")
    FintechResponse<String> getAccessToken(@RequestParam(name = "appid") String appid);

    /**
     * 获取微信JS API调用的签名信息
     * @param appid 公众号的app id
     * @param url 需要调用api的页面地址
     * @return 返回签名信息
     */
    @GetMapping(path = "/jsapi-signature")
    FintechResponse<WxJsapiSignature> getJsapiSignature(@RequestParam(name = "appid") String appid, @RequestParam(name = "url", required = false) String url);

    /**
     * 根据openid获取用户的信息
     * @param appid 公众号的app id
     * @param openid 用户的openid
     * @return 返回用户信息
     */
    @GetMapping(path = "/user-info")
    FintechResponse<WxMpUser> getUserInfo(@RequestParam(name = "appid") String appid, @RequestParam(name = "openid") String openid);

    /**
     * 发送模版消息
     * @param templateMessageVO 模版消息参数
     * @return 消息id
     */
    @PostMapping(path = "/send-template")
    FintechResponse<String> sendTemplateMessage(@RequestBody TemplateMessageVO templateMessageVO);

    /**
     * 发送客服文本消息
     * @param contentVO 文本内容以及内容中的参数值
     * @return 成功则返回true，失败则返回false
     */
    @PostMapping(path = "/kefu-text")
    FintechResponse<Boolean> sendKefuTextMessage(@RequestBody WeixinKefuMsgContentVO contentVO);

    /**
     * 获得申请单确认二维码
     * @param appid
     * @param requisitionId
     * @return
     */
    @GetMapping(path = "/confirm-requisition-qr")
    FintechResponse<WechatQRCodeVO> generateConfirmRequisitionQRCode(@RequestParam(name = "appid") String appid, @RequestParam(name = "requisitionId") Integer requisitionId);


    /**
     * 删除公众号自定义菜单
     * @param appid 公众号app id
     */
    @PostMapping(path = "/menu-delete")
    void deleteMenu(@RequestParam(name = "appid") String appid);

    /**
     * 添加公众号自定义菜单
     * @param wxMenu 微信自定义菜单
     * @return 返回菜单id
     */
    @PostMapping(path = "/menu-create")
    FintechResponse<String> createMenu(@RequestBody CreateWxMpMenuVO wxMenu);

}
