package com.fintech.insurance.micro.thirdparty.controller.wexin;

import com.fintech.insurance.commons.beans.WeixinConfigBean;
import com.fintech.insurance.commons.beans.WeixinTemplateMessagesConfgBean;
import com.fintech.insurance.commons.constants.WechatConstants;
import com.fintech.insurance.commons.enums.EntityType;
import com.fintech.insurance.commons.exceptions.FInsuranceIOException;
import com.fintech.insurance.commons.utils.ToolUtils;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.api.thirdparty.weixin.WeixinBizServiceAPI;
import com.fintech.insurance.micro.dto.common.ClientTokenVO;
import com.fintech.insurance.micro.dto.common.CreateWxMpMenuVO;
import com.fintech.insurance.micro.dto.common.TemplateMessageVO;
import com.fintech.insurance.micro.dto.common.WechatQRCodeVO;
import com.fintech.insurance.micro.dto.thirdparty.WeixinKefuMsgContentVO;
import com.fintech.insurance.micro.thirdparty.service.weixin.provider.UserProvider;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpQrcodeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Calendar;

/**
 * 微信集成入口控制器
 */
@RestController
public class WeixinBizController extends AbstractWeixinController implements WeixinBizServiceAPI {

    private static final Logger logger = LoggerFactory.getLogger(WeixinBizController.class);

    @Override
    public FintechResponse<ClientTokenVO> bindOauthInfoWithUser(@RequestParam(name = "appid") String appid, @RequestParam(name = "finsuranceMpUser") String finsuranceMpUser, @RequestParam(name = "userId") Integer userId) {
        UserProvider userProvider = this.getUserProvider(appid);
        WxMpService wxMpService = this.getWxMpService(appid);
        if (userProvider != null && wxMpService != null) {
            //判断当前的token是否有效
            WxMpUser wxMpUser = this.wechatService.getFinsuranceMpUserInfo(finsuranceMpUser, appid);
            if (wxMpUser == null) {
                throw super.packWxErrorException(106214);
            } else {
                userProvider.saveOauthUserInfo(appid, wxMpUser, userId);
                //绑定成功则返回用户的token
                String token = userProvider.generateJWTToken(appid, userId);
                ClientTokenVO clientTokenVO = new ClientTokenVO();
                clientTokenVO.setToken(token);
                clientTokenVO.setTokenExpireSeconds(userProvider.getMpTokenExpireSeconds());
                return FintechResponse.responseData(clientTokenVO);
            }
        } else {
            throw super.packWxMpUnsupportedException(appid);
        }
    }

    @Override
    public FintechResponse<WxMpUser> getFinanceMpUserInfo(@RequestParam(name = "appid") String appid, @RequestParam(name = "finsuranceMpUser") String finsuranceMpUser) {
        UserProvider userProvider = this.getUserProvider(appid);
        if (userProvider != null) {
            WxMpUser wxMpUser = this.wechatService.getFinsuranceMpUserInfo(finsuranceMpUser, appid);
            if (wxMpUser == null) {
                throw super.packWxErrorException(106214);
            } else {
                return FintechResponse.responseData(wxMpUser);
            }
        } else {
            throw super.packWxMpUnsupportedException(appid);
        }
    }

    @Override
    public FintechResponse<WxJsapiSignature> getJsapiSignature(@RequestParam(name = "appid") String appid, @RequestParam(name = "url", required = false) String url) {
        WxMpService mpService = this.getWxMpService(appid);
        if (mpService != null) {
            try {
                WxJsapiSignature signature = mpService.createJsapiSignature(url);
                return FintechResponse.responseData(signature);
            } catch (WxErrorException e) {
                logger.error("Fail to create the jsapi signature for weixin mp with appid " + appid);
                throw super.packWxErrorException(106208);
            }
        } else {
            throw super.packWxMpUnsupportedException(appid);
        }
    }

    @Override
    public FintechResponse<String> getAccessToken(@RequestParam(name = "appid") String appid) {
        WxMpService mpService = this.getWxMpService(appid);
        if (mpService != null) {
            try {
                return FintechResponse.responseData(mpService.getAccessToken());
            } catch (WxErrorException e) {
                logger.error("Fail to get the access token for the weixin mp with appid " + appid, e);
                throw super.packWxErrorException(106204);
            }
        } else {
            throw super.packWxMpUnsupportedException(appid);
        }
    }

