package com.fintech.insurance.micro.system.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sys_user_role")
public class UserRole extends BaseEntity implements Serializable{

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "created_by", columnDefinition = "int(11) comment '创建人主键'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int(11) comment '更新人主键'")
    private Integer updateBy;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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
}
