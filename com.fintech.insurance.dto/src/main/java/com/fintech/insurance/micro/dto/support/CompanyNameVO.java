package com.fintech.insurance.micro.dto.support;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description: 公司名称
 * @Date: 2017/11/10 10:18
 */
public class CompanyNameVO implements Serializable {
    @NotBlank(message = "102015")
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
