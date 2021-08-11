package com.fintech.insurance.micro.support.persist.entity;

import com.fintech.insurance.components.persist.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 常量数据配置表
 */
@Entity
@Table(name = "data_config_property")
public class ConfigProperty extends BaseEntity implements Serializable {

    @Column(name = "config_name", columnDefinition = "varchar(32) comment '配置名称'")
    private String configName;

    @Column(name = "config_code", columnDefinition = "varchar(32) comment '配置项编码，全局唯一'")
    private String configCode;

    @Column(name = "data_type", columnDefinition = "varchar(16) comment '数据类型，分string,number,date,datetime,rate(利率，一万倍)'")
    private String dataType;

    @Column(name = "is_collection", columnDefinition = "boolean comment '是否为集合，是则值存储为一个ｊｓｏｎ数组'")
    private Boolean isCollection;

    @Column(name = "config_value", columnDefinition = "text comment '配置值'")
    private String configValue;

    @Column(name = "unit_suffix", columnDefinition = "varchar(16) comment '单位后缀'")
    private String unitSuffix;

    @Column(name = "created_by", columnDefinition = "int(11) comment '创建人主键'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int(11) comment '更新人主键'")
    private Integer updateBy;

    public String getUnitSuffix() {
        return unitSuffix;
    }

    public void setUnitSuffix(String unitSuffix) {
        this.unitSuffix = unitSuffix;
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

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Boolean getCollection() {
        return isCollection;
    }

    public void setCollection(Boolean collection) {
        isCollection = collection;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
}
