package com.fintech.insurance.micro.biz.controller;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.*;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.cache.RedisSequenceFactory;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.biz.RequisitionServiceAPI;
import com.fintech.insurance.micro.biz.persist.dao.RequisitionDao;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import com.fintech.insurance.micro.biz.service.ChannelService;
import com.fintech.insurance.micro.biz.service.ContractService;
import com.fintech.insurance.micro.biz.service.RequisitionDetailService;
import com.fintech.insurance.micro.biz.service.RequisitionService;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.biz.*;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.finance.EnterpriseBankVO;
import com.fintech.insurance.micro.dto.finance.PaymentOrderVO;
import com.fintech.insurance.micro.dto.finance.VoucherVO;
import com.fintech.insurance.micro.dto.support.BranchVO;
import com.fintech.insurance.micro.dto.system.EntityAuditLogVO;
import com.fintech.insurance.micro.dto.system.QueryAuditLogVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendParamVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendResultVO;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.finance.EnterpriseBankServiceFeign;
import com.fintech.insurance.micro.feign.finance.PaymentOrderServiceFeign;
import com.fintech.insurance.micro.feign.finance.RepaymentPlanServiceFeign;
import com.fintech.insurance.micro.feign.retrieval.BizQueryFeign;
import com.fintech.insurance.micro.feign.support.InsuranceCompanyConfigServiceFeign;
import com.fintech.insurance.micro.feign.system.EntityAuditLogServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.sms.SMSServiceFeign;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @Author: qxy
 * @Description: 申请单管理
 * @Date: 2017/11/17 18:19
 */
