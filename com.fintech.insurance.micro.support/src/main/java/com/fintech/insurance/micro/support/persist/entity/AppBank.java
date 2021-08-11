package com.fintech.insurance.micro.support.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "data_app_bank")
public class AppBank extends BaseEntity implements Serializable {

    @Column(name = "bank_code", columnDefinition = "varchar(16) comment '银行code'")
    private String bankCode;

    @Column(name = "bank_name", columnDefinition = "varchar(128) comment '银行名称'")
    private String bankName;

    @Column(name = "created_by", columnDefinition = "int(11) comment '创建人主键'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int(11) comment '更新人主键'")
    private Integer updateBy;

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

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
