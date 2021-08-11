package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @Description: (渠道管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
@NoRepositoryBean
public interface RequisitionDetailComplexDao {

    /**
     * 查看车辆详情
     *
     * @param contractId        合同id
     **/
    List<RequisitionDetail> listRequisitionDetail(Integer contractId);

    /**
     * 查询车辆信息
     * @param requisitionId 业务单id
     * @param pageIndex 页数
     * @param pageSize  每页数
     * @return
     */
    Page<RequisitionDetail> findRequisitionDetailByRequisitionId(Integer requisitionId, Integer pageIndex, Integer pageSize);

    /**
     * 查看正在申请的车
     *
     * @param CarNumber  车牌号
     **/
    RequisitionDetail findLoaningRequisitionDetailByCarNumber(String CarNumber);

}
