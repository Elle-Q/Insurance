package com.fintech.insurance.micro.dto.biz;

import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/12 18:10
 */
public class ProductChannelDetailVO {

    private ProductVO productVO;

    private List<ChannelVO> channelList;

    public ProductChannelDetailVO() {
    }

    public ProductChannelDetailVO(ProductVO productVO, List<ChannelVO> channelList) {
        this.productVO = productVO;
        this.channelList = channelList;
    }

    public ProductVO getProductVO() {
        return productVO;
    }

    public void setProductVO(ProductVO productVO) {
        this.productVO = productVO;
    }

    public List<ChannelVO> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<ChannelVO> channelList) {
        this.channelList = channelList;
    }
}
