package com.fintech.insurance.micro.vo.wechat;


import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;

/**
 * 产品VO
 *
 * @author liu man
 * @version 1.1.0
 * @since 2017-08-18 17:04:30
 */

public class WeChatSimpleProductVO implements Serializable {

    private static final long serialVersionUID = -8820461361835512417L;

    //产品id
    private Integer id;
    //产品名称
    private String   productName;
    //产品还款方式
    private String   repayType;
    //产品还款类型
    private String   repayDayType;
    //产品服务费率
    @FinanceDataPoint
    private Double   serviceFeeRate;
    //产品最小利率
    @FinanceDataPoint
    private Double   minInterestRate;
    //产品最大利率
    @FinanceDataPoint
    private Double   maxInterestRate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getRepayDayType() {
        return repayDayType;
    }

    public void setRepayDayType(String repayDayType) {
        this.repayDayType = repayDayType;
    }

    public Double getServiceFeeRate() {
        return serviceFeeRate;
    }

    public void setServiceFeeRate(Double serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
    }

    public Double getMinInterestRate() {
        return minInterestRate;
    }

    public void setMinInterestRate(Double minInterestRate) {
        this.minInterestRate = minInterestRate;
    }

    public Double getMaxInterestRate() {
        return maxInterestRate;
    }

    public void setMaxInterestRate(Double maxInterestRate) {
        this.maxInterestRate = maxInterestRate;
    }
}
