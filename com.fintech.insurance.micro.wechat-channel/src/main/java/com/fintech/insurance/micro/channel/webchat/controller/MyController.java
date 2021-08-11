package com.fintech.insurance.micro.channel.webchat.controller;

import com.fintech.insurance.commons.beans.WeixinConfigBean;
import com.fintech.insurance.commons.enums.OauthType;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.enums.UserType;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.common.OauthAccountVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.feign.biz.RequisitionServiceFeign;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
import com.fintech.insurance.micro.vo.wechat.CountRequisitionByStatusVO;
import com.fintech.insurance.micro.vo.wechat.LoginChannelUserVO;
import com.fintech.insurance.micro.vo.wechat.MyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: qxy
 * @Description: 微信渠道端首页接口
 * @Date: 2017/12/9 11:30
 */
@RestController
@RequireWechatLogin
@RequestMapping(value = "/wechat/channel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MyController extends BaseFintechWechatController {

    @Autowired
    private SysUserServiceFeign sysUserServiceFeign;

    @Autowired
    WeixinConfigBean weixinConfigBean;

    @Autowired
    private RequisitionServiceFeign requisitionServiceFeign;

    /**
     * 渠道端我的首页
     * @return
     */
    @GetMapping(value = "/my")
    public FintechResponse<MyVO> count() {
        Integer currentLoginUserId = getCurrentUserId();
        FintechResponse<UserVO> userVOFintechResponse = sysUserServiceFeign.getUserById(currentLoginUserId);
        if (!userVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(userVOFintechResponse);
        }
        UserVO userVO = userVOFintechResponse.getData();
        if (null == userVO) {
            throw new FInsuranceBaseException(107025);
        }
        StringBuilder channelUserIds = new StringBuilder();
        channelUserIds.append(userVO.getId());

        if (UserType.CHANNEL.getCode().equals(userVO.getUserType()) && userVO.isChannelAdmin()) {
            FintechResponse<List<UserVO>> userVOList = sysUserServiceFeign.listChannelUserByCode(userVO.getChannelCode());
            if (null != userVOList && userVOList.getData().size() > 0) {
                for (UserVO u : userVOList.getData()) {
                    channelUserIds.append("," + u.getId());
                }
            }
        }
        FintechResponse<Integer> responseForSubmitted = requisitionServiceFeign.countRequisitionByStatus(channelUserIds.toString(), null, RequisitionStatus.Submitted.getCode());
        FintechResponse<Integer> responseForWaitingPayment = requisitionServiceFeign.countRequisitionByStatus(channelUserIds.toString(), null, RequisitionStatus.WaitingPayment.getCode());
        FintechResponse<Integer> responseForRejected = requisitionServiceFeign.countRequisitionByStatus(channelUserIds.toString(), null, RequisitionStatus.Rejected.getCode());
        FintechResponse<Integer> responseForWaitingLoan = requisitionServiceFeign.countRequisitionByStatus(channelUserIds.toString(), null, RequisitionStatus.WaitingLoan.getCode());
        Map<String, Integer> map = new HashMap<>();
        map.put(RequisitionStatus.Submitted.getCode(), responseForSubmitted.getData());
        map.put(RequisitionStatus.WaitingPayment.getCode(), responseForWaitingPayment.getData());
        map.put(RequisitionStatus.Rejected.getCode(), responseForRejected.getData());
        map.put(RequisitionStatus.WaitingLoan.getCode(), responseForWaitingLoan.getData());
        return FintechResponse.responseData(this.convertToVO(userVO, map));
    }

    private MyVO convertToVO(UserVO userVO, Map<String, Integer> map) {
        MyVO myVO = new MyVO();
        myVO.setLoginChannelUserVO(convertToWeChatUserVO(userVO));
        myVO.setCountRequisitionVOList(convertToWeChatVO(map));
        return myVO;
    }

    //userVO转化为微信端VO
    private LoginChannelUserVO convertToWeChatUserVO(UserVO userVO) {
        if (null == userVO) {
            return null;
        }
        if (!UserType.CHANNEL.getCode().equals(userVO.getUserType())) {//不是渠道账号
            return null;
        }
        LoginChannelUserVO loginChannelUserVO = new LoginChannelUserVO();
        loginChannelUserVO.setChannelCode(userVO.getChannelCode());
        loginChannelUserVO.setIsChannelAdmin(userVO.isChannelAdmin() ? 1 : 0);
        loginChannelUserVO.setIsLocked(userVO.getIsLocked());
        loginChannelUserVO.setMobile(userVO.getMobile());
        loginChannelUserVO.setName(userVO.getName());
        FintechResponse<OauthAccountVO> response = sysUserServiceFeign.getUserOauthAccount(userVO.getId(), OauthType.WECHAT_MP.getCode(), weixinConfigBean.getAppid());
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        if (null != response.getData()) {
            loginChannelUserVO.setHeadImgUrl(response.getData().getHeaderImage());
        }
        return loginChannelUserVO;
    }


    //CountRequisitionVO转化为微信端VO
    private List<CountRequisitionByStatusVO> convertToWeChatVO(Map<String, Integer> map) {
        List<CountRequisitionByStatusVO> countRequisitionByStatusVOS = new ArrayList<>();
        for (String s : map.keySet()) {
            CountRequisitionByStatusVO vo = new CountRequisitionByStatusVO();
            vo.setNum(map.get(s));
            vo.setRequisitionStatus(s);
            countRequisitionByStatusVOS.add(vo);
        }
        return countRequisitionByStatusVOS;
    }

}