    @Override
    public FintechResponse<WxMpUser> getUserInfo(@RequestParam(name = "appid") String appid, @RequestParam(name = "openid") String openid) {
        WxMpService mpService = this.getWxMpService(appid);
        if (mpService != null) {
            try {
                WxMpUser user = mpService.getUserService().userInfo(openid, WXMP_API_LANG);
                logger.debug("Success to get weixin user info with appid " + appid + " and openid " + openid);
                return FintechResponse.responseData(user);
            } catch (WxErrorException e) {
                logger.error("Fail to get the user info from weixin mp with appid " + appid + " for user with openid " + openid);
                throw super.packWxErrorException(106205);
            }
        } else {
            throw super.packWxMpUnsupportedException(appid);
        }
    }

    @Override
    public FintechResponse<String> sendTemplateMessage(@RequestBody TemplateMessageVO templateMessageVO) {
        WxMpService mpService = this.getWxMpService(templateMessageVO.getAppid());
        WeixinTemplateMessagesConfgBean templateMessagesConfgBean = this.getTemplateMessagesConfigBean(templateMessageVO.getAppid());
        if (mpService != null && templateMessagesConfgBean != null) {
            String templateId = templateMessageVO.getTemplateId();
            if (StringUtils.isEmpty(templateId) && StringUtils.isNotEmpty(templateMessageVO.getTemplateString())) {
                //获得指定模版属性的描述
                PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(WeixinTemplateMessagesConfgBean.class, templateMessageVO.getTemplateString());
                if (descriptor != null) {
                    Method readMethod = descriptor.getReadMethod();
                    if (readMethod != null) {
                        try {
                            templateId = (String) readMethod.invoke(templateMessagesConfgBean);
                            logger.info("Success to read " + templateMessageVO.getTemplateString() + " property from class" + WeixinTemplateMessagesConfgBean.class.getName() + " and got value " + templateId);
                        } catch (Exception e) {
                            logger.error("Fail to read " + templateMessageVO.getTemplateString() + " property from class " + WeixinTemplateMessagesConfgBean.class.getName(), e);
                        }
                    }
                }
            }
            if (StringUtils.isEmpty(templateId)) {
                throw super.packWxErrorException(106220);
            }
            try {
                WxMpTemplateMessage templateMsg = WxMpTemplateMessage.builder().templateId(templateId).toUser(templateMessageVO.getOpenid()).url(templateMessageVO.getUrl()).data(templateMessageVO.getTemplateData()).build();
                String msgId = mpService.getTemplateMsgService().sendTemplateMsg(templateMsg);
                logger.info("Success to send weixin tempalte message to user with openid " + templateMessageVO.getOpenid());
                return FintechResponse.responseData(msgId);
            } catch (WxErrorException e) {
                logger.error("Fail to send the template message to user with openid " + templateMessageVO.getOpenid() + " and template id is " + templateMessageVO.getTemplateId() + " and the template string is " + templateMessageVO.getTemplateString(), e);
                throw super.packWxErrorException(106218); //模版消息发送失败
            }
        } else {
            throw super.packWxMpUnsupportedException(templateMessageVO.getAppid());
        }
    }

    @Override
    public FintechResponse<Boolean> sendKefuTextMessage(@RequestBody WeixinKefuMsgContentVO contentVO) {
        WxMpService mpService = this.getWxMpService(contentVO.getAppid());
        if (mpService != null) {
            try {
                String content = contentVO.getContent();
                if (contentVO.getParams() != null && contentVO.getParams().size() > 0) {
                    content = String.format(content, contentVO.getParams());
                }
                WxMpKefuMessage kefuMessage = WxMpKefuMessage.TEXT().content(content).toUser(contentVO.getOpenid()).build();
                boolean isSuccess = mpService.getKefuService().sendKefuMessage(kefuMessage);
                logger.info("Success to send weixin kefu message to user with openid " + contentVO.getOpenid());
                return FintechResponse.responseData(isSuccess);
            } catch (WxErrorException e) {
                logger.error("Fail to send kefu message to user with openid " + contentVO.getOpenid(), e);
                throw super.packWxErrorException(106219); //客服消息发送失败
            }
        } else {
            throw super.packWxMpUnsupportedException(contentVO.getAppid());
        }
    }

