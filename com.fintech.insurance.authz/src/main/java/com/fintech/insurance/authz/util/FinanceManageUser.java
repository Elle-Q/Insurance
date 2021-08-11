package com.fintech.insurance.authz.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * 运营管理平台用户对象，用于鉴权
 */
public class FinanceManageUser implements UserDetails {

    private Integer id = 0;

    private String mobilePhone = "";

    private String password = "";

    private String userName = "";

    private boolean isLocked = false;

    private String userType = "";

    private String channelCode = "";

    private Integer organizationId = 0;

    private boolean isChannelAdmin = false;

    private List<String> roles = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id != null) {
            this.id = id;
        }
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        if (mobilePhone != null) {
            this.mobilePhone = mobilePhone;
        }
    }

    public void setPassword(String password) {
        if (password != null) {
            this.password = password;
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if (userName != null) {
            this.userName = userName;
        }
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        if (userType != null) {
            this.userType = userType;
        }
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        if (channelCode != null) {
            this.channelCode = channelCode;
        }
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        if (organizationId != null) {
            this.organizationId = organizationId;
        }
    }

    public boolean isChannelAdmin() {
        return isChannelAdmin;
    }

    public void setChannelAdmin(boolean channelAdmin) {
        isChannelAdmin = channelAdmin;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        if (roles != null) {
            this.roles = roles;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<String> handledRoles = new HashSet<>();
        if (this.roles != null && this.roles.size() > 0) {
            for (String role : roles) {
                if (!handledRoles.contains(role)) {
                    authorities.add(new SimpleGrantedAuthority(role));
                    handledRoles.add(role);
                }
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.mobilePhone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //账号永不过期
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; //密码永不过期
    }

    @Override
    public boolean isEnabled() {
        return !this.isLocked; //与锁定同义
    }
}
