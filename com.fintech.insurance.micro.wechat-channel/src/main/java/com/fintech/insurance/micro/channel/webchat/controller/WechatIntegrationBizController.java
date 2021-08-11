package com.fintech.insurance.micro.channel.webchat.controller;

import com.fintech.insurance.commons.beans.WeixinConfigBean;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.common.ClientTokenVO;
import com.fintech.insurance.micro.dto.common.CreateWxMpMenuVO;
import com.fintech.insurance.micro.dto.common.WechatQRCodeVO;
import com.fintech.insurance.micro.feign.thirdparty.weixin.WeixinBizServiceFeign;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信相关的业务接口
 */
@RestController
@RequestMapping(path = "/wechat/channel/integration", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WechatIntegrationBizController extends BaseFintechWechatController {

    private static final Logger logger = LoggerFactory.getLogger(WechatIntegrationBizController.class);

    @Autowired
    private WeixinBizServiceFeign weixinBizServiceFeignClient;

    @Autowired
    private WeixinConfigBean weixinConfigBean;

    /**
     * 获取access token
     * @return
     */
    @GetMapping(path = "/access-token")
    @RequireWechatLogin
    public FintechResponse<String> getAccessToken() {
        return this.weixinBizServiceFeignClient.getAccessToken(this.weixinConfigBean.getAppid());
    }

    /**
     * js api 签名
     * @param url
     * @return
     */
    @GetMapping(path = "/jsapi-signature")
    public FintechResponse<WxJsapiSignature> getJsapiSignature(@RequestParam(name = "url", required = false) String url) {
        return this.weixinBizServiceFeignClient.getJsapiSignature(this.weixinConfigBean.getAppid(), url);
    }

    /**
     * 获取微信用户信息
     * @param openid
     * @return
     */
    @GetMapping(path = "/user-info")
    @RequireWechatLogin
    public FintechResponse<WxMpUser> getUserInfo(@RequestParam(name = "openid") String openid) {
        return this.weixinBizServiceFeignClient.getUserInfo(this.weixinConfigBean.getAppid(), openid);
    }

    /**
     * 生成确认申请单的二维码
     * @param appid 客户端微信公众号的appid
     * @param requisitionId
     * @return
     */
    @GetMapping(path = "/confirm-requisition-qr")
    @RequireWechatLogin
    public FintechResponse<WechatQRCodeVO> generateConfirmRequisitionQRCode(@RequestParam(name = "appid") String appid, @RequestParam(name = "requisitionId") Integer requisitionId) {
        return this.weixinBizServiceFeignClient.generateConfirmRequisitionQRCode(appid, requisitionId);
    }

    /**
     * 删除菜单
     */
    @PostMapping(path = "/menu-delete")
    @RequireWechatLogin
    public void deleteMenu() {
        this.weixinBizServiceFeignClient.deleteMenu(this.weixinConfigBean.getAppid());
    }


    /**
     * 创建菜单
     * @return
     */
    @PostMapping(path = "/menu-create")
    public FintechResponse<String> createMenu(@RequestBody(required = false) WxMenu wxMenu) {
        CreateWxMpMenuVO menuVO = new CreateWxMpMenuVO();
        menuVO.setAppid(this.weixinConfigBean.getAppid());
        if (wxMenu == null) {
            wxMenu = new WxMenu();
            List<WxMenuButton> buttons = new ArrayList<>();

            WxMenuButton indexButton = new WxMenuButton();
            indexButton.setType(WxConsts.MenuButtonType.VIEW);
            indexButton.setName("我要录单");
            indexButton.setUrl(this.weixinConfigBean.getIndexUrl());
            buttons.add(indexButton);

            WxMenuButton myButton = new WxMenuButton();
            myButton.setType(WxConsts.MenuButtonType.VIEW);
            myButton.setName("我的账户");
            myButton.setUrl(this.weixinConfigBean.getUserCenterUrl());
            buttons.add(myButton);

            wxMenu.setButtons(buttons);
        }
        menuVO.setMenu(wxMenu);
        return this.weixinBizServiceFeignClient.createMenu(menuVO);
    }

    /**
     * 绑定用户，测试用
     * @param finsuranceMpUser
     * @param userId
     * @return
     */
    @PostMapping(path = "/bind-oauth-test")
    public FintechResponse<ClientTokenVO> bindOauthInfoWithUser(@RequestParam(name = "finsuranceMpUser") String finsuranceMpUser, @RequestParam(name = "userId") Integer userId) {
        return this.weixinBizServiceFeignClient.bindOauthInfoWithUser(this.weixinConfigBean.getAppid(), finsuranceMpUser, userId);
    }
}
