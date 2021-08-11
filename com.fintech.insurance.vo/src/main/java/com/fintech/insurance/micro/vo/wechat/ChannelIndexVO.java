package com.fintech.insurance.micro.vo.wechat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/8 14:03
 */
public class ChannelIndexVO implements Serializable {
    // 广告列表
    List<AdVO> adVOList = new ArrayList<>();
    // 产品列表
    List<ProductVO> productVOList = new ArrayList<>();

    // 是否冻结
    Integer isLocked = 0;

    public List<AdVO> getAdVOList() {
        return adVOList;
    }

    public void setAdVOList(List<AdVO> adVOList) {
        if (adVOList != null) {
            this.adVOList = adVOList;
        }
    }

    public List<ProductVO> getProductVOList() {
        return productVOList;
    }

    public void setProductVOList(List<ProductVO> productVOList) {
        if (productVOList != null) {
            this.productVOList = productVOList;
        }
    }

    public Integer getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }
}