@RestController
public class RequisitionController extends BaseFintechController implements RequisitionServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(RequisitionController.class);

    @Autowired
    private RequisitionService requisitionService;

    @Autowired
    private CustomerServiceFeign customerServiceFeign;


    @Autowired
    private EntityAuditLogServiceFeign entityAuditLogServiceFeign;

    @Autowired
    private InsuranceCompanyConfigServiceFeign insuranceCompanyConfigServiceFeign;

    @Autowired
    private PaymentOrderServiceFeign paymentOrderServiceFeign;

    @Autowired
    private BizQueryFeign bizQueryFeign;

    @Autowired
    private RequisitionDetailService  requisitionDetailService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    RepaymentPlanServiceFeign repaymentPlanServiceFeign;

    @Autowired
    private EnterpriseBankServiceFeign enterpriseBankServiceFeign;

    @Autowired
    private SMSServiceFeign smsServiceFeign;

    @Autowired
    private RequisitionDao requisitionDao;

    @Autowired
    private ContractService contractService;

    @Autowired
    private RedisSequenceFactory redisSequenceFactory;

    //查询客户是否被冻结
    private void checkoutCustomerIsLocked(Integer customerId, String channelCode){
        if(customerId == null){
            LOG.error("checkoutCustomerIsLocked failed with null customerId");
            throw new FInsuranceBaseException(107031);
        }
        FintechResponse<Boolean> fintechResponse = customerServiceFeign.getCustomerLockedStatusById(customerId);
        if(!fintechResponse.isOk()){
            LOG.error("checkoutCustomerIsLocked failed with error code=["+ fintechResponse.getCode() +"]" );
            throw new FInsuranceBaseException(fintechResponse.getCode());
        }
        if(fintechResponse.getData() == null || fintechResponse.getData()){
            LOG.error("checkoutCustomerIsLocked failed with error customerId=["+ customerId +"]" );
            throw new FInsuranceBaseException(107032);
        }

        if(StringUtils.isNoneBlank(channelCode)){
          ChannelVO channelVO = channelService.getChannelDetailByChannelCode(channelCode);
          if(channelVO == null){
              LOG.error("checkoutCustomerIsLocked failed  null ChannelVO with  channelCode=["+ channelCode +"]" );
              throw new FInsuranceBaseException(107039);
          }
          if(channelVO.getIsLocked().equals(1)) {
              LOG.error("checkoutCustomerIsLocked failed ChannelVO is locked with  channelCode=["+ channelCode +"]" );
              throw new FInsuranceBaseException(107044);
          }
        }
    }

    @Override
    public FintechResponse<WeChatRequisitionVO> againApplyFor(@RequestBody @Validated IdVO idVO) {
        return FintechResponse.responseData(requisitionService.againApplyFor(idVO.getId()));
    }

    @Override
    public FintechResponse<RequisitionVO> getProductTypeAndRepayDayTypeByContractNumber(String contractNumber) {
        return FintechResponse.responseData(requisitionService.getProductTypeAndRepayDayTypeByContractNumber(contractNumber));
    }

    @Override
    public FintechResponse<VoidPlaceHolder> payRequisition(Integer requisitionId) {
        // 支付订单
        Boolean isPayInProcessing = requisitionService.payRequisition(requisitionId);
        if (!isPayInProcessing) {
            throw new FInsuranceBaseException(104537);
        }
/*        else {
            Requisition requisition = requisitionDao.getById(requisitionId);
            this.sendSMSAndWXNotification(requisition);
        }*/

        LOG.info("正在支付订单");
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    public void sendSMSAndWXNotification(Requisition requisition){
        SMSSendParamVO smsSendParamVO = new SMSSendParamVO();
        FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getByCustomerId(requisition.getCustomerAccountInfoId());
        if (!customerVOFintechResponse.isOk() || customerVOFintechResponse.getData() == null) {
            throw new FInsuranceBaseException(103007);
        }
        smsSendParamVO.setEvent(NotificationEvent.DEFAULT_WELCOME);
        smsSendParamVO.setPhoneNumbers(new String[] {customerVOFintechResponse.getData().getPhone()});
        Map<String,String> smsParamsMap = new HashMap<String, String>();
        smsParamsMap.put("customer","【没有支付模板" + requisition.getRequisitionNumber() + "】");
        smsSendParamVO.setSmsParams(smsParamsMap);
        FintechResponse<SMSSendResultVO> smsSendResultVOFintechResponse = smsServiceFeign.sendSMS(smsSendParamVO);
        if (!smsSendResultVOFintechResponse.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(smsSendResultVOFintechResponse);
        }
    }

    public FintechResponse<VoidPlaceHolder> changeRequisitionStatus(@RequestBody @Validated RequisitionStatusVO requisitionStatusVO) {
        RequisitionStatus requisitionStatus = null;
        if (StringUtils.isNotBlank(requisitionStatusVO.getRequisitionStatus())) {
            try {
                requisitionStatus = RequisitionStatus.codeOf(requisitionStatusVO.getRequisitionStatus());
            } catch (Exception e) {
                throw new FInsuranceBaseException(103001, new Object[]{requisitionStatusVO.getRequisitionStatus(), RequisitionStatus.class.getSimpleName()});
            }
        }
        requisitionService.changeRequisitionStatusByRequsitionId(requisitionStatusVO.getRequisitionId(), requisitionStatus, true);
        return FintechResponse.voidReturnInstance();
    }

    @Override
    public FintechResponse<VoidPlaceHolder> changeRequisitionStatusByPaymentOrder(@RequestParam("paymentOrderNum") String paymentOrderNum,
                                                                                  @RequestParam("requisitionStatus") RequisitionStatus requisitionStatus) {
        requisitionService.changeRequisitionStatusByPaymentOrder(paymentOrderNum, requisitionStatus);
        return FintechResponse.voidReturnInstance();
    }

    @Override
    public FintechResponse<WeChatRequisitionVO> getWeChatRequisitionVO(Integer requisitionId) {
        return FintechResponse.responseData(requisitionService.getWeChatRequisitionVO(requisitionId));
    }

    @Override
    public FintechResponse<RequisitionDetailWrapVO> getRequisitionDetail(@RequestParam(name = "id") Integer id) {
        if (null == id) {
            LOG.debug("null id");
            throw new FInsuranceBaseException(101509);
        }
        RequisitionDetailWrapVO wrapVO = new RequisitionDetailWrapVO();
        RequisitionVO requisitionVO = requisitionService.getRequisitionVOById(id);
        if (null != requisitionVO) {
            wrapVO.setRequisitionVO(requisitionVO);//业务订单基本信息

            FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getByCustomerId(requisitionVO.getCustomerAccountInfoId());//客户信息
            CustomerVO customerVO = customerVOFintechResponse == null ? null : customerVOFintechResponse.getData();
            if(customerVO == null){
                wrapVO.setCustomerVO(new CustomerVO());
            }else {
                // CustomerLoanBankVO customerLoanBankVO = insuranceCompanyConfigServiceFeign.getByAccountNumberAndType(requisitionVO.getLoanAccountNumber(), requisitionVO.getLoanAccountType());//保险公司信息
                if (null != customerVO.getBankName()) {
                    FintechResponse<EnterpriseBankVO> enterpriseBankVOFintechResponse = enterpriseBankServiceFeign.getEnterpriseBank(ApplicationType.VERIFY.getCode(), customerVO.getBankName());
                    if (!enterpriseBankVOFintechResponse.isOk()) {
                        throw FInsuranceBaseException.buildFromErrorResponse(enterpriseBankVOFintechResponse);
                    }
                    if (null != enterpriseBankVOFintechResponse.getData()) {
                        customerVO.setBankName(enterpriseBankVOFintechResponse.getData().getBankName());
                    }
                }
                wrapVO.setCustomerVO(customerVO);

                wrapVO.setBranchVO(this.convertToBranchVO(requisitionVO));

            }
        }

        List<DurationVO> durationVOList = requisitionService.listDuration(id);
        wrapVO.setDurationVOList(durationVOList);
        return FintechResponse.responseData(wrapVO);
    }

    private BranchVO convertToBranchVO(RequisitionVO requisitionVO) {
        BranchVO branchVO = new BranchVO();
        branchVO.setAccountBank(requisitionVO.getLoanAccountBank());
        branchVO.setAccountBranch(requisitionVO.getLoanAccountBankBranch());
        // branchVO.setId(customerLoanBankVO.getId());
        branchVO.setCompanyName(requisitionVO.getInsuranceCompanyName());
        branchVO.setAccountName(requisitionVO.getLoanAccountName());
        branchVO.setBranchName(requisitionVO.getInsuranceBranchName());
        branchVO.setAccountNum(requisitionVO.getLoanAccountNumber());
        return branchVO;
    }

    @Override
    public FintechResponse<List<DurationVO>> listDuration(@RequestParam(name = "id") Integer id) {
        return FintechResponse.responseData(requisitionService.listDuration(id));
    }

    @Override
    public FintechResponse<RequisitionDetailVO> listRequisitionDetail(@NotNull @RequestParam(name = "contractId") Integer contractId) {
        RequisitionDetailVO requisitionDetailVO = requisitionDetailService.listRequisitionDetail(contractId);
        return FintechResponse.responseData(requisitionDetailVO);
    }


    @Override
    public FintechResponse<RequisitionDetailVO> getInsuranceDetail(@NotNull @RequestParam(name = "id") Integer id) {
        RequisitionDetailVO requisitionDetailVO = requisitionDetailService.getRequisitionDetailById(id);
        return FintechResponse.responseData(requisitionDetailVO);
    }


    @Override
    public FintechResponse<Pagination<RequisitionVO>> queryRequisition(@RequestParam(name = "requisitionNumber", defaultValue = "")String requisitionNumber,
                                                      @RequestParam(name = "requisitionStatus", defaultValue = "")String requisitionStatus,
                                                      @RequestParam(name = "productType", defaultValue = "")String productType,
                                                      @RequestParam(name = "channelName", defaultValue = "")String channelName,
                                                      @RequestParam(name = "submmitStartTime", required = false)Long submmitStartTime,
                                                      @RequestParam(name = "submmitEndTime", required = false)Long submmitEndTime,
                                                      @RequestParam(name = "customerName", defaultValue = "")String customerName,
                                                      @RequestParam(name = "pageIndex", defaultValue = "1")Integer pageIndex,
                                                      @RequestParam(name = "pageSize", defaultValue = "20")Integer pageSize) {
        //当前登录用户id
        Integer currentLoginUserId = getCurrentLoginUserId();
        Pagination<RequisitionVO> requisitionVOPagination = bizQueryFeign.queryRequisition(requisitionNumber,
                requisitionStatus, productType, channelName, submmitStartTime, submmitEndTime, customerName, pageIndex, pageSize).getData();
        List<RequisitionVO> requisitionList = null;
        Map<Integer, RequisitionVO> requisitionVOMap = new LinkedHashMap<>();
        if (null != requisitionVOPagination && requisitionVOPagination.getItems().size() > 0) {
            requisitionList = requisitionVOPagination.getItems();
            List<Integer> requisitionIdList = new ArrayList<>();
            List<String> requisitionBatchList = new ArrayList<>();
            for (RequisitionVO vo : requisitionList) {
                requisitionVOMap.put(vo.getId(), vo);
                requisitionIdList.add(vo.getId());
                if (null != vo.getLatestAuditBatch()) {
                    requisitionBatchList.add(vo.getLatestAuditBatch());
                }
            }
            //查找所有待当前登录用户审核的申请单审核记录
            QueryAuditLogVO queryAuditLogVO = new QueryAuditLogVO();//查询参数封装
            queryAuditLogVO.setEntityType(EntityType.REQUISITION.getCode());
            queryAuditLogVO.setRequisitionIdList(requisitionIdList);
            queryAuditLogVO.setCurrentLoginUserId(currentLoginUserId);
            queryAuditLogVO.setAuditStatus(AuditStatus.PENDING.getCode());
            queryAuditLogVO.setRequisitionBatchList(requisitionBatchList);
            FintechResponse<List<EntityAuditLogVO>> entityAuditLogVOS = entityAuditLogServiceFeign.listAuditLogsByCurrentUserAnd(queryAuditLogVO);
            if (!entityAuditLogVOS.isOk()) {
                throw FInsuranceBaseException.buildFromErrorResponse(entityAuditLogVOS);
            }
            if (null != entityAuditLogVOS.getData() && entityAuditLogVOS.getData().size() > 0) {
                for (Map.Entry<Integer, RequisitionVO> entry : requisitionVOMap.entrySet()) {//循环查询出的申请单是否在当前用户的待审核记录中，存在则可审核
                    for (EntityAuditLogVO e : entityAuditLogVOS.getData()) {
                        if (entry.getKey().equals(e.getEntityId())) {
                            entry.getValue().setCanAudit(0);//将申请单是否可以审核字段置0，表示可审，默认为1，不可审
                            break;
                        }
                    }
                }
            }
        }
        List<RequisitionVO> result = null;
        if (requisitionList == null || requisitionList.size() < 1) {
            result =  Collections.emptyList();
        } else {
            result = new ArrayList<>(requisitionVOMap.values());
        }

        Pagination<RequisitionVO> newPage = Pagination.createInstance(pageIndex, pageSize, requisitionVOPagination != null ? requisitionVOPagination.getTotalRowsCount() : 0, result);
        return FintechResponse.responseData(newPage);
    }

    @Override
    public FintechResponse<VoidPlaceHolder> cancelRequisition(@RequestBody(required = false) IdVO vo) {
        //当前登录用户id
        Integer currentLoginUserId = getCurrentLoginUserId();

        if (null == vo || null == vo.getId()) {
            throw new FInsuranceBaseException(104107);
        }
        RequisitionVO requisitionVO = requisitionService.getRequisitionById(vo.getId());
        if (null == requisitionVO) {
            throw new FInsuranceBaseException(104105);
        }
        FintechResponse<PaymentOrderVO> paymentOrderResponse = paymentOrderServiceFeign.getByOrderNumber(requisitionVO.getPaymentOrderNumber());
        if (!paymentOrderResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(paymentOrderResponse);
        }
        /*//判断申请单状态，支付失败状态才允许取消
        if (!RequisitionStatus.FailedPayment.getCode().equals(requisitionVO.getRequisitionStatus())) {
            LOG.error("the currentRequisition with id[" + requisitionVO.getId() + "] who's status is " + requisitionVO.getRequisitionStatus() + "is not allowed to be canceled");
            throw new FintechControllerException(104517);
        }*/
        //判断支付单是否是人工处理状态，否则不允许取消操作
        if (null != paymentOrderResponse.getData() && !paymentOrderResponse.getData().getManualFlag()) {
            throw new FInsuranceBaseException(104531);
        }

        //验证订单是否已取消
        if (RequisitionStatus.Canceled.getCode().equals(requisitionVO.getRequisitionStatus())) {
            LOG.error("the currentRequisition with id[" + requisitionVO.getId() + "] has already been canceled ");
            throw new FInsuranceBaseException(104532);
        }
        requisitionService.cancel(requisitionVO, currentLoginUserId);//订单状态变为已取消
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }


    @Override
    public FintechResponse<VoidPlaceHolder> confirmRequisitionPaid(@RequestBody OperationRecordVO operationrRecordVO) {

        //当前登录用户id
        Integer currentLoginUserId = getCurrentLoginUserId();

        RequisitionVO requisitionVO = requisitionService.getRequisitionById(operationrRecordVO.getId());
        //验证订单信息是否存在
        if (null == requisitionVO) {
            throw new FInsuranceBaseException(104505);
        }

        FintechResponse<PaymentOrderVO> paymentOrderResponse = paymentOrderServiceFeign.getByOrderNumber(requisitionVO.getPaymentOrderNumber());
        if (!paymentOrderResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(paymentOrderResponse);
        }

        PaymentOrderVO paymentOrderVO = paymentOrderResponse.getData();

        //支付成功的订单无须再次确认
        if (DebtStatus.CONFIRMED.getCode().equals(paymentOrderVO.getPaymentStatus()) && RequisitionStatus.WaitingLoan.getCode().equals(requisitionVO.getRequisitionStatus())) {
            throw new FInsuranceBaseException(104510);
        }
        //验证当前订单信息的状态,支付失败状态才允许确认操作
        if (!RequisitionStatus.FailedPayment.getCode().equals(requisitionVO.getRequisitionStatus())) {
            throw new FInsuranceBaseException(104519);
        }

        VoucherVO voucherVO = new VoucherVO();//支付凭证信息
        voucherVO.setRequisitionCode(requisitionVO.getRequisitionNumber());
        voucherVO.setPaymentOrderId(paymentOrderVO.getId());
        voucherVO.setRemark(operationrRecordVO.getOperationRemark());
        voucherVO.setVoucher(JSON.toJSONString(operationrRecordVO.getImageKeys()));
        voucherVO.setUserId(currentLoginUserId);
        voucherVO.setAccountType(AccountType.SERVICEFEE.getCode());
        voucherVO.setRequisitionId(requisitionVO.getId());
        voucherVO.setAccountAmount(paymentOrderVO.getTotalAmount());
        voucherVO.setTransactionSerial(redisSequenceFactory.generateSerialNumber(BizCategory.VH));
        paymentOrderServiceFeign.saveVoucher(voucherVO);//保存支付凭证

        //支付单状态变为支付成功
        paymentOrderServiceFeign.changePaymentOrderStatus(requisitionVO.getPaymentOrderNumber(), "", DebtStatus.CONFIRMED);

        requisitionService.confirmPaid(requisitionVO, currentLoginUserId, operationrRecordVO.getOperationRemark());//确认支付，订单状态变为待放款

        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }




    @Override
    public FintechResponse<VoidPlaceHolder> debitRequisition(@RequestBody(required = true) IdVO vo) {
        //检查请求参数
        if (null == vo.getId()) {
            throw new FInsuranceBaseException(102001);
        }
        RequisitionVO requisition = this.requisitionService.getRequisitionById(vo.getId());
        if (null == requisition) {
            throw new FInsuranceBaseException(104105);
        }

        //检查该申请单是否为支付失败状态
        if (!RequisitionStatus.FailedPayment.getCode().equals(requisition.getRequisitionStatus())) {
            throw new FInsuranceBaseException(104533);
        }

        //检查该申请单是否已经发起过扣款， 但是尚未处理完成
        if (null == requisition.getPaymentOrderNumber()) {
            throw new FInsuranceBaseException(105019);
        }
        FintechResponse<PaymentOrderVO> paymentOrderResp = paymentOrderServiceFeign.getByOrderNumber(requisition.getPaymentOrderNumber());
        if (!paymentOrderResp.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(paymentOrderResp);
        }
        PaymentOrderVO paymentOrderVO = paymentOrderResp.getData();
        if (null == paymentOrderVO) {
            throw new FInsuranceBaseException(105009);
        }
        if (!DebtStatus.FAILED.getCode().equals(paymentOrderVO.getPaymentStatus())) {
            throw new FInsuranceBaseException(104529); // 上笔扣款已经成功或处理中状态的不能重复扣款
        }

        Boolean isPayInProcessing = requisitionService.payRequisition(vo.getId());
        if (!isPayInProcessing) {
            throw new FInsuranceBaseException(104537);
        }

        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<RequisitionVO> getRequisitionById(Integer id) {
        return FintechResponse.responseData(requisitionService.getRequisitionById(id));
    }

    @Override
    public FintechResponse<VoidPlaceHolder> saveRequisition(@RequestBody RequisitionVO requisitionVO) {
        requisitionService.save(requisitionVO);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    /*@Override
    public FintechResponse<VoidPlaceHolder> submitRequisitionForCustomerConfirm(@RequestBody IdVO vo) {
        RequisitionVO requisition = this.requisitionService.getRequisitionById(vo.getId());
        //检查requisition是否存在
        if (requisition == null) {
            throw new FInsuranceBaseException(104512);
        }
        //检查该申请单是否为该渠道用户创建的，否则不能提交
        Integer currentUserId = getCurrentLoginUserId();
        if (currentUserId == null || requisition.getChannelUserId() == null || currentUserId.intValue() != requisition.getChannelUserId().intValue()) {
            throw new FInsuranceBaseException(104513);//不能提交他人的数据
        }
        //检查申请单的状态是否为待提交或者已退回状态
        if (!RequisitionStatus.Draft.getCode().equalsIgnoreCase(requisition.getRequisitionStatus())
                && !RequisitionStatus.Rejected.getCode().equalsIgnoreCase(requisition.getRequisitionStatus())) {
            throw new FInsuranceBaseException(104514);
        }
        //提交申请单
        this.requisitionService.submitForCustomerConfirm(requisition);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }*/

    public FintechResponse<VoidPlaceHolder> submitRequisitionForAudit(@RequestBody IdVO vo) {
        RequisitionVO requisition = this.requisitionService.getRequisitionById(vo.getId());
        //检查requisition是否存在
        if (requisition == null) {
            throw new FInsuranceBaseException(104512);
        }
        //检查该申请单是否为用户的申请单
        Integer currentUserId = getCurrentLoginUserId();
        if (currentUserId == null || requisition.getCustomerId() == null || currentUserId.intValue() != requisition.getCustomerId().intValue()) {
            throw new FInsuranceBaseException(104513);//不能提交他人的数据
        }
        //如果是渠道创建的单据，则状态必须为已提交状态
        if (!RequisitionStatus.Submitted.getCode().equalsIgnoreCase(requisition.getRequisitionStatus())) {
            throw new FInsuranceBaseException(104514);
        }
        //提交申请单
        this.requisitionService.submitForAudit(requisition, getCurrentLoginUserId());
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<RequisitionVO> getRequisitionByNumber(@NotNull String requisitionNumber) {
        return FintechResponse.responseData(requisitionService.getByNumber(requisitionNumber));
    }

    @Override
    public FintechResponse<CustomerRequisitionVO> getCustomerRequisitionVOById(@NotNull Integer id) {
        CustomerRequisitionVO vo = requisitionService.getCustomerRequisitionVOById(id);
        return FintechResponse.responseData(vo);
    }

    @Override
    public FintechResponse<RequisitionInfoVO> getWeChatRequistionDetailVO(@NotNull Integer requisitionId) {
        return FintechResponse.responseData(requisitionService.getWeChatRequistionDetailVO(requisitionId));
    }

    @Override
    public FintechResponse<Pagination<RequisitionVO>> pageRequisitionByCustomerIdOrChannelUserId(@RequestParam(name = "customerIds", defaultValue = "")String customerIds,
                                                                                                 @RequestParam(name = "channelUserIds", defaultValue = "")String channelUserIds,
                                                                                                 @RequestParam(name = "productType") String productType,
                                                                                                 @RequestParam(name = "requisitionStatus") String requisitionStatus,
                                                                                                 @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                                                 @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        return FintechResponse.responseData(requisitionService.pageRequisitionByCustomerId(customerIds, channelUserIds, productType, requisitionStatus, pageIndex, pageSize));
    }

    @Override
    public FintechResponse<Pagination<SimpleRequisitionDetailVO>> pageRequisitionDetailByRequisitionId(@RequestParam(name = "requisitionId") Integer requisitionId,
                                                                                                       @RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                                                       @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        Pagination<SimpleRequisitionDetailVO> page = requisitionDetailService.findSimpleRequisitionDetailByRequisitionId( requisitionId,  pageIndex,  pageSize);
        return FintechResponse.responseData(page);
    }

    @Override
    public FintechResponse<VoidPlaceHolder> cancelForUnconfirmed() {
        requisitionService.cancelForUnconfirmed();
        return FintechResponse.voidReturnInstance();
    }

    @Override
    public FintechResponse<VoidPlaceHolder> cancelForWaitingpayment() {
        requisitionService.cancelForWaitingpayment();
        return FintechResponse.voidReturnInstance();
    }

    @Override
    public FintechResponse<Integer> saveCustomerRequisition(@RequestBody CustomerRequisitionVO customerRequisitionVO) {
        this.checkoutCustomerIsLocked(customerRequisitionVO.getCustomerId(), customerRequisitionVO.getChannelCode());
        Integer id = requisitionService.saveCustomerRequisition(customerRequisitionVO);
        return FintechResponse.responseData(id);
    }

    @Override
    public FintechResponse<Integer> saveRequisitionDetail(@RequestBody RequisitionDetailInfoVO requisitionDetailVO) {

        //日期校验
        Date commercialInsuranceStart = requisitionDetailVO.getCommercialInsuranceStart();
        Date endDate = DateUtils.addDays(DateUtils.addYears(commercialInsuranceStart, 1) , -1);
        if(endDate.before(requisitionDetailVO.getCommercialInsuranceEnd())){
            throw new FInsuranceBaseException(104934);
        }

        //结束时间必须大于开始时间，结束时间不能大于当前时间
        if(endDate.before(commercialInsuranceStart) || endDate.before(DateCommonUtils.getToday())) {
            throw new FInsuranceBaseException(104928);
        }
        //查询车辆是否存在贷款中的情况
        RequisitionDetail loaningRequisitionDetail = null;
        if(StringUtils.isNoneBlank(requisitionDetailVO.getCarNumber())) {
            loaningRequisitionDetail =  requisitionDetailService.findLoaningRequisitionDetailByCarNumber(requisitionDetailVO.getCarNumber());
        }
        if(loaningRequisitionDetail != null ){
            throw new FInsuranceBaseException(104932);
        }
        Integer id = requisitionDetailService.saveRequisitionDetail(requisitionDetailVO);
        return FintechResponse.responseData(id);
    }

    @Override
    public FintechResponse<VoidPlaceHolder> deleteRequisitionDetailById(@Validated @RequestBody IdVO idVO) {
        LOG.info("prepare to delete requsition detail for id:" + idVO.getId());
        requisitionDetailService.deleteRequisitionDetailById(idVO.getId());
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }


    @Override
    public FintechResponse<StatisticRequisitionVO> statisticRequisitionInfoById(@RequestParam(value = "id") @NotNull Integer id) {
        StatisticRequisitionVO statisticRequisitionVO = requisitionService.statisticRequisitionInfoById(id);
        return FintechResponse.responseData(statisticRequisitionVO);
    }

    @Override
    public FintechResponse<RequisitionDetailInfoVO> getRequisitionDetailInfoById(@RequestParam(value = "id")  @NotNull Integer id) {
        RequisitionDetailInfoVO vo = requisitionDetailService.getRequisitionDetailInfoById(id);
        return FintechResponse.responseData(vo);
    }

    @Override
    public FintechResponse<Integer> countRequisitionByStatus(@RequestParam(value = "channelUserIds", required = false) String channelUserIds,
                                                              @RequestParam(value = "customerId", required = false) Integer customerId,
                                                              @RequestParam(value = "requisitionStatus", required = false) String requisitionStatus) {
        return FintechResponse.responseData(requisitionService.countRequisitionByStatus(channelUserIds, customerId, requisitionStatus));
    }

    @Override
    public FintechResponse<VoidPlaceHolder> confirmApplyFor(@RequestParam(value = "id") Integer id,@RequestParam(value = "currentLoginUserId", required = false)  Integer currentLoginUserId) {

        RequisitionVO requisitionVO = requisitionService.getRequisitionById(id);
        requisitionService.submitForAudit(requisitionVO, null == currentLoginUserId ? 0 : currentLoginUserId);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<List<RequisitionDetailVO>> listRequisitionDetailByRequisitionId(@RequestParam(name = "requisitionId") Integer requisitionId) {
        return FintechResponse.responseData(requisitionService.listRequisitionDetailByRequisitionId(requisitionId));
    }

    @Override
    public FintechResponse<RequisitionVO> getRequisitionByContractNumber(@RequestParam(name = "contractNumber") String contractNumber) {
        return FintechResponse.responseData(requisitionService.getRequisitionByContractNumber(contractNumber));
    }
}
