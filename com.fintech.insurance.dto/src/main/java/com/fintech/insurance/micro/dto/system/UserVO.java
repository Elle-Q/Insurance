package com.fintech.insurance.micro.dto.system;

import com.fintech.insurance.micro.dto.biz.BaseVO;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class UserVO extends BaseVO{

    public static final String ID = "id"; //用户的id
    public static final String NAME = "name"; //用户的名称
    public static final String USER_TYPE = "user_type"; //用户的类型
    public static final String CHANNEL_CODE = "channel_code"; //渠道编码
    public static final String ORG_ID = "organization_id"; //内部用户所在的公司id
    public static final String IS_CHANNEL_ADMIN = "channel_admin"; //用户是否为渠道管理员

    //id
    @NotNull(groups = {Update.class}, message = "101505")
    private Integer id;

    //姓名
    private String name;

    //密码
    private String password;

    //手机号
    private String mobile;

    //角色名
    private List<String> roles;

    //角色ids
    private String[] roleIds;

    //创建时间
    private Date crateAt;

    //用户状态
    private Integer isLocked;

    //用户类型
    private String userType;

    //渠道编号
    private String channelCode;

    //用户所在公司
    private Integer organizationId;

    //是否为渠道管理员
    private boolean isChannelAdmin;

    //是否为超级管理员
    private Integer isSuperAdmin;

    //默认密码
    private String defaultPassword;


    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public Integer getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(Integer isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public boolean isChannelAdmin() {
        return isChannelAdmin;
    }

    public void setChannelAdmin(boolean channelAdmin) {
        isChannelAdmin = channelAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Date getCrateAt() {
        return crateAt;
    }

    public void setCrateAt(Date crateAt) {
        this.crateAt = crateAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    public Integer getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }
}
