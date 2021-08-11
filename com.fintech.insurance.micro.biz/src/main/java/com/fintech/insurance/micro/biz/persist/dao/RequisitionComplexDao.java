package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Date;
import java.util.List;

/**
 * @Description: (渠道管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
@NoRepositoryBean
public interface RequisitionComplexDao {

    /**
     * 分页查询业务信息
     *
     * @param requisitionNumber 申请单号
     * @param requisitionStatus 申请单状态
     * @param productType       产品类型
     * @param channelName       渠道名称
     * @param beginDate         提交开始时间
     * @param endDate           提交结束时间
     * @param customerIds      客户id
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Page<Requisition> queryRequisition(String requisitionNumber, RequisitionStatus requisitionStatus, ProductType productType, String channelName,
                                       Date beginDate, Date endDate, List<Integer> customerIds, Integer pageIndex, Integer pageSize);

    Page<Requisition> queryRequisition(String requisitionNumber, String requisitionStatus, ProductType productType, String channelName,
                                       List<Integer> customerIds, Integer pageIndex, Integer pageSize);


    /**
     * 分页查询客户申请单信息
     * @param customerIdInt
     * @param productType
     * @param requisitionStatus
     * @return
     */
    Page<Requisition> pageRequisitionByCustomerId(List<Integer> customerIdInt, List<Integer> channelUserIdInt, String productType, String requisitionStatus, Integer pageIndex, Integer pageSize);

    /**
     * 根据客户id或者业务id查找客户最近业务单
     * @param requisitionId 业务单id
     * @param customerId 客户id
     * @return
     */
    Requisition getAcquiescenceChannelByRequisitionIdAndCustomerId(Integer requisitionId, Integer customerId);

    Integer countRequisitionByStatus(List<Integer> idInts, Integer customerId, String requisitionStatus);
}
