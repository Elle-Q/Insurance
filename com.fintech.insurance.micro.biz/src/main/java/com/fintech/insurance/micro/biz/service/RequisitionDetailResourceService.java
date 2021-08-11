package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.enums.ResourceType;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetailResource;
import com.fintech.insurance.micro.dto.biz.RequisitionDetailResourceVO;

import java.util.List;

/**
 * @Description: (业务详情资源)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
public interface RequisitionDetailResourceService {

    public RequisitionDetailResourceVO getRequisitionDetailResourceById(Integer id);

    /**
     * 查看车辆详情
     *
     * @param requisitionDetailId 资源id
     * @param resourceType        资源类型
     **/
    List<RequisitionDetailResource> listRequisitionDetailResource(Integer requisitionDetailId, ResourceType resourceType);

    /**
     * 查看车辆详情
     *
     * @param requisitionDetail 业务id
     * @param resourceVOList     资源类型
     **/
    void saveRequisitionDetailResource(RequisitionDetail requisitionDetail, List<RequisitionDetailResourceVO> resourceVOList);

    /**
     * 查看车辆详情
     *
     * @param resourceList   资源类型
     **/
    void deleteRequisitionDetailResource(List<RequisitionDetailResource> resourceList);

    /**
     * 保存车辆图片
     *
     * @param requisitionDetailResource 图片
     **/
    void saveRequisitionDetailResource(RequisitionDetailResource requisitionDetailResource);
}
