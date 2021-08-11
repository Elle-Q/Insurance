package com.fintech.insurance.micro.biz.persist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fintech.insurance.components.persist.BaseEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 产品表
 *
 * @author liu man
 * @version 1.1.0
 * @since 2017-08-18 17:04:30
 */
@Entity
@Table(name = "busi_product")
public class Product extends BaseEntity {

    private static final long serialVersionUID = -8820461361835512413L;

    @Column(name = "product_name", length = 64, columnDefinition = "varchar(64) comment '产品名称'")
    private String productName;

    @Column(name = "product_code", length = 32, columnDefinition = "varchar(32) comment '产品编码，创建时自动生成，全局唯一'")
    private String productCode;

    @Column(name = "product_type", length = 16, columnDefinition = "varchar(16) comment '产品类型，分期或者抵押'")
    private String productType;

    @Column(name = "product_icon", length = 32, columnDefinition = "varchar(32) comment '产品图标'")
    private String productIcon;

    @Column(name = "product_banner", length = 32, columnDefinition = "varchar(32) comment '产品广告图'")
    private String productBanner;

    @Column(name = "isOnsale", columnDefinition = "boolean comment 'comment '1上架,0下架 是否已经上架'")
    private Boolean isOnsale;

    //还款方式  principal_interest:等额本息")
    @Column(name = "repay_type", length = 16, columnDefinition = "varchar(16) comment '还款类型'")
    private String repayType;

    //还款日类型 initial_payment：期初还款，final_payment：期末还款")
    @Column(name = "repay_day_type", length = 16, columnDefinition = "varchar(16) comment '还款日类型'")
    private String repayDayType;

    @Column(name = "service_fee_rate", columnDefinition = "double comment '服务费率（万倍）'")
    private Double serviceFeeRate;

    @Column(name = "other_fee_rate", columnDefinition = "double comment '其他费用的费率（万倍）'")
    private Double otherFeeRate;

    @Column(name = "prepayment_penalty_rate", columnDefinition = "double comment '提前还款罚息率（万倍）'")
    private Double prepaymentPenaltyRate;

    @Column(name = "prepayment_days", columnDefinition = "int comment '提前还款天数'")
    private Integer prepaymentDays = 0;

    @Column(name = "overdue_fine_rate", columnDefinition = "double comment '逾期罚息率（万倍）'")
    private Double overdueFineRate;

    @Column(name = "max_overdue_days", columnDefinition = "int comment '最大逾期天数'")
    private Integer maxOverdueDays;

    @Column(name = "product_description", columnDefinition = "text comment '产品描述'")
    private String productDescription;

    @Column(name = "created_by", columnDefinition = "int comment '创建者'")
    private Integer createBy;

    @Column(name = "updated_by", columnDefinition = "int comment '更新者'")
    private Integer updateBy;

    //产品描述
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "busi_product_channel",
            joinColumns = @JoinColumn(name = "product_id", columnDefinition = "int(11) not null comment '产品主键'"),
            inverseJoinColumns = @JoinColumn(name = "channel_id", columnDefinition = "int(11) comment '渠道主键'"))
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Channel> channelSet = new HashSet<Channel>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "product")
    private Set<ProductRate> productRateSet;

    public Set<ProductRate> getProductRateSet() {
        return productRateSet;
    }

    public void setProductRateSet(Set<ProductRate> productRateSet) {
        this.productRateSet = productRateSet;
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

    public Boolean getOnsale() {
        return isOnsale;
    }

    public void setOnsale(Boolean onsale) {
        isOnsale = onsale;
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

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Set<Channel> getChannelSet() {
        return channelSet;
    }

    public void setChannelSet(Set<Channel> channelSet) {
        this.channelSet = channelSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (productName != null ? !productName.equals(product.productName) : product.productName != null) return false;
        if (productCode != null ? !productCode.equals(product.productCode) : product.productCode != null) return false;
        if (productType != null ? !productType.equals(product.productType) : product.productType != null) return false;
        if (productIcon != null ? !productIcon.equals(product.productIcon) : product.productIcon != null) return false;
        if (productBanner != null ? !productBanner.equals(product.productBanner) : product.productBanner != null)
            return false;
        if (isOnsale != null ? !isOnsale.equals(product.isOnsale) : product.isOnsale != null) return false;
        if (repayType != null ? !repayType.equals(product.repayType) : product.repayType != null) return false;
        if (repayDayType != null ? !repayDayType.equals(product.repayDayType) : product.repayDayType != null)
            return false;
        if (serviceFeeRate != null ? !serviceFeeRate.equals(product.serviceFeeRate) : product.serviceFeeRate != null)
            return false;
        if (otherFeeRate != null ? !otherFeeRate.equals(product.otherFeeRate) : product.otherFeeRate != null)
            return false;
        if (prepaymentPenaltyRate != null ? !prepaymentPenaltyRate.equals(product.prepaymentPenaltyRate) : product.prepaymentPenaltyRate != null)
            return false;
        if (prepaymentDays != null ? !prepaymentDays.equals(product.prepaymentDays) : product.prepaymentDays != null)
            return false;
        if (overdueFineRate != null ? !overdueFineRate.equals(product.overdueFineRate) : product.overdueFineRate != null)
            return false;
        if (maxOverdueDays != null ? !maxOverdueDays.equals(product.maxOverdueDays) : product.maxOverdueDays != null)
            return false;
        if (productDescription != null ? !productDescription.equals(product.productDescription) : product.productDescription != null)
            return false;
        if (createBy != null ? !createBy.equals(product.createBy) : product.createBy != null) return false;
        if (updateBy != null ? !updateBy.equals(product.updateBy) : product.updateBy != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = productName != null ? productName.hashCode() : 0;
        result = 31 * result + (productCode != null ? productCode.hashCode() : 0);
        result = 31 * result + (productType != null ? productType.hashCode() : 0);
        result = 31 * result + (productIcon != null ? productIcon.hashCode() : 0);
        result = 31 * result + (productBanner != null ? productBanner.hashCode() : 0);
        result = 31 * result + (isOnsale != null ? isOnsale.hashCode() : 0);
        result = 31 * result + (repayType != null ? repayType.hashCode() : 0);
        result = 31 * result + (repayDayType != null ? repayDayType.hashCode() : 0);
        result = 31 * result + (serviceFeeRate != null ? serviceFeeRate.hashCode() : 0);
        result = 31 * result + (otherFeeRate != null ? otherFeeRate.hashCode() : 0);
        result = 31 * result + (prepaymentPenaltyRate != null ? prepaymentPenaltyRate.hashCode() : 0);
        result = 31 * result + (prepaymentDays != null ? prepaymentDays.hashCode() : 0);
        result = 31 * result + (overdueFineRate != null ? overdueFineRate.hashCode() : 0);
        result = 31 * result + (maxOverdueDays != null ? maxOverdueDays.hashCode() : 0);
        result = 31 * result + (productDescription != null ? productDescription.hashCode() : 0);
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        result = 31 * result + (updateBy != null ? updateBy.hashCode() : 0);
        return result;
    }
}
