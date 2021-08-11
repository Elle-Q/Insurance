package com.fintech.insurance.micro.vo.wechat;

import com.fintech.insurance.commons.annotations.ImageUrl;

import java.io.Serializable;


/**
 * 微信产品VO
 *
 * @author liu man
 * @version 1.1.0
 * @since 2017-08-18 17:04:30
 */

public class WeChatProductVO implements Serializable {

    private static final long serialVersionUID = -8820461361835512417L;

    //主键id
    private Integer id;

    //产品描叙
    private String productDescription;

    //产品名称
    private String productName;

    //产品类型
    private String productType;

    //产品广告图
    @ImageUrl
    private String productBanner;

    //产品广告图url
    public String productBannerUrl;

    //产品广告缩略图url
    public String productBannerNarrowUrl;

    // 是否冻结
    private Integer isLocked;

    public String getProductBannerUrl() {
        return productBannerUrl;
    }

    public void setProductBannerUrl(String productBannerUrl) {
        this.productBannerUrl = productBannerUrl;
    }

    public String getProductBannerNarrowUrl() {
        return productBannerNarrowUrl;
    }

    public void setProductBannerNarrowUrl(String productBannerNarrowUrl) {
        this.productBannerNarrowUrl = productBannerNarrowUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
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

    public String getProductBanner() {
        return productBanner;
    }

    public void setProductBanner(String productBanner) {
        this.productBanner = productBanner;
    }

    public Integer getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }
}
