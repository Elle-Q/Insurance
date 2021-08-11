package com.fintech.insurance.micro.system.service;

import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.common.OauthAccountVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.system.persist.entity.User;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import java.util.List;

public interface UserService {

    /**
     * 保存用户
     * @param userVO   userVO
     * @param currentLoginUserId    当前登录用户id
     * @return
     */
    User save(UserVO userVO, Integer currentLoginUserId);

    /**
     * 查询用户信息
     *
     * @param mobile   用户手机号
     * @param name  用户姓名
     * @param isLocked  是否锁定标志  0表示正常 1表示冻结
     * @return
     */
    List<UserVO> listStaffUser(String mobile, String name, Integer isLocked, Integer currentLoginUserId );

    /**
     * 冻结用户
     *
     * @param id   用户id
     */
    void freeze(Integer id);

    /**
     * 解冻用户
     *
     * @param id    用户id
     */
    void unfreeze(Integer id);

    /**
     * 根据渠道编号查询渠道管理员
     * @param channelCode    渠道编码
     * @return
     */
    UserVO getChannelAdminByCode(String channelCode);

    /**
     * 根据手机号模糊查询渠道用户
     * @param mobile
     * @return
     */
    List<UserVO> listChannelUserByMobile(String mobile);

    /**
     * 根据手机号码查找用户
     *
     * @param mobile 手机号码
     * @return 用户信息
     */
    UserVO getUserByMobile(String mobile);

    /**
     * 获取用户
     *
     * @param id 用户Id
     * @return
     */
    UserVO getUserById(Integer id);

    /**
     * 获取渠道用户
     * @param channelCode 渠道code
     * @return
     */
    List<UserVO> listChannelUserByCode(String channelCode);

    /**
     * 根据角色查找用户
     * @param roleCode      角色编码
     * @return
     */
    List<UserVO> getUsersByRoleCode(String roleCode);

    /**
     * 根据渠道绑定手机号查找渠道用户
     * @param mobile
     * @return
     */
    UserVO getChannelUserByMobile(String mobile);

    /**
     * 用户修改密码
     * @param oldPassword       旧密码
     * @param newPassword       新密码
     * @param currentLoginUserId    档期那登录用户id
     */
    void changePassword(String oldPassword, String newPassword, Integer currentLoginUserId);

    /**
     * 管理员重置密码
     * @param id
     */
    void resetPassword(Integer id);

    /**
     * 根据appid以及openid查找用户信息
     * @param appid
     * @param openid
     * @return
     */
    UserVO getUserByWxOpenid(String appid, String openid);

    /**
     * 根据unionid查找用户信息
     * @param unionid
     * @return
     */
    UserVO getUserByWxUnionid(String unionid);

    /**
     * 将用户授权信息存储入库
     * @param userId
     * @param appid
     * @param wxMpUser
     * @return
     */
    boolean bindUserWithWxAccount(Integer userId, String appid, WxMpUser wxMpUser);

    /**
     * 保存渠道子账号
     * @param userVO
     * @return
     */
    Integer saveChannelUser(UserVO userVO);

    /**
     * 获取渠道用户
     * @param channelCode 渠道code
     * @return
     */
    Pagination<UserVO> pageChannelUserByCode(String channelCode, Integer pageIndex, Integer pageSize);

    /**
     * 获得用户的oauth信息
     * @param userId
     * @param oauthType
     * @param appid
     * @return
     */
    OauthAccountVO getUserOauth(Integer userId, String oauthType, String appid);
}
