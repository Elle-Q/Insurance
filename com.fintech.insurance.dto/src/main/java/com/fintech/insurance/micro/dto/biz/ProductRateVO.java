package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.micro.dto.validate.groups.Save;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 产品利率VO
 *
 * @author liu man
 * @version 1.1.0
 * @since 2017-08-18 17:04:30
 */

public class ProductRateVO implements Serializable {

    private static final long serialVersionUID = -8820461361835512417L;

    //主键id
    private Integer id;

    //业务期数
    private Integer businessDuration;

    //计息类型，按月或者按日计息
    private String interestType;

    //利率点（万倍）
    @FinanceDataPoint
    private Double interestRate;

    //可借比例（万倍）
    @FinanceDataPoint
    private Double loanRatio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusinessDuration() {
        return businessDuration;
    }

    public void setBusinessDuration(Integer businessDuration) {
        this.businessDuration = businessDuration;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getLoanRatio() {
        return loanRatio;
    }

    public void setLoanRatio(Double loanRatio) {
        this.loanRatio = loanRatio;
    }
}
