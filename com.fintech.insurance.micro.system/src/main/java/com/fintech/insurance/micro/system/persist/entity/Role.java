package com.fintech.insurance.micro.system.persist.entity;


import com.fintech.insurance.components.persist.BaseEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description: 用户角色实体
 * @Author: qxy
 * @Date: 2017/11/11 10:30
 */
@Entity
@Table(name = "sys_role")
public class Role extends BaseEntity implements Serializable {

    @Column(name = "role_code", length = 32, columnDefinition = "varchar(32) comment '角色编码'")
    private String roleCode;

    @Column(name = "role_name", length = 64, columnDefinition = "varchar(64) comment '角色名称'")
    private String roleName;

    @Column(name = "remark", columnDefinition = "varchar(1024) character set utf8 comment '备注'")
    private String remark;

    @Column(name = "created_by", columnDefinition = "int(11) comment '创建人主键'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int(11) comment '更新人主键'")
    private Integer updateBy;

    //角色
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "roleSet")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<User> userSet = new HashSet<>();

    public Role() {
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
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

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (this.getId() != null && role.getId() != null) {
            return this.getId().equals(role.getId());
        } else {
            return super.equals(o);
        }
    }

    @Override
    public int hashCode() {
        if (this.getId() != null) {
            return this.getId();
        }
        return super.hashCode();
    }
}
