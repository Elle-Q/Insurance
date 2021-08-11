package com.fintech.insurance.micro.thirdparty.service.weixin.provider;

import com.fintech.insurance.commons.constants.JWTConstant;
import com.fintech.insurance.commons.utils.JWTUtils;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.dto.common.BindUserWxOauthVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
import com.fintech.insurance.micro.thirdparty.model.weixin.WxOauthUser;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("systemUserProvider")
public class SystemUserProviderImpl extends AbstractUserProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SystemUserProviderImpl.class);

    @Autowired
    private SysUserServiceFeign sysUserServiceFeign;

    @Override
    public WxOauthUser getOauthUserId(String appid, String openid, String unionId) {
        //先根据openid查询用户是否存在
        boolean foundByOpenid = false;
        boolean foundByUnionid = false;
        FintechResponse<UserVO> userResponse = sysUserServiceFeign.getUserByWxOpenid(appid, openid);
        UserVO user = (userResponse != null && userResponse.getData() != null) ? userResponse.getData() : null;
        if (user == null && !StringUtils.isEmpty(unionId)) {
            //如果openid找不到用户则根据unionid查找
            userResponse = sysUserServiceFeign.getUserByWxUnionid(appid, unionId);
            if (userResponse != null && userResponse.getData() != null) {
                foundByUnionid = true;
                user = userResponse.getData();
            }
        } else {
            foundByOpenid = true;
        }
        if (user == null) {
            return null;
        } else {
            WxOauthUser wxOauthUser = new WxOauthUser();
            wxOauthUser.setId(user.getId());
            wxOauthUser.setFoundByOpenid(foundByOpenid);
            wxOauthUser.setFoundByUnionid(foundByUnionid);
            wxOauthUser.setUserType(user.getUserType());
            wxOauthUser.setLocked(user.getIsLocked() != null && user.getIsLocked() > 0);
            wxOauthUser.setChannelCode(user.getChannelCode());
            wxOauthUser.setOrganizationId(user.getOrganizationId());
            wxOauthUser.setChannelAdmin(user.isChannelAdmin());
            return wxOauthUser;
        }
    }

    @Override
    public boolean saveOauthUserInfo(String appid, WxMpUser wxMpUser, Integer id) {
        if (StringUtils.isNotEmpty(appid) && wxMpUser != null && id != null) {
            FintechResponse<String> bindResult = this.sysUserServiceFeign.bindUserWithWxAccount(new BindUserWxOauthVO(id, appid, wxMpUser));
            LOG.error("Bind appid= {}, wxMpUser: {}, id: {}, Result: {}", appid, wxMpUser.getNickname(), id, bindResult.toString());
            if (bindResult != null && bindResult.isOk()) {
                return true;
            } else {
                throw FInsuranceBaseException.buildFromErrorResponse(bindResult);
            }
        } else {
            throw new FInsuranceBaseException(106201);
        }
    }

    @Override
    public String generateJWTToken(String appid, WxOauthUser wxOauthUser) {
        Map<String, String> claims = new HashMap<>();
        claims.put(JWTConstant.USER_ID, String.valueOf(wxOauthUser.getId()));
        claims.put(JWTConstant.CHANNEL_CODE, wxOauthUser.getChannelCode());
        claims.put(JWTConstant.ORGANIZATION_ID, String.valueOf(wxOauthUser.getOrganizationId()));
        claims.put(JWTConstant.CHANNEL_ADMIN, String.valueOf(wxOauthUser.isChannelAdmin() ? 1 : 0));
        return this.generateToken(appid, wxOauthUser.getUserType(), claims);
    }

    @Override
    public String generateJWTToken(String appid, Integer userId) {
        FintechResponse<UserVO> userVOFintechResponse = this.sysUserServiceFeign.getUserById(userId);
        if (!userVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(userVOFintechResponse);
        }
        UserVO userVO = userVOFintechResponse.getData();
        Map<String, String> claims = new HashMap<>();
        claims.put(JWTConstant.USER_ID, String.valueOf(userVO.getId()));
        claims.put(JWTConstant.CHANNEL_CODE, userVO.getChannelCode());
        claims.put(JWTConstant.ORGANIZATION_ID, String.valueOf(userVO.getOrganizationId()));
        claims.put(JWTConstant.CHANNEL_ADMIN, String.valueOf(userVO.isChannelAdmin() ? 1 : 0));
        return this.generateToken(appid, userVO.getUserType(), claims);
    }

    private String generateToken(String appid, String userType, Map<String, String> claims) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JWTConstant.TOKEN_TYPE, TOKEN_TYPE);
        headers.put(JWTConstant.USER_TYPE, userType);
        headers.put(JWTConstant.APPID, appid);
        return JWTUtils.encode(headers, claims, super.getTokenExpireDate(null), super.getMpTokenIssuer(), this.getMpTokenSignKey());
    }
}
