package com.fintech.insurance.micro.thirdparty.service.weixin.provider;

import com.fintech.insurance.commons.constants.JWTConstant;
import com.fintech.insurance.commons.enums.UserType;
import com.fintech.insurance.commons.utils.JWTUtils;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.dto.common.BindUserWxOauthVO;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.thirdparty.model.weixin.WxOauthUser;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("customerUserProvider")
public class CustomerUserProviderImpl extends AbstractUserProvider {

    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    @Override
    public WxOauthUser getOauthUserId(String appid, String openid, String unionId) {
        //先根据openid查询用户是否存在
        boolean foundByOpenid = false;
        boolean foundByUnionid = false;
        FintechResponse<CustomerVO> customerVOResponse = customerServiceFeign.getCustomerByWxOpenid(appid, openid);
        CustomerVO customerVO = (customerVOResponse != null && customerVOResponse.getData() != null) ? customerVOResponse.getData() : null;
        if (customerVO == null && !StringUtils.isEmpty(unionId)) {
            //如果openid找不到用户则根据unionid查找
            customerVOResponse = customerServiceFeign.getCustomerByWxUnionid(appid, unionId);
            if (customerVOResponse != null && customerVOResponse.getData() != null) {
                customerVO = customerVOResponse.getData();
                foundByUnionid = true;
            }
        } else {
            foundByOpenid = true;
        }
        if (customerVO == null) {
            return null;
        } else {
            WxOauthUser wxOauthUser = new WxOauthUser();
            wxOauthUser.setId(customerVO.getAccountId());
            wxOauthUser.setFoundByOpenid(foundByOpenid);
            wxOauthUser.setFoundByUnionid(foundByUnionid);
            wxOauthUser.setUserType(UserType.CUSTOMER.getCode());
            return wxOauthUser;
        }
    }

    @Override
    public boolean saveOauthUserInfo(String appid, WxMpUser wxMpUser, Integer id) {
        if (wxMpUser != null && StringUtils.isNotEmpty(appid) && id != null) {
            FintechResponse<Boolean> bindResult = this.customerServiceFeign.bindCustomerWithWxAccount(new BindUserWxOauthVO(id, appid, wxMpUser));
            if (bindResult != null && bindResult.isOk()) {
                return true;
            } else {
                throw new FInsuranceBaseException(bindResult == null ? FInsuranceBaseException.SYSTEM_UNKNOWN_ERROR : bindResult.getCode());
            }
        } else {
            throw new FInsuranceBaseException();
        }
    }

    @Override
    public String generateJWTToken(String appid, WxOauthUser wxOauthUser) {
        return this.generateToken(appid, wxOauthUser.getUserType(), wxOauthUser.getId());
    }

    @Override
    public String generateJWTToken(String appid, Integer userId) {
        return this.generateToken(appid, UserType.CUSTOMER.getCode(), userId);
    }

    private String generateToken(String appid, String userType, Integer userId) {
        Map<String, String> headers = new HashMap<>();
        headers.put(JWTConstant.TOKEN_TYPE, TOKEN_TYPE);
        headers.put(JWTConstant.USER_TYPE, userType);
        headers.put(JWTConstant.APPID, appid);
        Map<String, String> claims = new HashMap<>();
        claims.put(JWTConstant.USER_ID, String.valueOf(userId));
        return JWTUtils.encode(headers, claims, super.getTokenExpireDate(null), super.getMpTokenIssuer(), this.getMpTokenSignKey());
    }
}
