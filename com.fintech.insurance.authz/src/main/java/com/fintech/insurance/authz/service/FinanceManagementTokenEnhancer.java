package com.fintech.insurance.authz.service;

import com.fintech.insurance.authz.util.FinanceManageUser;
import com.fintech.insurance.micro.dto.system.UserVO;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义token信息编码转换类，向JWT标准token中注入特定的用户信息
 */
public class FinanceManagementTokenEnhancer extends JwtAccessTokenConverter {

    public FinanceManagementTokenEnhancer(String tokenSignKey) {
        this.setSigningKey(tokenSignKey);
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        Object principal = authentication.getPrincipal();
        if (principal != null && principal instanceof FinanceManageUser) {
            FinanceManageUser financeManageUser = (FinanceManageUser) principal;
            final Map<String, Object> userInfo = new HashMap<>();
            userInfo.put(UserVO.ID, financeManageUser.getId());
            userInfo.put(UserVO.NAME, financeManageUser.getUserName());
            userInfo.put(UserVO.USER_TYPE, financeManageUser.getUserType());
            userInfo.put(UserVO.CHANNEL_CODE, financeManageUser.getChannelCode());
            userInfo.put(UserVO.ORG_ID, financeManageUser.getOrganizationId());
            userInfo.put(UserVO.IS_CHANNEL_ADMIN, financeManageUser.isChannelAdmin() ? 1 : 0);
            customAccessToken.setAdditionalInformation(userInfo);
        }
        return super.enhance(customAccessToken, authentication);
    }
}
