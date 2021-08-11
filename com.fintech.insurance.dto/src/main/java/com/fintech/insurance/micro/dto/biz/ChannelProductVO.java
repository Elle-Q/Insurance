package com.fintech.insurance.micro.dto.biz;


import com.fintech.insurance.commons.annotations.FinanceDataPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 渠道产品vo
 *
 * @author liu man
 * @version 1.1.0
 * @since 2017-08-18 17:04:30
 */

public class ChannelProductVO implements Serializable {

    private static final long serialVersionUID = -8820461361835512417L;

    //渠道id
    private Integer channelId;

    //默认选中的产品id
    private Integer productId;

    //渠道编号，创建时自动生成，全局唯一
    private String channelCode;

    //渠道名称
    private String channelName;

    //渠道下的所有产品
    List<SimpleProductVO> simpleProductVOList = new ArrayList<SimpleProductVO>();

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public List<SimpleProductVO> getSimpleProductVOList() {
        return simpleProductVOList;
    }

    public void setSimpleProductVOList(List<SimpleProductVO> simpleProductVOList) {
        this.simpleProductVOList = simpleProductVOList;
    }
}
