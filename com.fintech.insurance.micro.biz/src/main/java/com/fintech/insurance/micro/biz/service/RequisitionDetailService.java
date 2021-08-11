package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import com.fintech.insurance.micro.dto.biz.RequisitionDetailInfoVO;
import com.fintech.insurance.micro.dto.biz.RequisitionDetailVO;
import com.fintech.insurance.micro.dto.biz.SimpleRequisitionDetailVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: (业务详情)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
public interface RequisitionDetailService {
    /**
     * 查看车辆详情
     *
     * @param contractId        合同id
     **/
    RequisitionDetailVO listRequisitionDetail(Integer contractId);


    /**
     * 查看车辆保单信息
     *
     * @param id 车辆id
     **/
    RequisitionDetailVO getRequisitionDetailById(Integer id);

    /**
     * 微信查看车辆保单信息
     *
     * @param id 车辆id
     **/
    RequisitionDetailInfoVO getWXRequisitionDetailInfoById(Integer id);

    /**
     * WX保存车辆信息
     * @param requisitionDetailVO 车辆信息
     * @return
     */
    Integer saveRequisitionDetail(RequisitionDetailInfoVO requisitionDetailVO);

    /**
     * WX获取车辆详情信息
     * @param id
     * @return
     */
    RequisitionDetailInfoVO getRequisitionDetailInfoById(Integer id);

    /**
     * 查询业务申请单车辆信息
     * @param requisitionId 业务单id
     * @param pageIndex 页数
     * @param pageSize  每页数
     * @return
     */
    Pagination<SimpleRequisitionDetailVO> findSimpleRequisitionDetailByRequisitionId(Integer requisitionId, Integer pageIndex, Integer pageSize);

    /**
     * 删除车辆信息
     * @param id
     */
    void deleteRequisitionDetailById(Integer id);

    //查询车辆是否在放款中
    RequisitionDetail findLoaningRequisitionDetailByCarNumber(String carNumber);

    //设置车辆信息的残值
    void setRequisitionDetailCommercialInsuranceValue(RequisitionDetail requisitionDetail);

}
