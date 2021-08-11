package com.fintech.insurance.micro.channel.webchat.controller;

import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.enums.NotificationEvent;
import com.fintech.insurance.commons.enums.UserType;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.common.ClientTokenVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSCheckVerificationParamVO;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.sms.SMSServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.weixin.WeixinBizServiceFeign;
import com.fintech.insurance.micro.vo.wechat.ChannelUserVO;
import com.fintech.insurance.micro.vo.wechat.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 渠道用户
 * @Author: qxy
 * @Date: 2017/12/6 18:54
 */
@RestController
@RequestMapping(value = "/wechat/channel/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ChannelUserController extends BaseFintechWechatController{

    @Autowired
    private SysUserServiceFeign sysUserServiceFeign;

    @Autowired
    private SMSServiceFeign smsServiceFeign;

    @Autowired
    private WeixinBizServiceFeign weixinBizServiceFeign;
    /**
     * 渠道账户登录
     * @return
     */
    @PostMapping(value = "/login")
    public FintechResponse<ChannelUserVO> login(@RequestBody LoginVO loginVO) {
        //校验短信验证码
        SMSCheckVerificationParamVO smsCheckVerificationParamVO = new SMSCheckVerificationParamVO();
        smsCheckVerificationParamVO.setEventCode(NotificationEvent.WX_CHANNEL_LOGIN_AUTH.getCode());
        smsCheckVerificationParamVO.setPhoneNumber(loginVO.getPhoneNumber());
        smsCheckVerificationParamVO.setSequenceId(loginVO.getSequenceId());
        smsCheckVerificationParamVO.setVerification(loginVO.getVerification());
        FintechResponse<Boolean> fintechResponse = smsServiceFeign.checkSMSVerification(smsCheckVerificationParamVO);
        if (!fintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(fintechResponse);
        }
        Boolean flag = fintechResponse.getData();
        if (!flag) {
            throw new FInsuranceBaseException(107023);//短信校验出错
        }
        UserVO userVO = sysUserServiceFeign.getChannelUserByMobile(loginVO.getPhoneNumber());
        if (null == userVO) {//判断渠道用户是否存在
            throw new FInsuranceBaseException(107025);
        }
        FintechResponse<ClientTokenVO> response = weixinBizServiceFeign.bindOauthInfoWithUser(loginVO.getAppid(), loginVO.getFinsuranceMpUser(), userVO.getId());
        if (!response.isOk()) {
            throw  FInsuranceBaseException.buildFromErrorResponse(response);
        }
        ChannelUserVO user = this.convertToWeChatUserVO(userVO);
        user.setToken(response.getData().getToken());
        user.setTokenExpireSeconds(response.getData().getTokenExpireSeconds());
        return FintechResponse.responseData(user);
    }

    /**
     * 渠道子账号查询
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @RequireWechatLogin
    public FintechResponse<Pagination<ChannelUserVO>> listChannelUser(@RequestParam(value = "pageIndex", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
                                                                @RequestParam(value = "pageSize", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageSize) {
        String currentUserChannelCode = getCurrentUserChannelCode();//当前登录渠道账户的渠道code
        FintechResponse<Pagination<UserVO>> response = sysUserServiceFeign.pageChannelUserByCode(currentUserChannelCode, pageIndex, pageSize);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        List<ChannelUserVO> channelUserVOS = new ArrayList<>();
        if (null != response.getData().getItems() && response.getData().getItems().size() > 0) {
            for (UserVO u : response.getData().getItems()) {
                ChannelUserVO c = this.convertToWeChatUserVO(u);
                channelUserVOS.add(c);
            }
        }
        return FintechResponse.responseData(Pagination.createInstance(pageIndex, pageSize, response.getData().getTotalRowsCount(), channelUserVOS));
    }

    /**
     * 添加编辑渠道子账户
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequireWechatLogin
    public FintechResponse<Integer> saveChannelUser(@RequestBody @Validated ChannelUserVO channelUserVO) {
        UserVO userVO = this.convertToInternalVO(channelUserVO);
        FintechResponse<Integer> response = sysUserServiceFeign.saveChannelUser(userVO);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }


    /**
     * 渠道子账号详情信息
     */
    @RequestMapping(value = "/detail")
    @RequireWechatLogin
    public FintechResponse<ChannelUserVO> getChannelUser(@RequestParam(value = "id") Integer id) {
        FintechResponse<UserVO> response = sysUserServiceFeign.getUserById(id);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        UserVO userVO = response.getData();
        if (null == userVO) {
            throw new FInsuranceBaseException(107026);
        }
        if (!UserType.CHANNEL.getCode().equals(userVO.getUserType())) {//不是渠道用户
            throw new FInsuranceBaseException(107027);
        }
        return FintechResponse.responseData(this.convertToWeChatUserVO(userVO));
    }

    //转化为微信端VO
    private ChannelUserVO convertToWeChatUserVO(UserVO userVO) {
        if (null == userVO) {
            return null;
        }
        if (!UserType.CHANNEL.getCode().equals(userVO.getUserType())) {//不是渠道账号
            return null;
        }
        ChannelUserVO channelUserVO = new ChannelUserVO();
        channelUserVO.setId(userVO.getId());
        channelUserVO.setChannelCode(userVO.getChannelCode());
        channelUserVO.setIsChannelAdmin(userVO.isChannelAdmin() ? 1 : 0);
        channelUserVO.setIsLocked(userVO.getIsLocked());
        channelUserVO.setMobile(userVO.getMobile());
        channelUserVO.setName(userVO.getName());
        channelUserVO.setUserType(userVO.getUserType());
        return channelUserVO;
    }

    //转化为内部VO
    private UserVO convertToInternalVO(ChannelUserVO channelUserVO) {
        if (null == channelUserVO) {
            return null;
        }
        UserVO userVO = new UserVO();
        userVO.setId(channelUserVO.getId());
        userVO.setMobile(channelUserVO.getMobile());
        userVO.setName(channelUserVO.getName());
        userVO.setIsLocked(0);
        userVO.setUserType(UserType.CHANNEL.getCode());
        userVO.setChannelCode(getCurrentUserChannelCode());
        userVO.setChannelAdmin(false);
        userVO.setCreateBy(getCurrentUserId());
        return userVO;
    }
}