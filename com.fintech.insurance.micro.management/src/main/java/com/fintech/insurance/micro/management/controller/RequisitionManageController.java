package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.annotations.FinanceDuplicateSubmitDisable;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.biz.*;
import com.fintech.insurance.micro.feign.biz.RequisitionServiceFeign;
import com.fintech.insurance.micro.feign.system.EntityAuditLogServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 申请单管理
 * @Author: Yong Li
 * @Date: 2017/12/7 13:27
 */
@RestController
@RequestMapping(value = "/management/requisition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RequisitionManageController extends BaseFintechManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(RequisitionManageController.class);

    @Autowired
    private RequisitionServiceFeign requisitionServiceFeign;

    @Autowired
    private EntityAuditLogServiceFeign entityAuditLogServiceFeign;


    /**
     * 查看业务详情
     * @param id
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    FintechResponse<RequisitionDetailWrapVO> getRequisitionDetail(@NotNull @RequestParam(name = "id") Integer id) {
        FintechResponse<RequisitionDetailWrapVO> result = requisitionServiceFeign.getRequisitionDetail(id);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

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
                                                                @RequestParam(name = "submmitStartTime", required = false)String submmitStartTime,
                                                                @RequestParam(name = "submmitEndTime", required = false)String submmitEndTime,
                                                                @RequestParam(name = "customerName", defaultValue = "")String customerName,
                                                                @RequestParam(name = "pageIndex", defaultValue = "1")Integer pageIndex,
                                                                @RequestParam(name = "pageSize", defaultValue = "20")Integer pageSize) {
        FintechResponse<Pagination<RequisitionVO>> result = requisitionServiceFeign.queryRequisition(requisitionNumber, requisitionStatus,
                productType, channelName, DateCommonUtils.convertDateStringToStamp(submmitStartTime, false),
                DateCommonUtils.convertDateStringToStamp(submmitEndTime, true), customerName, pageIndex, pageSize);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 查看车辆详情
     * @param contractId 合同id
     **/
    @RequestMapping(value = "/requisition-detail", method = RequestMethod.GET)
    FintechResponse<RequisitionDetailVO> listRequisitionDetail(@NotNull @RequestParam(name = "contractId") Integer contractId) {
        FintechResponse<RequisitionDetailVO> result = requisitionServiceFeign.listRequisitionDetail(contractId);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 查看订单分期
     * @param id 申请单id
     **/
    @RequestMapping(value = "/list-duration", method = RequestMethod.GET)
    FintechResponse<List<DurationVO>> listDuration(@RequestParam(name = "id") Integer id) {
        FintechResponse<List<DurationVO>> result = requisitionServiceFeign.listDuration(id);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 查看车辆保单信息
     * @param id 车辆id
     **/
    @RequestMapping(value = "/insurance-detail", method = RequestMethod.GET)
    FintechResponse<RequisitionDetailVO> getInsuranceDetail(@NotNull @RequestParam(name = "id") Integer id) {
        FintechResponse<RequisitionDetailVO> result = requisitionServiceFeign.getInsuranceDetail(id);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 取消订单
     * @param vo
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> cancelRequisition(@RequestBody(required = false) IdVO vo) {
        FintechResponse<VoidPlaceHolder> response = requisitionServiceFeign.cancelRequisition(vo);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    /**
     * 确认已支付
     * @param operationrRecordVO
     */
    @RequestMapping(value = "/confirmpaid", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> confirmRequisitionPaid(@RequestBody OperationRecordVO operationrRecordVO) {
        FintechResponse<VoidPlaceHolder> response = requisitionServiceFeign.confirmRequisitionPaid(operationrRecordVO);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }


    /**
     * 扣款
     * @param vo
     */
    @RequestMapping(value = "/debit", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> debitRequisition(@RequestBody(required = true) IdVO vo) {
        FintechResponse<VoidPlaceHolder> response = requisitionServiceFeign.debitRequisition(vo);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    /**
     * 审核
     * @param operationrRecordVO
     */
    @RequestMapping(path = "/audit", method = RequestMethod.POST)
    @FinanceDuplicateSubmitDisable
    FintechResponse<VoidPlaceHolder> auditRequsition(@RequestBody OperationRecordVO operationrRecordVO) {
        FintechResponse<VoidPlaceHolder> response = entityAuditLogServiceFeign.auditRequsition(operationrRecordVO);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

}