    @Override
    public FintechResponse<WechatQRCodeVO> generateConfirmRequisitionQRCode(@RequestParam(name = "appid") String appid, @RequestParam(name = "requisitionId") Integer requisitionId) {
        WxMpService mpService = this.getWxMpService(appid);
        WeixinConfigBean configBean = this.getWeixinConfigBean(appid);
        if (mpService != null && configBean != null) {
            WxMpQrcodeService wxMpQrcodeService = mpService.getQrcodeService();
            try {
                WxMpQrCodeTicket ticket = wxMpQrcodeService.qrCodeCreateTmpTicket(WechatConstants.QR_TMP_CONFIRM_REQUISITION_PREFIX + String.valueOf(requisitionId), configBean.getTmpQrcodeExpireSecond());
                String qrCodeUrl = wxMpQrcodeService.qrCodePictureUrl(ticket.getTicket(), false);
                WechatQRCodeVO vo = new WechatQRCodeVO();
                this.base64EncodeRemoteQrCode(qrCodeUrl, vo);
                vo.setEntityType(EntityType.REQUISITION.getCode());
                vo.setEntityId(requisitionId);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, configBean.getTmpQrcodeExpireSecond());
                vo.setExpireAt(calendar.getTimeInMillis());
                return FintechResponse.responseData(vo);
            } catch (WxErrorException e) {
                logger.error("Fail to get the qrcode picture for requisition with id " + String.valueOf(requisitionId));
                throw super.packWxErrorException(106217); //获取二维码失败
            }
        } else {
            throw super.packWxMpUnsupportedException(appid);
        }
    }

    /**
     * 获取远程图像并将图片内容进行base64编码
     * @param url
     * @param vo
     */
    private void base64EncodeRemoteQrCode(String url, WechatQRCodeVO vo) {
        if (StringUtils.isNotEmpty(url) && vo != null) {
            vo.setUrl(url);
            logger.info("down the QR code image: {}", url);
            byte[] data = null;
            int downloadTryTimes = 1;

            // 下载二维码的时候有遇到 connection reset问题，重试三次获取二维码
            while (data == null && downloadTryTimes++ <= 3) {
                try {
                    data = ToolUtils.downloadFile(url);
                    Thread.sleep(500);
                } catch (FInsuranceIOException e) {
                    logger.error("download QR code image failed on " + downloadTryTimes + " time, url: " + url + " for  " + e.getMessage());
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }

            if (data != null) {
                vo.setBase64Iamge(Base64.encodeBase64String(data));
            }
        }
    }

    @Override
    public void deleteMenu(@RequestParam(name = "appid") String appid) {
        WxMpService mpService = this.getWxMpService(appid);
        if (mpService != null) {
            try {
                mpService.getMenuService().menuDelete();
            } catch (WxErrorException e) {
                logger.error("Fail to delete menu for weixin mp with appid " + appid);
                throw super.packWxErrorException(106205);
            }
        } else {
            throw super.packWxMpUnsupportedException(appid);
        }
    }

    @Override
    public FintechResponse<String> createMenu(@RequestBody CreateWxMpMenuVO wxMenu) {
        if (wxMenu == null) {
            throw super.packWxErrorException(106206);
        } else {
            WxMpService mpService = this.getWxMpService(wxMenu.getAppid());
            if (mpService != null) {
                try {
                    mpService.getMenuService().menuDelete();
                    String menuId = mpService.getMenuService().menuCreate(wxMenu.getMenu());
                    return FintechResponse.responseData(menuId);
                } catch (WxErrorException e) {
                    logger.error("Fail to create menu for weixin mp with appid " + wxMenu.getAppid());
                    throw super.packWxErrorException(106207);
                }
            } else {
                throw super.packWxMpUnsupportedException( wxMenu.getAppid());
            }
        }
    }
}
