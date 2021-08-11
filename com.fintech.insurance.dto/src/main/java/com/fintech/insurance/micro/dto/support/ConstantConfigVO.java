package com.fintech.insurance.micro.dto.support;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description: 常量配置VO
 * @Date: 2017/11/09 09:49
 */
public class ConstantConfigVO implements Serializable {
    // 配置名称
    private String configName;
    // 配置编号
    private String configCode;
    // 配置值
    private Object configValue;
    // 单位后缀
    private String unitSuffix;

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

    public Object getConfigValue() {
        return configValue;
    }

    public void setConfigValue(Object configValue) {
        this.configValue = configValue;
    }

    public String getUnitSuffix() {
        return unitSuffix;
    }

    public void setUnitSuffix(String unitSuffix) {
        this.unitSuffix = unitSuffix;
    }


}
