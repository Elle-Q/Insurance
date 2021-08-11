package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ResourceType;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetailResource;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @Description: (渠道管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
@NoRepositoryBean
public interface RequisitionDetailResourceComplexDao {

    /**
     * 查看车辆详情
     *
     * @param requisitionDetailId 资源id
     * @param resourceType        资源类型
     **/
    List<RequisitionDetailResource> listRequisitionDetailResource(Integer requisitionDetailId, ResourceType resourceType);

}
