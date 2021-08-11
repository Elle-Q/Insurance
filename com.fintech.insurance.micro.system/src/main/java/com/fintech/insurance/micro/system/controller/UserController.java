package com.fintech.insurance.micro.system.controller;

import com.fintech.insurance.commons.constants.GatewayFintechHeaders;
import com.fintech.insurance.commons.enums.OauthType;
import com.fintech.insurance.commons.enums.UserType;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.system.SysUserServiceAPI;
import com.fintech.insurance.micro.dto.common.BindUserWxOauthVO;
import com.fintech.insurance.micro.dto.common.OauthAccountVO;
import com.fintech.insurance.micro.dto.system.PasswordVO;
import com.fintech.insurance.micro.dto.system.RoleVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import com.fintech.insurance.micro.system.persist.entity.User;
import com.fintech.insurance.micro.system.service.RoleService;
import com.fintech.insurance.micro.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;

/**
 * 系统用户管理
 *
 * @author qxy
 * @since 2017-11-15 11:56
 */
@RestController
public class UserController extends BaseFintechController implements SysUserServiceAPI {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Override
    public FintechResponse<Integer> saveUser(@RequestBody UserVO userVO) {
        Integer currentLoginUserId = getCurrentLoginUserId();
        if (null == userVO.getUserType()) {
            userVO.setUserType(UserType.STAFF.getCode());
        }
        User user = userService.save(userVO, currentLoginUserId);
        return FintechResponse.responseData(user.getId());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> changeUserPassword(@RequestBody PasswordVO passwordVO) {
        if (StringUtils.isEmpty(passwordVO.getOldPassword()) || StringUtils.isEmpty(passwordVO.getNewPassword())) {
            throw new FInsuranceBaseException(101509);
        }
        Integer currentLoginUserId = getCurrentLoginUserId();
        userService.changePassword(passwordVO.getOldPassword(), passwordVO.getNewPassword(), currentLoginUserId);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> resetUserPassword(@RequestBody PasswordVO passwordVO) {
        if (null == passwordVO.getId()) {
            throw new FInsuranceBaseException(101509);
        }
        userService.resetPassword(passwordVO.getId());
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    public FintechResponse<List<UserVO>> listUser(@RequestParam(name = "mobile", defaultValue = "") String mobile,
                                                  @RequestParam(name = "name", defaultValue = "") String name,
                                                  @RequestParam(name = "isLocked", defaultValue = "") Integer isLocked) {
        Integer currentLoginUserId = getCurrentLoginUserId();
        return FintechResponse.responseData(userService.listStaffUser(mobile, name, isLocked, currentLoginUserId));
    }

    @Override
    public FintechResponse<VoidPlaceHolder> freezeUser(@Validated({Update.class}) @RequestBody UserVO userVO) {
        userService.freeze(userVO.getId());
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> unfreezeUser(@Validated({Update.class}) @RequestBody UserVO userVO) {
        userService.unfreeze(userVO.getId());
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<List<RoleVO>> listRoles() {
        //获取当前登录用户的参考方法
        String currentLoginUserName = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(GatewayFintechHeaders.PRINCIPAL);
        logger.debug("Current login user name is " + currentLoginUserName);
        String currentLoginUserID = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(GatewayFintechHeaders.PRINCIPAL_ID);
        logger.debug("Current login user id is " + currentLoginUserID);
        return FintechResponse.responseData(roleService.listRoles());
    }

    public FintechResponse<List<UserVO>> getUsersByRoleCodes(@RequestParam(name = "roleCode") String roleCode) {
            return FintechResponse.responseData(userService.getUsersByRoleCode(roleCode));
    }

    @Override
    public UserVO getUserByMobile(@RequestParam(name = "mobile") String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return null;
        } else {
            return this.userService.getUserByMobile(mobile);
        }
    }

    @Override
    public List<UserVO> listChannelUserByMobile(@RequestParam(name = "mobile") String mobile) {
        return userService.listChannelUserByMobile(mobile);
    }

    @Override
    public FintechResponse<UserVO> getUserById(Integer id) {
        return FintechResponse.responseData(userService.getUserById(id));
    }

    @Override
    public FintechResponse<List<UserVO>> listChannelUserByCode(@RequestParam(name = "channelCode") String channelCode) {
        return FintechResponse.responseData(userService.listChannelUserByCode(channelCode));
    }

    @Override
    public UserVO getChannelUserByMobile(@RequestParam(name = "mobile") String mobile) {
        return userService.getChannelUserByMobile(mobile);
    }

    @Override
    public FintechResponse<UserVO> getUserInfoById(Integer id) {
        return FintechResponse.responseData(userService.getUserById(id));
    }

    @Override
    public FintechResponse<Integer> saveChannelUser(@RequestBody UserVO userVO) {
        if (null != userVO.getId()) {
            UserVO preUser = userService.getUserById(userVO.getId());
            if (null == preUser) {
                throw new FInsuranceBaseException(101500);
            }
            if (!UserType.CHANNEL.getCode().equals(preUser.getUserType()) || preUser.isChannelAdmin()) {//不允许编辑渠道管理员以及渠道用户以外的信息
                throw new FInsuranceBaseException(107020);
            }
        }
        return FintechResponse.responseData(userService.saveChannelUser(userVO));
    }

    @Override
    public FintechResponse<Pagination<UserVO>> pageChannelUserByCode(@RequestParam(value = "currentUserChannelCode") String currentUserChannelCode,
                                                                     @RequestParam(value = "pageIndex") Integer pageIndex,
                                                                     @RequestParam(value = "pageSize") Integer pageSize) {
        if (StringUtils.isEmpty(currentUserChannelCode)) {
            throw new FInsuranceBaseException(102001);
        }
        return FintechResponse.responseData(userService.pageChannelUserByCode(currentUserChannelCode, pageIndex, pageSize));
    }

    public UserVO getChannelAdminByCode(@RequestParam(name = "channelCode") String channelCode) {
        return userService.getChannelAdminByCode(channelCode);
    }

    @Override
    public FintechResponse<UserVO> getUserByWxOpenid(String appid, String openid) {
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(openid)) {
            throw new FInsuranceBaseException(101511); //请求参数错误
        } else {
            UserVO userVO = this.userService.getUserByWxOpenid(appid, openid);
            if (userVO == null) {
                throw new FInsuranceBaseException(101500); //没找到用户信息
            } else {
                return FintechResponse.responseData(userVO);
            }
        }
    }

    @Override
    public FintechResponse<UserVO> getUserByWxUnionid(String appid, String unionid) {
        if (StringUtils.isEmpty(unionid)) {
            throw new FInsuranceBaseException(101511); //请求参数错误
        } else {
            UserVO userVO = this.userService.getUserByWxUnionid(unionid);
            if (userVO == null) {
                throw new FInsuranceBaseException(101500); //没找到用户信息
            } else {
                return FintechResponse.responseData(userVO);
            }
        }
    }

    @Override
    public FintechResponse<String> bindUserWithWxAccount(@RequestBody BindUserWxOauthVO bindUserWxOauthVO) {
        if (bindUserWxOauthVO != null && bindUserWxOauthVO.getUserId() != null && StringUtils.isNotEmpty(bindUserWxOauthVO.getAppid()) && bindUserWxOauthVO.getWxMpUser() != null) {
            boolean bindResult = this.userService.bindUserWithWxAccount(bindUserWxOauthVO.getUserId(), bindUserWxOauthVO.getAppid(), bindUserWxOauthVO.getWxMpUser());
            if (bindResult) {
                return FintechResponse.responseOk();
            } else {
                throw new FInsuranceBaseException(101503); //用户不存在
            }
        } else {
            throw new FInsuranceBaseException(101512); //绑定请求出错
        }
    }

    @Override
    public FintechResponse<String> getUserWxOpenid(Integer userId, String appid) {
        OauthAccountVO userOauth = this.userService.getUserOauth(userId, OauthType.WECHAT_MP.getCode(), appid);
        if (userOauth == null) {
            throw new FInsuranceBaseException(101514);
        } else {
            return FintechResponse.responseData(userOauth.getOauthAccount());
        }
    }

    @Override
    public FintechResponse<OauthAccountVO> getUserOauthAccount(Integer userId, String oauthType, String oauthAppId) {
        OauthAccountVO userOauth = this.userService.getUserOauth(userId, oauthType, oauthAppId);
        if (userOauth == null) {
            throw new FInsuranceBaseException(101514);
        } else {
            return FintechResponse.responseData(userOauth);
        }
    }

}

