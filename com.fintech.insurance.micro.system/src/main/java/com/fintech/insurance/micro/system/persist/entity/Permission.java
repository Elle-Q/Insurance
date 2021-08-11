package com.fintech.insurance.micro.system.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 权限表
 */
@Entity
@Table(name = "sys_permission")
public class Permission extends BaseEntity implements Serializable {

    @Column(name = "permission_code", length = 15, columnDefinition = "varchar(15) comment '权限编码，全局唯一，创建时自动生成，推荐使用拼音首字母拼接'")
    private String permissionCode;

    @Column(name = "permission_name", length = 128, columnDefinition = "varchar(128) comment '权限名称'")
    private String permissionName;

    @Column(name = "remark", columnDefinition = "varchar(1024) character set utf8 comment '备注'")
    private String Remark;

    @Column(name = "created_by", columnDefinition = "int(11) comment '创建人主键'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int(11) comment '更新人主键'")
    private Integer updateBy;

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
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
