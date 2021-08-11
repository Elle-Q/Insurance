package com.fintech.insurance.micro.system.service;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.commons.enums.OauthType;
import com.fintech.insurance.commons.enums.RoleType;
import com.fintech.insurance.commons.enums.UserType;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.dto.common.OauthAccountVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.system.persist.dao.RoleDao;
import com.fintech.insurance.micro.system.persist.dao.UserDao;
import com.fintech.insurance.micro.system.persist.dao.UserOauthDao;
import com.fintech.insurance.micro.system.persist.entity.Role;
import com.fintech.insurance.micro.system.persist.entity.User;
import com.fintech.insurance.micro.system.persist.entity.UserOauth;
import com.vdurmont.emoji.EmojiParser;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserOauthDao userOauthDao;

    @Value("${fintech.system.default-password}")
    private String defaultPassword;

    @Autowired
    RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User save(UserVO userVO, Integer currentLoginUserId) {
        User user = userDao.getById(userVO.getId());
        List<Integer> roleIdIntegers = new ArrayList<>();
        if (user == null) {
            user = new User();
            user.setCreateAt(new Date());//设置公共字段
            user.setCreateBy(currentLoginUserId);
        } else {
            user.setUpdateAt(new Date());//设置公共字段
            user.setUpdateBy(currentLoginUserId);
        }
        if (StringUtils.isNotEmpty(userVO.getMobile())) {
           user.setUserName(userVO.getName());
           User preUser = userDao.getByMobile(userVO.getMobile());
           //验证手机号是否唯一
           if ((userVO.getId() == null && preUser != null) || (userVO.getId() != null && preUser != null && !userVO.getId().equals(preUser.getId()))) {
               logger.error("the mobile [" + userVO.getMobile() + "]" + "already used by user[ " + preUser.getId() + "]");
               throw new FInsuranceBaseException(101501);
           }
           user.setMobilePhone(userVO.getMobile());
        }
        user.setUserType(userVO.getUserType().toLowerCase().trim());
        if (UserType.CHANNEL.getCode().equalsIgnoreCase(userVO.getUserType())) {//渠道用户
            user.setChannelAdmin(userVO.isChannelAdmin());
            user.setChannelCode(userVO.getChannelCode());
        } else if (UserType.STAFF.getCode().equalsIgnoreCase(userVO.getUserType())) {//普通用户
            user.setOrganizationId(userVO.getOrganizationId());//设置内部用户所属的公司
            user.setPassword(this.passwordEncoder.encode(defaultPassword));
        }

        //设置用户的角色
        String[] idArray = userVO.getRoleIds();
        if (null != idArray && idArray.length > 0) {
            for (String s : idArray) {
                try {
                    Integer id = Integer.parseInt(s);
                    if (!roleIdIntegers.contains(id)) {
                        roleIdIntegers.add(id);
                    }
                } catch (NumberFormatException e) {
                    logger.error("Fail to convert String " + s + " to an Integer", e);
                }
            }
            if (roleIdIntegers.size() > 0) {
                List<Role> roles = this.roleDao.findByIdArray(roleIdIntegers);
                if (roles.size() > 0) {
                    user.setRoleSet(new HashSet<>(roles));
                }
            }
        }
        //保存用户
        userDao.save(user);

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserVO> listStaffUser(String mobile, String name, Integer isLocked, Integer currentLoginUserId ) {
        User currentLoginUser = userDao.getById(currentLoginUserId);
        Integer isSuperAdmin = 1;//是否为超级管理员标志
        Role adminRole = roleDao.getByCode(RoleType.ADMIN.getCode());
        if (null != currentLoginUser) {
            if (currentLoginUser.getRoleSet().contains(adminRole)) {
                isSuperAdmin = 0;
            }
        }
        List<UserVO> userVOList = new ArrayList<>();
        List<User> userList = userDao.listUser(mobile, name, isLocked, UserType.STAFF.getCode());
        if (null != userList && userList.size() > 0) {
            for (User user : userList) {
                UserVO userVO = this.convertToVO(user);
                userVO.setIsSuperAdmin(isSuperAdmin);
                userVOList.add(userVO);
            }
        }
        return userVOList;
    }

    @Override
    public void freeze(Integer id) {
        User user = userDao.getById(id);
        if (null == user) {
            logger.error("no user with id[" + id + "]");
            throw new FInsuranceBaseException(101500);
        }
        if (!user.isLocked()) {
            user.setLocked(true);
            user.setUpdateAt(new Date());
            userDao.save(user);
        }
    }

    @Override
    public void unfreeze(Integer id) {
        User user = userDao.getById(id);
        if (null == user) {
            logger.error("no user with id[" + id + "]");
            throw new FInsuranceBaseException(101500);
        }
        if (user.isLocked()) {
            user.setLocked(false);
            user.setUpdateAt(new Date());
            userDao.save(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO getChannelAdminByCode(String channelCode) {
        User user = userDao.getChannelAdminByCode(channelCode);
        UserVO userVO = new UserVO();
        if (null != user) {
            userVO = this.convertToVO(user);
        }
        return userVO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserVO> listChannelUserByMobile(String mobile) {
        List<User> userList = userDao.listChannelUserByMobile(mobile);
        List<UserVO> userVOList = new ArrayList<>();
        if (null != userList && userList.size() > 0) {
            for (User user : userList) {
                UserVO userVO = this.convertToVO(user);
                userVOList.add(userVO);
            }
        }
        if (userVOList.size() < 1) {
            return Collections.emptyList();
        } else {
            return userVOList;
        }
    }

    @Cacheable(value = "roleCache", key = "#user.id")
    public List<String> listRoleCodes(User user) {
        Set<Role> roleSet = user.getRoleSet();
        List<String> codeList = new ArrayList<>();
        if (roleSet.size() > 0) {
            for (Role role : roleSet) {
                codeList.add(role.getRoleCode());
            }
        }
        return codeList;
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO getUserByMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return null;
        } else {
            User user = this.userDao.getByMobile(mobile);
            return this.convertToVO(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO getUserById(Integer id) {
        if (id == null) {
            return null;
        }
        User user = this.userDao.getById(id);
        return this.convertToVO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserVO> listChannelUserByCode(String channelCode) {
        List<User> userList = userDao.listChannelUserByCode(channelCode);
        List<UserVO> userVOList = new ArrayList<>();
        if (null != userList && userList.size() > 0) {
            for (User user : userList) {
                UserVO userVO = this.convertToVO(user);
                userVOList.add(userVO);
            }
        }
        return userVOList;
    }

    /**
     * 根据角色查找用户
     * @param roleCode
     * @return
     */
    public List<UserVO> getUsersByRoleCode(String roleCode) {
        if (StringUtils.isEmpty(roleCode)) {
            return Collections.emptyList();
        } else {
            List<User> users = this.userDao.listUsersByRoleCode(roleCode);
            if (users != null) {
                List<UserVO> voList = new ArrayList<>();
                for (User user : users) {
                    UserVO vo = this.convertToVO(user);
                    if (vo != null) {
                        voList.add(vo);
                    }
                }
                return voList;
            } else {
                return Collections.emptyList();
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO getChannelUserByMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return null;
        } else {
            User user = this.userDao.getChannelUserByMobile(mobile);
            return this.convertToVO(user);
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, Integer currentLoginUserId) {
        User user = userDao.getById(currentLoginUserId);
        if (null == user) {
            throw new FInsuranceBaseException(101506);
        }
        if (!user.getPassword().equals(passwordEncoder.encode(oldPassword))) {
            throw new FInsuranceBaseException(101510);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);
    }

    @Override
    public void resetPassword(Integer id) {
        User user = userDao.getById(id);
        if (null == user) {
            throw new FInsuranceBaseException(101506);
        }
        user.setPassword(passwordEncoder.encode(defaultPassword));
        userDao.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO getUserByWxOpenid(String appid, String openid) {
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(openid)) {
            return null;
        } else {
            UserOauth userOauth = this.userOauthDao.getUserOauthByOpenid(appid, openid, OauthType.WECHAT_MP.getCode());
            if (userOauth == null) {
                return null;
            } else {
                return this.convertToVO(userOauth.getUser());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserVO getUserByWxUnionid(String unionid) {
        if (StringUtils.isEmpty(unionid)) {
            return null;
        } else {
            UserOauth userOauth = this.userOauthDao.getUserOauthByWxUnionid(unionid);
            if (userOauth == null) {
                return null;
            } else {
                return this.convertToVO(userOauth.getUser());
            }
        }
    }

    @Override
    public boolean bindUserWithWxAccount(Integer userId, String appid, WxMpUser wxMpUser) {
        if (userId != null && StringUtils.isNotEmpty(appid) && wxMpUser != null) {
            User user = this.userDao.getById(userId);
            if (user == null) {
                return false;
            } else {
                UserOauth userOauth = this.userOauthDao.getUserOauthByOpenid(appid, wxMpUser.getOpenId(), OauthType.WECHAT_MP.getCode());
                if (userOauth == null) {
                    //检查目标用户是否已经绑定了微信公众号
                    Set<UserOauth> oauths = user.getUserOauths();
                    if (oauths != null && oauths.size() > 0) {
                        boolean hasBind = false;
                        for (UserOauth oauth : oauths) {
                            if (oauth != null && OauthType.WECHAT_MP.getCode().equalsIgnoreCase(oauth.getOauthType()) && appid.equals(oauth.getOauthAppId())) {
                                hasBind = true;
                            }
                        }
                        if (hasBind) {
                            throw new FInsuranceBaseException(106216); //该账号已经绑定了微信，不能重复绑定
                        }
                    }
                    userOauth = new UserOauth();
                    userOauth.setCreateBy(userId);
                    userOauth.setCreateAt(new Date());
                } else {
                    if (!userOauth.getOauthAccount().equals(wxMpUser.getOpenId()) || !userOauth.getWxUnionid().equals(wxMpUser.getUnionId())) {
                        throw new FInsuranceBaseException(106216); //该账号已经绑定了微信，不能重复绑定
                    }
                }
                userOauth.setUser(user);
                userOauth.setOauthType(OauthType.WECHAT_MP.getCode());
                userOauth.setOauthAppId(appid);
                userOauth.setOauthAccount(wxMpUser.getOpenId());
                userOauth.setNickName(EmojiParser.removeAllEmojis(wxMpUser.getNickname()));
                userOauth.setGener(wxMpUser.getSex());
                userOauth.setHeaderImage(wxMpUser.getHeadImgUrl());
                userOauth.setWxUnionid(wxMpUser.getUnionId());
                userOauth.setOauthContent(EmojiParser.removeAllEmojis(JSON.toJSONString(wxMpUser)));
                userOauth.setUpdateAt(new Date());
                this.userOauthDao.save(userOauth);
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public Integer saveChannelUser(UserVO userVO) {
        User user = userDao.getById(userVO.getId());
        if (user == null) {
            user = new User();
            user.setCreateAt(new Date());//设置公共字段
            user.setCreateBy(userVO.getCreateBy());
        } else {
            user.setUpdateAt(new Date());//设置公共字段
            user.setUpdateBy(userVO.getCreateBy());
        }
        if (StringUtils.isNotEmpty(userVO.getMobile())) {
            User preUser = userDao.getByMobile(userVO.getMobile());
            //验证手机号是否唯一
            if ((userVO.getId() == null && preUser != null) || (userVO.getId() != null && preUser != null && !userVO.getId().equals(preUser.getId()))) {
                logger.error("the mobile [" + userVO.getMobile() + "]" + "already used by user[ " + preUser.getId() + "]");
                throw new FInsuranceBaseException(101501);
            }
            user.setMobilePhone(userVO.getMobile());
        }
        user.setUserName(userVO.getName());
        user.setLocked(userVO.getIsLocked() != 0);
        user.setUserType(userVO.getUserType().toLowerCase().trim());
        user.setChannelAdmin(userVO.isChannelAdmin());
        user.setChannelCode(userVO.getChannelCode());

        //保存渠道子账户
        userDao.save(user);

        return user.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<UserVO> pageChannelUserByCode(String channelCode, Integer pageIndex, Integer pageSize) {
        Page<User> userPagination = userDao.pageChannelUserByCode(channelCode, pageIndex, pageSize);
        List<UserVO> userVOList = new ArrayList<>();
        if(userPagination != null && userPagination.getContent() != null && userPagination.getContent().size() > 0) {
            for (User user : userPagination.getContent()) {
                UserVO userVO = this.convertToVO(user);//转VO
                userVOList.add(userVO);
            }
        }
        if (userPagination == null || userPagination.getContent() == null || userPagination.getContent().size() < 1) {//无数据返回emptyList
            userVOList = Collections.emptyList();
        }
        return Pagination.createInstance(pageIndex, pageSize, userPagination == null ? 0 : userPagination.getTotalElements(), userVOList);
    }

    @Override
    @Transactional(readOnly = true)
    public OauthAccountVO getUserOauth(Integer userId, String oauthType, String appid) {
        if (StringUtils.isEmpty(oauthType) || StringUtils.isEmpty(appid) || userId == null) {
            return null;
        } else {
            UserOauth userOauth = this.userOauthDao.getUserOauthByUserId(userId, appid, oauthType);
            if (userOauth == null || userOauth.getUser().isLocked()) {
                return null;
            } else {
                return this.convertToVO(userOauth);
            }
        }
    }

    private OauthAccountVO convertToVO(UserOauth userOauth) {
        if (userOauth == null) {
            return null;
        } else {
            OauthAccountVO userOauthVO = new OauthAccountVO();
            userOauthVO.setId(userOauth.getId());
            userOauthVO.setUserId(userOauth.getUser().getId());
            userOauthVO.setOauthType(OauthType.codeOf(userOauth.getOauthType()));
            userOauthVO.setOauthAppId(userOauth.getOauthAppId());
            userOauthVO.setOauthAccount(userOauth.getOauthAccount());
            userOauthVO.setNickName(userOauth.getNickName());
            userOauthVO.setGender(userOauth.getGener());
            userOauthVO.setHeaderImage(userOauth.getHeaderImage());
            userOauthVO.setOauthContent(userOauth.getOauthContent());
            userOauthVO.setCreateAt(userOauth.getCreateAt());
            userOauthVO.setUpdateAt(userOauth.getUpdateAt());
            userOauthVO.setCreateBy(userOauth.getCreateBy());
            userOauthVO.setUpdateBy(userOauth.getUpdateBy());
            userOauthVO.setUserLocked(userOauth.getUser().isLocked());
            return userOauthVO;
        }
    }

    private UserVO convertToVO(User user) {
        if (user == null) {
            return null;
        } else {
            UserVO userVO = new UserVO();
            userVO.setId(user.getId());
            userVO.setName(user.getUserName());
            userVO.setMobile(null == user.getMobilePhone() ? "" : user.getMobilePhone());
            userVO.setPassword(user.getPassword());
            userVO.setUserType(user.getUserType());
            userVO.setOrganizationId(user.getOrganizationId());
            userVO.setChannelCode(user.getChannelCode());
            userVO.setChannelAdmin(user.isChannelAdmin());
            userVO.setIsLocked(user.isLocked() ? 1 : 0);
            userVO.setCrateAt(user.getCreateAt());
            List<String> codeList = this.listRoleCodes(user);
            userVO.setRoles(codeList);
            userVO.setRoleIds(this.getRoleIds(user));
            userVO.setDefaultPassword(defaultPassword);
            return userVO;
        }
    }

    @Cacheable(value = "roleIdCache", key = "#user.id")
    public String[] getRoleIds(User user) {
        Set<Role> roleSet = user.getRoleSet();
        Set<String> idsSet = new HashSet<>();
        if (roleSet.size() > 0) {
            for (Role role : roleSet) {
                idsSet.add(role.getId().toString());
            }
        }
        return idsSet.toArray(new String[roleSet.size()]);
    }
}
