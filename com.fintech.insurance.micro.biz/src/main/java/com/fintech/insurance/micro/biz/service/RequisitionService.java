package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import com.fintech.insurance.micro.dto.biz.*;

import java.util.Date;
import java.util.List;

/**
 * @Description: (业务管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
public interface RequisitionService {

    /**
     * 再次申请接口
     * @param requisitionId
     * @return
     */
    WeChatRequisitionVO againApplyFor(Integer requisitionId);


    RequisitionVO getProductTypeAndRepayDayTypeByContractNumber(String contractNumber);
    /**
     * 支付订单 - 受理成功与否
     * @param requisitionId
     */
    Boolean payRequisition(Integer requisitionId);

    /**
     * 更改订单状态
     * @param requisitionId
     * @param requisitionStatus
     * @param sendWechatNotification
     */
    void changeRequisitionStatusByRequsitionId(Integer requisitionId, RequisitionStatus requisitionStatus, boolean sendWechatNotification);

    /**
     * 更改订单状态
     * @param paymentOrderNumber 支付单号
     * @param requisitionStatus
     */
    void changeRequisitionStatusByPaymentOrder(String paymentOrderNumber, RequisitionStatus requisitionStatus);

    /**
     * 微信端渠道详情
     * @param requisitionId
     * @return
     */
    WeChatRequisitionVO getWeChatRequisitionVO(Integer requisitionId);

    public RequisitionVO getRequisitionById(Integer id);

    /**
     * 查看业务详情
     *
     * @param requisitionNumber     订单编号
     */
    RequisitionVO getRequisitionVOByRequisitionNumber(String requisitionNumber);

    /**
     * 确认已支付
     *
     * @param requisitionVO  申请单信息
     */
    void confirmPaid(RequisitionVO requisitionVO, Integer currentLoginUserId, String remark);

    /**
     * 分期信息查询
     * @param id 订单id
     * @return
     */
    List<DurationVO> listDuration(Integer id);

    /**
     * 取消订单
     * @param requisitionVO      订单
     */
    void cancel(RequisitionVO requisitionVO, Integer currentLoginUserId);


    void save(RequisitionVO requisitionVO);

    /**
     * 提交申请单至用户确认
     * @param requisitionVO
     */
    /*void submitForCustomerConfirm(RequisitionVO requisitionVO);*/

    /**
     * 提交申请单至审核员审核
     * @param requisitionVO
     */
    void submitForAudit(RequisitionVO requisitionVO, Integer currentLoginUserId);

    /**
     * 计算整个申请单金额
     * @param requisition 申请单
     * @return 返回总申请金额
     */
    Integer calculateRequisition(Requisition requisition);

    /**
     * 计算单个申请单明细金额
     * 场景1：添加车辆时，只需设置好车辆相关的商业保险和车船税、交强险的原始信息，则通过调用此方法来自动计算单个车辆的商业保险残值以及可贷金额
     * 场景2：提交申请单时，计算整个申请单的金额
     * @param requisitionDetail
     */
    void calculateRequisitionDetail(RequisitionDetail requisitionDetail);

    /*List<Contract> generateContract(Requisition requisition);*/

    RequisitionVO getByNumber(String requisitionNumber);

    RequisitionVO getRequisitionVOById(Integer id);

    /**
     * 统计业务申请信息
     * @param id
     * @return
     */
    StatisticRequisitionVO statisticRequisitionInfoById(Integer id);

    //根据id获取业务单实体
    Requisition getRequisitionEntityById(Integer id);

    /**
     * 保存客户业务单
     * @param customerRequisitionVO
     * @return
     */
    Integer saveCustomerRequisition(CustomerRequisitionVO customerRequisitionVO);

    /**
     * 根据id获取客户业务申请
     * @param id
     * @return
     */
    CustomerRequisitionVO getCustomerRequisitionVOById(Integer id);

    /**
     * 选择期限后，重新根据现有的车辆信息生成合同对象以及还款计划对象,已有的合同和还款计划对象会被删除.
     * @param requisition
     * @param month
     */
    void createContractDataForRequsition(Requisition requisition, Integer month);

     /* * 获取申请资料详情，发送给微信端
     * @param requisitionId
     * @return
      */
    RequisitionInfoVO getWeChatRequistionDetailVO(Integer requisitionId);


    /**
     * 分页查询客户申请信息
     * @param customerIds
     * @param productType
     * @param requisitionStatus
     * @return
     */
    Pagination<RequisitionVO> pageRequisitionByCustomerId(String customerIds, String channelUserIds, String productType, String requisitionStatus, Integer pageIndex, Integer pageSize);


    //保存申请单
    void save(Requisition requisition);

    /**
     * 取消待客户确认的申请单
     */
    void cancelForUnconfirmed();

    /**
     * 取消待支付的申请单
     */
    void cancelForWaitingpayment();

    /**
     * 获取用户最近业务单
     * @param requisitionId 业务单id
     * @param customerId 用户id
     * @return
     */
    RequisitionVO getAcquiescenceChannelByRequisitionIdAndCustomerId(Integer requisitionId, Integer customerId);

    /**
     * 已过最大预期天数的订单(过滤掉人工处理的订单)，如果扣款失败， 变更状态为待退保
     */
    void changeStatusForOverdue();

    /**
     * 根据指定商业保单的开始时间和结束时间以及申请单的信息，计算商业保单的最大分期期数
     * @param beginDate 商业保单的开始时间
     * @param endDate 商业保单的结束时间
     * @param requisition
     * @return
     */
    int computeMaxAvaiableInstalmentNum(Date beginDate, Date endDate, Requisition requisition);

    Integer countRequisitionByStatus(String channelUserIds, Integer customerId, String requisitionStatus);

    /**
     * 客户端确认申请
     * @param id
     */
    void confirmApplyFor(Integer id);

    //更新业务单数据
    void updateRequisition(Requisition requisition);

    List<RequisitionDetailVO> listRequisitionDetailByRequisitionId(Integer requisitionId);

    //根据合同号查询业务详情
    RequisitionVO getRequisitionByContractNumber(String contractNumber);
}
