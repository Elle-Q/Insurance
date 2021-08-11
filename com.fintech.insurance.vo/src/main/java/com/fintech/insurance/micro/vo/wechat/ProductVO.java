package com.fintech.insurance.micro.vo.wechat;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;

/**
 * @Author: Clayburn
 * @Description: 微信渠道端首页产品VO
 * @Date: 2017/12/8 13:56
 */
public class ProductVO implements Serializable {
    // 产品id
    private Integer id;
    // 产品名称
    private String productName;
    // 产品类型
    private String productType;
    // 服务费率 = 服务费率 + 其他费率
    @FinanceDataPoint
    private Double serviceFeeRate;
    // 最小月利率
    @FinanceDataPoint
    private Double minInterestRate;
    // 最大月利率
    @FinanceDataPoint
    private Double maxInterestRate;
    // 还款方式
    private String repayType;
    // 还款日类型
    private String repayDayType;

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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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
}
