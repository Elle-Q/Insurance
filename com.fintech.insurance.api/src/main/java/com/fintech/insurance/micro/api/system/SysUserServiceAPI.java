package com.fintech.insurance.micro.api.system;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.common.BindUserWxOauthVO;
import com.fintech.insurance.micro.dto.common.OauthAccountVO;
import com.fintech.insurance.micro.dto.system.PasswordVO;
import com.fintech.insurance.micro.dto.system.RoleVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/system/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface SysUserServiceAPI {

    /**
     * 创建编辑用户
     * @param userVO
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    FintechResponse<Integer> saveUser(@RequestBody UserVO userVO);

    /**
     * 用户修改密码
     *
     * @param passwordVO
     */
    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> changeUserPassword(@RequestBody PasswordVO passwordVO);

    /**s
     * 管理员重置密码
     *
     * @param passwordVO
     */
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> resetUserPassword(@RequestBody PasswordVO passwordVO);


    /**
     * 用户列表
     * @param mobile 手机号码
     * @param name  姓名
     * @param isLocked 用户状态
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    FintechResponse<List<UserVO>> listUser(@RequestParam(name = "mobile", defaultValue = "") String mobile,
                                           @RequestParam(name = "name", defaultValue = "") String name,
                                           @RequestParam(name = "isLocked", defaultValue = "") Integer isLocked);

    /**
     * 冻结用户
     * @param userVO
     */
    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> freezeUser(@Validated({Update.class}) @RequestBody UserVO userVO);

    /**
     * 解冻用户
     * @param userVO
     */
    @RequestMapping(value = "/unfreeze", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> unfreezeUser(@Validated({Update.class}) @RequestBody UserVO userVO);

    /**
     * 角色列表
     */
    @RequestMapping(value = "/role/list", method = RequestMethod.GET)
    FintechResponse<List<RoleVO>> listRoles();

    /**
     * 根据角色查找用户
     * @param roleCode
     * @return
     */
    @RequestMapping(value = "/find-by-role", method = RequestMethod.GET)
    FintechResponse<List<UserVO>> getUsersByRoleCodes(@RequestParam(name = "roleCode") String roleCode);

    /**
     * 根据手机号码查找用户
     * @param mobile
     * @return
     */
    @GetMapping(path = "/find")
    UserVO getUserByMobile(@RequestParam(name = "mobile") String mobile);

    /**
     * 根据公众号的app id以及微信用户的openid获取系统用户的信息
     * @param appid 公众号的app id
     * @param openid 微信用户的openid
     * @return 系统用户信息
     */
    @GetMapping(path = "/find-oauth-user")
    FintechResponse<UserVO> getUserByWxOpenid(@RequestParam(name = "appid") String appid, @RequestParam(name = "openid") String openid);

    /**
     * 根据公众号的app id以及微信用户的union id 获取系统用户信息
     * @param appid 公众号的appId
     * @param unionid 微信用户的union id
     * @return 系统用户信息
     */
    @GetMapping(value = "/find-unionid-user")
    FintechResponse<UserVO> getUserByWxUnionid(@RequestParam(name = "appid") String appid, @RequestParam(name = "unionid") String unionid);

    /**
     * 绑定用户的微信账号
     * @param bindUserWxOauthVO 用户授权后获取的微信公众号用户的资料
     * @return 绑定成功则返回true，绑定失败则返回false
     */
    @PostMapping(path = "/bind-oauth-acct")
    FintechResponse<String> bindUserWithWxAccount(@RequestBody BindUserWxOauthVO bindUserWxOauthVO);

    /**
     * 获取客户的openid
     * @param userId 用户的id
     * @param appid 指定的微信公众号的app id
     * @return 客户在该公众号授权后的openid
     */
    @GetMapping(path = "/get-user-openid")
    FintechResponse<String> getUserWxOpenid(@RequestParam(name = "userId") Integer userId, @RequestParam(name = "appid") String appid);

    /**
     * 获取用户的授权信息
     * @param userId 用户id
     * @param oauthType 授权类型
     * @param oauthAppId 授权的app id
     * @return 用户的授权信息
     */
    @GetMapping(path = "/get-user-oauth")
    FintechResponse<OauthAccountVO> getUserOauthAccount(@RequestParam(name = "userId") Integer userId,
                                       @RequestParam(name = "oauthType") String oauthType,
                                       @RequestParam(name = "oauthAppId") String oauthAppId);

    /**
     * 查询渠道管理员
     * @param channelCode
     * @return
     */
    @GetMapping(path = "/get-channel-admin-by-code")
    UserVO getChannelAdminByCode(@RequestParam(name = "channelCode") String channelCode);

    /**
     *根据手机号模糊查询渠道用户
     * @param mobile
     * @return
     */
    @GetMapping(path = "/list-user-by-mobile")
    List<UserVO> listChannelUserByMobile(@RequestParam(name = "mobile") String mobile);

    /**
     * 获取用户信息
     *
     * @param id 用户id
     * @return
     */
    @RequestMapping(path = "/get-user-by-id")
    FintechResponse<UserVO> getUserById(@RequestParam(name = "id", required = true) Integer id);

    /**
     * 获取渠道用户信息
     * this belongs to qxy
     * @param channelCode 渠道code
     * @return
     */
    @RequestMapping(path = "/list-channel-user-by-code")
    FintechResponse<List<UserVO>> listChannelUserByCode(@RequestParam(name = "channelCode") String channelCode);

    /**
     * 获取渠道用户信息
     * this belongs to qxy
     * @param mobile 渠道账户绑定手机号
     * @return
     */
    @RequestMapping(path = "/get-channel-user-by-mobile")
    UserVO getChannelUserByMobile(@RequestParam(name = "mobile") String mobile);


    /**
     * 获取用户详细信息
     *
     * @param id 用户id
     * @return
     */
    @RequestMapping(path = "/get-user-info-by-id")
    FintechResponse<UserVO> getUserInfoById(@RequestParam(name = "id", required = true) Integer id);

    /**
     * WX保存渠道子账户
     * @param userVO
     * @return
     */
    @RequestMapping(path = "/save-channel-user")
    FintechResponse<Integer> saveChannelUser(@RequestBody UserVO userVO);

    /**
     * 分页查询渠道用户信息
     * @param currentUserChannelCode  当前登录渠道账户code
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(path = "/page-channel-user-by-code")
    FintechResponse<Pagination<UserVO>> pageChannelUserByCode(@RequestParam(value = "currentUserChannelCode") String currentUserChannelCode,
                                                              @RequestParam(value = "pageIndex") Integer pageIndex,
                                                              @RequestParam(value = "pageSize") Integer pageSize);
}
