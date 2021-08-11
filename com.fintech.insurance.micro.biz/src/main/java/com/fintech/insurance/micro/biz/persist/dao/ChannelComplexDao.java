package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.micro.biz.persist.entity.Channel;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @Description: (渠道管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
@NoRepositoryBean
public interface ChannelComplexDao {

    /**
     * 分页查询渠道信息
     *
     * @param channelCode
     * @param channelName
     */
    Page<Channel> queryChannel(String channelCode, String channelName, List<Integer> companyIds, List<String> channelCodes, int pageIndex, int pageSize);

    /**
     * 查询产品的渠道
     *
     * @param productId 产品id
     * @param belongFlag 是否属于该产品
     * @return
     */
    public List<Channel> queryChannelByProductIdAndBelongFlag(Integer productId, boolean belongFlag);

    /**
     * 查询渠道集合
     *
     * @param ids 渠道主键id集合
     * @return
     */
    public List<Channel> findChannelVOByIds(List<Integer> ids);

    /**
     * 通过客户号查询客户渠道
     *
     * @param codes 渠道code集合
     * @param pageIndex 页数
     * @param pageSize 每页数
     * @return
     */
    public Page<Channel> findChannelByChannelCodes(List<String> codes, Integer pageIndex, Integer pageSize);
}
