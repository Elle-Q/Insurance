package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.biz.persist.entity.Channel;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import com.fintech.insurance.micro.dto.biz.ProductChannelVO;

import java.util.List;

/**
 * @Description: (渠道管理)
 * @Author: qxy
 * @Date: 2017/11/13 10:28
 */
public interface ChannelService {

    //获取渠道集合
    public List<Channel> findChannelVOByIds(List<Integer> ids);

    /**
     * 创建编辑渠道
     *
     * @param channelVO             渠道VO
     * @param currentLoginUserID    当前登录用户id
     */
    Channel saveChannel(ChannelVO channelVO, Integer currentLoginUserID);

    /**
     * 分页查询渠道信息
     *
     * @param channelCode   渠道编码
     * @param channelName   渠道名称
     * @param companyIds    公司id集合
     * @param channelCodes  渠道code集合
     */
    Pagination<ChannelVO> queryChannel(String channelCode, String channelName, List<Integer> companyIds, List<String> channelCodes, Integer pageIndex, Integer pageSize);

    /**
     * 查询待增加的渠道
     *
     * @param productId 产品id
     * @belongFlag 是否属于productId 这个产品
     * @return
     */
    public List<ChannelVO> queryChannelByProductIdAndBelongFlag(Integer productId , boolean belongFlag);


    ChannelVO getDetail(Integer id);

     /**
     * 新增产品渠道
     *
     * @param pcVO 产品渠道VO
     * @return
     */
    void addProductChannel(ProductChannelVO pcVO);

    /**
     * 删除产品渠道
     *
     * @param pcVO 产品渠道VO
     * @return
     */
    void deleteProductChannel(ProductChannelVO pcVO);

    /**
     * 冻结或解冻渠道
     * @param id        渠道id
     * @param isLocked  是否锁定
     */
    void freezeOrUnfreeze(Integer id, Boolean isLocked);

    /**
     * 通过客户号获取渠道
     * @param codes   渠道集合
     * @param pageIndex   页数
     * @param pageSize   每页数
     */
    Pagination<ChannelVO> findChannelByChannelCodes(List<String> codes, Integer pageIndex, Integer pageSize);

    /**
     * 根据渠道code 查找渠道
     * @param channelCode 渠道code
     * @return
     */
    ChannelVO getChannelDetailByChannelCode(String channelCode);

    /**
     * 根据业务id和客户id 查找默认渠道
     * @param requisitionId 业务id
     * @param customerId 客户id
     * @return
     */
    ChannelVO getAcquiescenceChannelByRequisitionIdAndCustomerId(Integer requisitionId, Integer customerId);

}
