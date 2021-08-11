package com.fintech.insurance.micro.api.biz;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.biz.*;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequestMapping(value = "/biz/requisition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface RequisitionServiceAPI {

    /**
     * 再次申请接口
     * @param idVO
     * @return
     */
    @PostMapping(value = "/again-apply-for")
    FintechResponse<WeChatRequisitionVO> againApplyFor(@RequestBody @Validated IdVO idVO);

    @GetMapping(value = "/by-contract-number")
    FintechResponse<RequisitionVO> getProductTypeAndRepayDayTypeByContractNumber(@RequestParam("contractNumber") String contractNumber);

    /**
     * 支付接口
     * @param requisitionId
     */
    @GetMapping("/pay")
    FintechResponse<VoidPlaceHolder> payRequisition(@RequestParam("requisitionId") Integer requisitionId);

    /**
     * 更改申请单的状态
     * @param requisitionStatusVO
     */
    @PostMapping("/change-status")
    FintechResponse<VoidPlaceHolder> changeRequisitionStatus(@RequestBody @Validated RequisitionStatusVO requisitionStatusVO);

    /**
     * 更改申请单的状态 - 通过服务支付单来查找申请单
     * @param paymentOrderNum 服务支付单
     * @param requisitionStatus 申请状态
     */
    @PostMapping("/change-status-by-paymentnumber")
    FintechResponse<VoidPlaceHolder> changeRequisitionStatusByPaymentOrder(@RequestParam("paymentOrderNum") String paymentOrderNum,
                                                                           @RequestParam("requisitionStatus") RequisitionStatus requisitionStatus);

    @GetMapping("/wechat-detail")
    FintechResponse<WeChatRequisitionVO> getWeChatRequisitionVO(@RequestParam("requisitionId") Integer requisitionId);

    /**
     * 查看业务详情
     * @param id
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    FintechResponse<RequisitionDetailWrapVO> getRequisitionDetail(@RequestParam(name = "id") Integer id);

    /**
     * 分页查询业务信息
     * @param requisitionNumber
     * @param requisitionStatus
     * @param productType
     * @param channelName
     * @param submmitStartTime
     * @param submmitEndTime
     * @param customerName
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    FintechResponse<Pagination<RequisitionVO>> queryRequisition(@RequestParam(name = "requisitionNumber", defaultValue = "")String requisitionNumber,
                                                                @RequestParam(name = "requisitionStatus", defaultValue = "")String requisitionStatus,
                                                                @RequestParam(name = "productType", defaultValue = "")String productType,
                                                                @RequestParam(name = "channelName", defaultValue = "")String channelName,
                                                                @RequestParam(name = "submmitStartTime", required = false)Long submmitStartTime,
                                                                @RequestParam(name = "submmitEndTime", required = false)Long submmitEndTime,
                                                                @RequestParam(name = "customerName", defaultValue = "")String customerName,
                                                                @RequestParam(name = "pageIndex", defaultValue = "1")Integer pageIndex,
                                                                @RequestParam(name = "pageSize", defaultValue = "20")Integer pageSize);

    /**
     * 查看车辆详情
     * @param contractId 合同id
     **/
    @RequestMapping(value = "/requisition-detail", method = RequestMethod.GET)
    FintechResponse<RequisitionDetailVO> listRequisitionDetail(@NotNull @RequestParam(name = "contractId") Integer contractId);

    /**
     * 查看订单分期
     * @param id 申请单id
     **/
    @RequestMapping(value = "/list-duration", method = RequestMethod.GET)
     FintechResponse<List<DurationVO>> listDuration(@RequestParam(name = "id") Integer id);

    /**
     * 查看车辆保单信息
     * @param id 车辆id
     **/
    @RequestMapping(value = "/insurance-detail", method = RequestMethod.GET)
    FintechResponse<RequisitionDetailVO> getInsuranceDetail(@RequestParam(name = "id") Integer id);

    /**
     * 取消订单
     * @param vo
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> cancelRequisition(@RequestBody(required = false) IdVO vo);

    /**
     * 确认已支付
     * @param operationrRecordVO
     */
    @RequestMapping(value = "/confirmpaid", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> confirmRequisitionPaid(@RequestBody OperationRecordVO operationrRecordVO);


    /**
     * 扣款
     * @param vo
     */
    @RequestMapping(value = "/debit", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> debitRequisition(@RequestBody(required = true) IdVO vo);

    /**
     * 获取订单详情
     * @param id 订单id
     */
    @RequestMapping(value = "/get-requisition-by-id", method = RequestMethod.GET)
    FintechResponse<RequisitionVO> getRequisitionById(@RequestParam(name = "id") Integer id);

    /**
     * 更新订单信息(只做更新处理)
     * @param requisitionVO
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> saveRequisition(@RequestBody RequisitionVO requisitionVO);

    /**
     * 提交申请单至用户确认
     * @param vo
     */
    /*@PostMapping(value = "/submit-confirm")
    FintechResponse<VoidPlaceHolder> submitRequisitionForCustomerConfirm(@RequestBody IdVO vo);*/

    /**
     * 提交申请单至审核，分为用户直接提交或者用户同意渠道进件后提交
     * @param vo
     */
    @PostMapping(value = "/submit-audit")
    FintechResponse<VoidPlaceHolder> submitRequisitionForAudit(@RequestBody IdVO vo);

    /**
     * 保存订单信息
     * @param customerRequisitionVO
     */
    @RequestMapping(value = "/save-customer-requisition", method = RequestMethod.POST)
    FintechResponse<Integer> saveCustomerRequisition(@RequestBody CustomerRequisitionVO customerRequisitionVO);

    /**
     * 保存车辆信息
     * @param requisitionDetailVO 车辆申请
     */
    @PostMapping(value = "/save-requisition-detail")
    FintechResponse<Integer> saveRequisitionDetail(@RequestBody RequisitionDetailInfoVO requisitionDetailVO);



    /**
     * 删除车辆信息
     * @param idVO 主键
     */
    @PostMapping(value = "/delete-requisition-detail")
    FintechResponse<VoidPlaceHolder> deleteRequisitionDetailById(@Validated @RequestBody IdVO idVO);


    /**
     * 查询业务单信息
     * @param id 业务单主键
     */
    @GetMapping(value = "/statistic_requisition_info/get")
    FintechResponse<StatisticRequisitionVO> statisticRequisitionInfoById(@RequestParam(value = "id") @NotNull Integer id);

    /**
     * WX获取车辆信息
     * @param id 主键id
     */
    @GetMapping(value = "/get-requisition-detail-info")
    FintechResponse<RequisitionDetailInfoVO> getRequisitionDetailInfoById(@RequestParam(value = "id") @NotNull Integer id);

    @PostMapping(value = "/get-by-number")
    FintechResponse<RequisitionVO> getRequisitionByNumber(@RequestParam(name = "requisitionNumber") String requisitionNumber);

    /**
     * 获取客户订单详情
     * @param id 订单id
     */
    @RequestMapping(value = "/get-customer-requisition-by-id", method = RequestMethod.GET)
    FintechResponse<CustomerRequisitionVO> getCustomerRequisitionVOById(@RequestParam(name = "id") Integer id);

    /* * 获取申请资料详情
     * @param requisitionId
     * @return
     */
    @GetMapping(value = "/get-wechat-requsition-detail")
    FintechResponse<RequisitionInfoVO> getWeChatRequistionDetailVO(@RequestParam(value = "requisitionId") Integer requisitionId);

    /**
     * WX客户端-查询客户所有申请单信息
     */
    @GetMapping(value = "/page-requisition-by-customerid")
    FintechResponse<Pagination<RequisitionVO>> pageRequisitionByCustomerIdOrChannelUserId(@RequestParam(name = "customerIds", defaultValue = "")String customerIds,
                                                                                          @RequestParam(name = "channelUserIds", defaultValue = "")String channelUserIds,
                                                                                          @RequestParam(name = "productType") String productType,
                                                                                          @RequestParam(name = "requisitionStatus") String requisitionStatus,
                                                                                          @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                                          @RequestParam(name = "pageSize", defaultValue = "20")Integer pageSize);

    /**
     * 查询客户申请单车辆信息
     */
    @RequestMapping(value = "/query-requisition-detail/page", method = RequestMethod.GET)
    FintechResponse<Pagination<SimpleRequisitionDetailVO>> pageRequisitionDetailByRequisitionId(@RequestParam(name = "requisitionId", required = true )  Integer requisitionId,
                                                                                                @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                                                @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize);

    /**
     * 取消待客户确认的申请单
     * @return
     */
    @RequestMapping(value = "/cancel-for-unconfirmed", method = RequestMethod.GET)
    FintechResponse<VoidPlaceHolder> cancelForUnconfirmed();

    /**
     * 取消待支付的申请单
     * @return
     */
    @RequestMapping(value = "/cancel-for-waitingpayment", method = RequestMethod.GET)
    FintechResponse<VoidPlaceHolder> cancelForWaitingpayment();

    /**
     * 统计申请单状态数量
     * @return
     */
    @RequestMapping(value = "/count-requisition-by-status", method = RequestMethod.GET)
    FintechResponse<Integer> countRequisitionByStatus(@RequestParam(name = "channelUserIds", required = false) String channelUserIds,
                                                      @RequestParam(name = "customerId", required = false) Integer customerId,
                                                      @RequestParam(name = "requisitionStatus", required = false) String requisitionStatus);

    /**
     * 客户端确认申请
     * @param id
     */
    @RequestMapping(value = "/confirm-apply-for", method = RequestMethod.GET)
    FintechResponse<VoidPlaceHolder> confirmApplyFor(@RequestParam(name = "id", required = false ) Integer id,
                                                     @RequestParam(name = "currentLoginUserId", required = false) Integer currentLoginUserId);

    @RequestMapping(value = "/list-requisitiondetail-by-requisitionid", method = RequestMethod.GET)
    FintechResponse<List<RequisitionDetailVO>> listRequisitionDetailByRequisitionId(@RequestParam(name = "requisitionId") Integer requisitionId);

    /**
     * 已过最大预期天数的订单(过滤掉人工处理的订单)，如果扣款失败， 变更状态为待退保
     * @return
     */
    //FintechResponse<VoidPlaceHolder> changeStatusForOverdue();

    /**
     * 通过合同号查询业务单详情
     */
    @GetMapping(value = "/page-requisition-by-contract-number")
    FintechResponse<RequisitionVO> getRequisitionByContractNumber(@RequestParam(name = "contractNumber", defaultValue = "") String contractNumber);
}
