package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.micro.dto.validate.groups.Find;
import com.fintech.insurance.micro.dto.validate.groups.Save;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 产品VO
 *
 * @author liu man
 * @version 1.1.0
 * @since 2017-08-18 17:04:30
 */

public class ProductVO implements Serializable {

    private static final long serialVersionUID = -8820461361835512417L;

    //主键id
    private Integer id;

    //产品名称
    @NotBlank(groups = {Save.class}, message ="104107")
    private String productName;

    //产品编码，创建时自动生成，全局唯一
    private String productCode;

    //保单贷款,car_instalments:车贷分期
    @NotBlank(groups = {Save.class}, message ="104107")
    private String productType;

    //产品图标
    private String productIcon;

    //产品广告图
    @NotBlank(groups = {Save.class}, message ="104107")
    private String productBanner;

    // comment '1上架,0下架
    private Integer isOnsale;

    //还款方式  principal_interest:等额本息")
    @NotBlank(groups = {Save.class}, message ="104107")
    private String repayType;

   //还款日类型 initial_payment：期初还款，final_payment：期末还款")
    @NotBlank(groups = {Save.class}, message ="104107")
    private String repayDayType;

    //服务费率（万倍）
    @FinanceDataPoint
    @NotNull(groups = {Save.class}, message ="104107")
    private Double serviceFeeRate;

    //其他费用的费率（万倍）
    @FinanceDataPoint
    @NotNull(groups = {Save.class}, message ="104107")
    private Double otherFeeRate;

    //提前还款罚息率（万倍）
    @FinanceDataPoint
    private Double prepaymentPenaltyRate = 0D;

    //提前还款天数
    private Integer prepaymentDays = 0;

     //逾期罚息率（万倍）
     @NotNull(groups = {Save.class}, message ="104107")
     @FinanceDataPoint
    private Double overdueFineRate;

    //可借款比例（万倍）
    @FinanceDataPoint
    private Double loanRatio;

    //最大逾期天数
    @NotNull(groups = {Save.class}, message ="104107")
    private Integer maxOverdueDays;

    //产品描述
    @NotBlank(groups = {Save.class}, message ="104107")
    private String productDescription;

    @FinanceDataPoint
    private Double interestRate;

    //创建人主键
    private Integer createBy;

    //创建时间
    private Date createAt = new Date();

    //更新人主键
    private Integer updateBy;

    //更新时间
    private Date updateAt;

    //渠道id集合
    private Integer[] channelIds;

    //产品利率集合
    private List<ProductRateVO> productRateVOList;

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductIcon() {
        return productIcon;
    }

    public void setProductIcon(String productIcon) {
        this.productIcon = productIcon;
    }

    public String getProductBanner() {
        return productBanner;
    }

    public void setProductBanner(String productBanner) {
        this.productBanner = productBanner;
    }

    public Integer getIsOnsale() {
        return isOnsale;
    }

    public void setIsOnsale(Integer isOnsale) {
        this.isOnsale = isOnsale;
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

    public Double getOtherFeeRate() {
        return otherFeeRate;
    }

    public void setOtherFeeRate(Double otherFeeRate) {
        this.otherFeeRate = otherFeeRate;
    }

    public Double getPrepaymentPenaltyRate() {
        return prepaymentPenaltyRate;
    }

    public void setPrepaymentPenaltyRate(Double prepaymentPenaltyRate) {
        this.prepaymentPenaltyRate = prepaymentPenaltyRate;
    }

    public Integer getPrepaymentDays() {
        return prepaymentDays;
    }

    public void setPrepaymentDays(Integer prepaymentDays) {
        this.prepaymentDays = prepaymentDays;
    }

    public Double getOverdueFineRate() {
        return overdueFineRate;
    }

    public void setOverdueFineRate(Double overdueFineRate) {
        this.overdueFineRate = overdueFineRate;
    }

    public Double getLoanRatio() {
        return loanRatio;
    }

    public void setLoanRatio(Double loanRatio) {
        this.loanRatio = loanRatio;
    }

    public Integer getMaxOverdueDays() {
        return maxOverdueDays;
    }

    public void setMaxOverdueDays(Integer maxOverdueDays) {
        this.maxOverdueDays = maxOverdueDays;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Integer[] getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(Integer[] channelIds) {
        this.channelIds = channelIds;
    }

    public List<ProductRateVO> getProductRateVOList() {
        return productRateVOList;
    }

    public void setProductRateVOList(List<ProductRateVO> productRateVOList) {
        this.productRateVOList = productRateVOList;
    }
}
