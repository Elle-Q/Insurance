package com.fintech.insurance.micro.system.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description: 系统用户实体
 * @Author: qxy
 * @Date: 2017/11/11 10:30
 */
@Entity
@Table(name = "sys_user")
public class User extends BaseEntity implements Serializable {

    @Column(name = "mobile_phone", length = 15, columnDefinition = "varchar(15) comment '手机号码'")
    private String mobilePhone;

    @Column(name = "password", length = 128, columnDefinition = "varchar(128) comment '登陆密码'")
    private String password;

    @Column(name = "user_name", length = 64, columnDefinition = "varchar(64) comment '用户姓名'")
    private String userName;

    @Column(name = "is_locked", columnDefinition = "comment '是否已经被锁定'")
    private boolean isLocked;

    @Column(name = "user_type", length = 32, columnDefinition = "varchar(32) comment '用户类型'")
    private String userType;

    @Column(name = "channel_code", columnDefinition = "varchar(32) comment '渠道编号'")
    private String channelCode;

    @Column(name = "organization_id", length = 10, columnDefinition = "int(10) comment '用户所在公司'")
    private Integer organizationId;

    @Column(name = "is_channel_admin", columnDefinition = "comment '是否为渠道管理员'")
    private boolean isChannelAdmin;

    @Column(name = "last_login_time", length = 19, columnDefinition = "timestamp comment '最近登录时间'")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;

    @Column(name = "last_login_ip", columnDefinition = "varchar(32) comment '最近登录来源IP'")
    private String lastLoginIp;

    @Column(name = "created_by", columnDefinition = "int(11) comment '创建人主键'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int(11) comment '更新人主键'")
    private Integer updateBy;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id", columnDefinition = "int(11) not null comment '用户主键'"),
            inverseJoinColumns = @JoinColumn(name = "role_id", columnDefinition = "int(11) comment '角色主键'"))
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Role> roleSet = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserOauth> userOauths;

    public Set<Role> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Set<UserOauth> getUserOauths() {
        return userOauths;
    }

    public void setUserOauths(Set<UserOauth> userOauths) {
        this.userOauths = userOauths;
    }
}
