package com.fintech.insurance.micro.customer.webchat.controller;

import com.fintech.insurance.commons.annotations.FinanceDuplicateSubmitDisable;
import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.*;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.biz.*;
import com.fintech.insurance.micro.dto.biz.ProductVO;
import com.fintech.insurance.micro.dto.customer.CustomerBankCardVO;
import com.fintech.insurance.micro.dto.customer.CustomerLoanBankVO;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.finance.PaymentOrderVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.feign.biz.ContractServiceFeign;
import com.fintech.insurance.micro.feign.biz.ProductBusinessServiceFegin;
import com.fintech.insurance.micro.feign.biz.RequisitionServiceFeign;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.finance.PaymentOrderServiceFeign;
import com.fintech.insurance.micro.feign.finance.RepaymentPlanServiceFeign;
import com.fintech.insurance.micro.feign.support.InsuranceCompanyConfigServiceFeign;
import com.fintech.insurance.micro.feign.system.EntityAuditLogServiceFeign;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
import com.fintech.insurance.micro.vo.wechat.*;
import com.fintech.insurance.service.agg.CommonRequisitionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @Description: 微信客户端外部接口门面
 * @Author: Yong Li
 * @Date: 2017/12/6 18:55
 */
@RestController
@RequestMapping(value = "/wechat/customer/requisition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequireWechatLogin
public class RequisitionController extends BaseFintechWechatController {


    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    @Autowired
    private RequisitionServiceFeign requisitionServiceFeign;

    @Autowired
    private CommonRequisitionService commonRequisitionService;

    @Autowired
    private InsuranceCompanyConfigServiceFeign companyConfigServiceFeign;

    @Autowired
    private ContractServiceFeign contractServiceFeign;

    @Autowired
    private ProductBusinessServiceFegin productBusinessServiceFegin;

    @Autowired
    private PaymentOrderServiceFeign paymentOrderServiceFeign;

    @Autowired
    private EntityAuditLogServiceFeign entityAuditLogServiceFeign;

    @Autowired
    private RepaymentPlanServiceFeign repaymentPlanServiceFeign;

    @GetMapping(value = "/detail")
    public FintechResponse<WeChatRequisitionVO> getWeChatRequisitionDetail(@RequestParam("requisitionId") Integer requisitionId) {
        FintechResponse<WeChatRequisitionVO> response = requisitionServiceFeign.getWeChatRequisitionVO(requisitionId);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        WeChatRequisitionVO weChatRequisitionVO = response.getData();
        Integer loginUserId = getCurrentUserId();
        if(weChatRequisitionVO == null){
            throw new FInsuranceBaseException(104929);
        }
        if( weChatRequisitionVO.getCustomerId() == null || !weChatRequisitionVO.getCustomerId().equals(loginUserId)) {
            throw new FInsuranceBaseException(107048);
        }
        return response;
    }

    @GetMapping(value = "/confirm_apply_detail")
    @FinanceDuplicateSubmitDisable(value = 10)
    public FintechResponse<WeChatApplyRequisitionVO> getWeChatConfirmApplyRequisitionDetail(@RequestParam("requisitionId") Integer requisitionId) {
        FintechResponse<WeChatRequisitionVO> response =  requisitionServiceFeign.getWeChatRequisitionVO(requisitionId);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }

        WeChatRequisitionVO weChatRequisitionVO = response.getData();
        WeChatApplyRequisitionVO requisitionVO = null;
        if(weChatRequisitionVO != null){
            requisitionVO = new WeChatApplyRequisitionVO();
            requisitionVO.setRequisitionId(weChatRequisitionVO.getRequisitionId());
            // 申请总金额
            requisitionVO.setRequisitionAmount(weChatRequisitionVO.getRequisitionAmount());
            // 企业名称
            requisitionVO.setEnterpriseName(weChatRequisitionVO.getEnterpriseName());
            // 车辆数目
            requisitionVO.setCarCount(weChatRequisitionVO.getCarCount());
            // 保单数目
            requisitionVO.setInsuranceCount(weChatRequisitionVO.getInsuranceCount());
        }
        return FintechResponse.responseData(requisitionVO);
    }

    @GetMapping(value = "/instalment-detail")
    public FintechResponse<List<ContractSimpleDetail>> getWeChatContractByRequisitionId(@RequestParam(value = "requisitionId") Integer requisitionId) {
        FintechResponse<Pagination<ContractSimpleDetail>> response = contractServiceFeign.pageWechatContractByRequisitionId(requisitionId, 1, Integer.MAX_VALUE);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return FintechResponse.responseData(response.getData().getItems());
    }

    @PostMapping(value = "/apply-for")
    @FinanceDuplicateSubmitDisable(value = 10)
    public FintechResponse<VoidPlaceHolder> confirmApplyFor(@RequestBody @Validated IdVO idVO) {
        Integer currentLoginUserId = getCurrentUserId();
        requisitionServiceFeign.confirmApplyFor(idVO.getId(), currentLoginUserId);
        return FintechResponse.voidReturnInstance();
    }

    @PostMapping(value = "/again-apply-for")
    @FinanceDuplicateSubmitDisable(value = 10)
    public FintechResponse<WeChatRequisitionVO> againApplyFor(@RequestBody @Validated IdVO idVO) {
        FintechResponse<WeChatRequisitionVO> response = requisitionServiceFeign.againApplyFor(idVO);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    @PostMapping(value = "/cancel")
    @FinanceDuplicateSubmitDisable(value = 10)
    public FintechResponse<VoidPlaceHolder> cancelApplyFor(@RequestBody @Validated IdVO idVO) {
        RequisitionStatusVO statusVO = new RequisitionStatusVO();
        statusVO.setRequisitionId(idVO.getId());
        statusVO.setRequisitionStatus(RequisitionStatus.Canceled.getCode());
        requisitionServiceFeign.changeRequisitionStatus(statusVO);
        return FintechResponse.voidReturnInstance();
    }

    @GetMapping(value = "/pay")
    public FintechResponse<VoidPlaceHolder> payServiceFee(@RequestParam("requisitionId") Integer requisitionId) {
        FintechResponse<VoidPlaceHolder> response = requisitionServiceFeign.payRequisition(requisitionId);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    //通过选中产品保存用户申请单
    @RequestMapping(value = "/save-customer-requisition-by-channel", method = RequestMethod.POST)
    @FinanceDuplicateSubmitDisable(value = 10)
    public FintechResponse<Integer> saveCustomerRequisitionByChannel(@RequestBody SaveCustomerRequisitionVO saveCustomerRequisitionVO) {
        //获取单前登录用id
        Integer currentLoginUserId = this.getCurrentUserId();
        CustomerRequisitionVO customerRequisitionVO = new CustomerRequisitionVO();
        String channelCode = saveCustomerRequisitionVO.getChannelCode();
        customerRequisitionVO.setChannelCode(channelCode);
        Integer channelUserId = null;
        if(saveCustomerRequisitionVO.getCustomerAccountInfoId() != null){
            FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getCustomerAccountInfoById(saveCustomerRequisitionVO.getCustomerAccountInfoId());
            if(!customerVOFintechResponse.isOk()){
                throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
            }
            channelUserId = customerVOFintechResponse.getData() == null ? null : customerVOFintechResponse.getData().getChannelUserId();
        }
        if(channelUserId == null && StringUtils.isNoneBlank(channelCode)){
            FintechResponse<List<CustomerVO>>  userVOList = customerServiceFeign.listByCustomerIdAndChannelCode(currentLoginUserId, channelCode);
            if(!userVOList.isOk()){
                throw FInsuranceBaseException.buildFromErrorResponse(userVOList);
            }
            channelUserId = userVOList == null || userVOList.getData() == null  ? null : userVOList.getData().get(0).getChannelUserId();
        }
        //渠道用户id默认渠道用户id为0
        customerRequisitionVO.setChannelUserId(channelUserId == null ? 0 : channelUserId );

        Integer requisitionId = saveCustomerRequisitionVO.getId();
        RequisitionVO requisitionVO = null;
        if(requisitionId != null) {
            FintechResponse<RequisitionVO> requisitionVOFintechResponse = requisitionServiceFeign.getRequisitionById(requisitionId);
            if(!requisitionVOFintechResponse.isOk()){
                throw FInsuranceBaseException.buildFromErrorResponse(requisitionVOFintechResponse);
            }
            requisitionVO = requisitionVOFintechResponse.getData();
        }
        String[] otherResource = requisitionVO == null ? null : requisitionVO.getOtherResource();
        List<String> otherResourceList = new ArrayList<String>();
        if(otherResource != null && otherResource.length > 0){
            for(int i=0; i< otherResource.length ; i++){
                otherResourceList.add(otherResource[i]);
            }
        }
        //其他材料
        customerRequisitionVO.setOtherResourceList(otherResourceList);
        //手持身份证照片
        customerRequisitionVO.setIdCardEvidence(requisitionVO == null ? null : requisitionVO.getHandheldCertificate());
        //业务id
        customerRequisitionVO.setId(saveCustomerRequisitionVO.getId());
        //渠道code
        customerRequisitionVO.setChannelCode(StringUtils.isBlank(channelCode) ? saveCustomerRequisitionVO.getChannelCode() : channelCode );
        //产品id
        customerRequisitionVO.setProductId(saveCustomerRequisitionVO.getProductId());
        //客户id
        customerRequisitionVO.setCustomerId(currentLoginUserId);

        //客户指定分期默认不指定
        customerRequisitionVO.setBusinessDuration(0);
        //客户账户Info id
        Integer customerAccountInfoId = requisitionVO == null ? 0 : requisitionVO.getCustomerAccountInfoId();
        customerRequisitionVO.setCustomerAccountInfoId(customerAccountInfoId );
        CustomerLoanBankVO bankVO = new CustomerLoanBankVO();
        bankVO.setLoanAccountType(LoanAccountType.PERSONAL_LOAN_TYPE.getCode());
        bankVO.setLoanAccountNumber(requisitionVO == null ? null : requisitionVO.getLoanAccountNumber());
        customerRequisitionVO.setCustomerLoanBankVO(bankVO);
        customerRequisitionVO.setCreateBy(currentLoginUserId);
        customerRequisitionVO.setUpdateBy(currentLoginUserId);
        customerRequisitionVO.setChannelApplication(false);
        return requisitionServiceFeign.saveCustomerRequisition(customerRequisitionVO);
    }


    @RequestMapping(value = "/save-customer-requisition", method = RequestMethod.POST)
    @FinanceDuplicateSubmitDisable(value = 10)
    public FintechResponse<Integer> saveCustomerRequisition(@RequestBody WeChatCustomerRequisitionVO weChatCustomerRequisitionVO) {
        //获取单前登录用id
        Integer currentLoginUserId = this.getCurrentUserId();
        CustomerRequisitionVO customerRequisitionVO = new CustomerRequisitionVO();
        String channelCode = weChatCustomerRequisitionVO.getChannelCode();
        customerRequisitionVO.setChannelCode(channelCode);
        Integer channelUserId = null;
        if(weChatCustomerRequisitionVO.getCustomerAccountInfoId() != null){
            FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getCustomerAccountInfoById(weChatCustomerRequisitionVO.getCustomerAccountInfoId());
            if(!customerVOFintechResponse.isOk()){
                throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
            }
            channelUserId = customerVOFintechResponse.getData() == null ? null : customerVOFintechResponse.getData().getChannelUserId();
        }
        if(channelUserId == null && StringUtils.isNoneBlank(channelCode)){
            FintechResponse<List<CustomerVO>>  userVOList = customerServiceFeign.listByCustomerIdAndChannelCode(currentLoginUserId, channelCode);
            if(!userVOList.isOk()){
                throw FInsuranceBaseException.buildFromErrorResponse(userVOList);
            }
            channelUserId =  userVOList == null || userVOList.getData() == null ? null : userVOList.getData().get(0).getChannelUserId();
        }
        //渠道用户必须存在
        if(channelUserId == null){
            throw new FInsuranceBaseException(107051);
        }
        //渠道用户id
        customerRequisitionVO.setChannelUserId(channelUserId);

        FintechResponse<ProductVO> productVOFintechResponse = productBusinessServiceFegin.findWeChatProductInfoById(weChatCustomerRequisitionVO.getProductId());
        if(!productVOFintechResponse.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(productVOFintechResponse);
        }
        ProductVO productVO = productVOFintechResponse.getData();
        //业务id
        customerRequisitionVO.setId(weChatCustomerRequisitionVO.getId());
        //渠道code
        customerRequisitionVO.setChannelCode(StringUtils.isBlank(channelCode) ? weChatCustomerRequisitionVO.getChannelCode() : channelCode );
        //产品id
        customerRequisitionVO.setProductId(weChatCustomerRequisitionVO.getProductId());
        //客户id
        customerRequisitionVO.setCustomerId(currentLoginUserId);
        //其他材料
        customerRequisitionVO.setOtherResourceList(weChatCustomerRequisitionVO.getOtherResourceList());
        //身份证手持照片
        customerRequisitionVO.setIdCardEvidence(weChatCustomerRequisitionVO.getIdCardEvidence());
        //客户账号info id
        Integer customerAccountInfoId = weChatCustomerRequisitionVO.getCustomerAccountInfoId();
        //客户指定分期
        customerRequisitionVO.setBusinessDuration(customerRequisitionVO.getBusinessDuration());
        setCustomerLoanBankVO( customerRequisitionVO, productVO,  customerAccountInfoId, weChatCustomerRequisitionVO);
        //客户账户info id
        customerRequisitionVO.setCustomerAccountInfoId(customerAccountInfoId);
        //客户账户id
        customerRequisitionVO.setCustomerId(weChatCustomerRequisitionVO.getCustomerId());
        //客户银行卡信息
        if(weChatCustomerRequisitionVO.getCompanyId() != null || weChatCustomerRequisitionVO.getBranchId() != null ) {
            CustomerLoanBankVO bankVO = companyConfigServiceFeign.getLoanBankVO(weChatCustomerRequisitionVO.getCompanyId(), weChatCustomerRequisitionVO.getBranchId());
            customerRequisitionVO.setCustomerLoanBankVO(bankVO == null ? new CustomerLoanBankVO() : bankVO);
        }
        customerRequisitionVO.setCreateBy(currentLoginUserId);
        customerRequisitionVO.setUpdateBy(currentLoginUserId);
        customerRequisitionVO.setChannelApplication(false);
        return requisitionServiceFeign.saveCustomerRequisition(customerRequisitionVO);
    }

    //设置放款银行
    private void setCustomerLoanBankVO( CustomerRequisitionVO customerRequisitionVO,ProductVO productVO, Integer customerAccountInfoId,WeChatCustomerRequisitionVO weChatCustomerRequisitionVO){
        CustomerLoanBankVO bankVO = new CustomerLoanBankVO();
        if(StringUtils.equals(productVO.getProductType(), ProductType.POLICY_LOANS.getCode())) {
            //客户银行卡信息
            if (customerAccountInfoId != null && customerAccountInfoId != 0) {
                FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getCustomerAccountInfoById(customerAccountInfoId);
                if (!customerVOFintechResponse.isOk()) {
                    throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
                }
                CustomerVO customerVO = customerVOFintechResponse.getData();
                bankVO.setLoanAccountNumber(customerVO == null ? null : customerVO.getBankCard());
                bankVO.setLoanAccountType(LoanAccountType.PERSONAL_LOAN_TYPE.getCode());
            }
        } else {
            //客户银行卡信息
            if(weChatCustomerRequisitionVO.getCompanyId() != null || weChatCustomerRequisitionVO.getBranchId() != null) {
                bankVO = companyConfigServiceFeign.getLoanBankVO(weChatCustomerRequisitionVO.getCompanyId(), weChatCustomerRequisitionVO.getBranchId());
            }
        }
        customerRequisitionVO.setCustomerLoanBankVO(bankVO == null ? new CustomerLoanBankVO() : bankVO);
    }

    @RequestMapping(value = "/get-customer-requisition-by-id", method = RequestMethod.GET)
    public FintechResponse<WeChatCustomerRequisitionVO> findProductInfoById(@NotNull Integer id) {
        return commonRequisitionService.getCustomerRequisitionVOById(id);
    }

    @RequestMapping(value = "/save-requisition-detail", method = RequestMethod.POST)
    @FinanceDuplicateSubmitDisable(value = 10)
    public FintechResponse<Integer> saveRequisitionDetail(@RequestBody RequisitionDetailInfoVO requisitionDetailVO) {
        FintechResponse<Integer> integerFintechResponse = requisitionServiceFeign.saveRequisitionDetail(requisitionDetailVO);
        if(!integerFintechResponse.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(integerFintechResponse);
        }
        return integerFintechResponse;
    }

    @RequestMapping(value = "/insurance-detail", method = RequestMethod.GET)
    public FintechResponse<RequisitionDetailInfoVO> getInsuranceDetail(@NotNull Integer id) {
        FintechResponse<RequisitionDetailInfoVO>  response =  requisitionServiceFeign.getRequisitionDetailInfoById(id);
        if(!response.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    @RequestMapping(value = "/delete-requisition-detail", method = RequestMethod.POST)
    @FinanceDuplicateSubmitDisable(value = 10)
    public FintechResponse<VoidPlaceHolder> deleteRequisitionDetailById(@RequestBody IdVO idVO) {
        FintechResponse<VoidPlaceHolder> response = requisitionServiceFeign.deleteRequisitionDetailById(idVO);
        if(!response.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    /**
     * 查询业务单信息
     * @param id 业务单主键
     */
    @GetMapping(value = "/statistic_requisition_info/get")
    public FintechResponse<StatisticRequisitionVO> statisticRequisitionInfoById(@NotNull Integer id){
        FintechResponse<StatisticRequisitionVO> statisticRequisitionVOFintechResponse = requisitionServiceFeign.statisticRequisitionInfoById(id);
        if(!statisticRequisitionVOFintechResponse.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(statisticRequisitionVOFintechResponse);
        }
        return statisticRequisitionVOFintechResponse;
    }

    /**
     * 查询客户所有申请单信息
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public FintechResponse<Pagination<CustomerRequisitionSimpleVO>> pageRequisitionByCustomerId(@RequestParam(value = "productType", defaultValue = "") String productType,
                                                                                  @RequestParam(value = "requisitionStatus", defaultValue = "") String requisitionStatus,
                                                                                  @RequestParam(value = "pageIndex", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
                                                                                  @RequestParam(value = "pageSize", defaultValue = BasicConstants.DEFAULT_PAGE_SIZE) Integer pageSize){
        //客户端
        Integer currentLoginUserId = getCurrentUserId();
        FintechResponse<Pagination<RequisitionVO>> response =  requisitionServiceFeign.pageRequisitionByCustomerIdOrChannelUserId(currentLoginUserId.toString(),"",  productType, requisitionStatus, pageIndex, pageSize);

        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }

        List<CustomerRequisitionSimpleVO> list = new ArrayList<CustomerRequisitionSimpleVO>();
        if (null != response.getData().getItems() && response.getData().getItems().size() > 0) {
            for (RequisitionVO r : response.getData().getItems()) {
                FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getCustomerAccountInfoById(r.getCustomerAccountInfoId());
                if (!customerVOFintechResponse.isOk()) {
                    throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
                }
                CustomerVO customerVO = null;
                if (null != customerVOFintechResponse.getData()) {
                    customerVO = customerVOFintechResponse.getData();//当前登录客户信息
                }
                CustomerRequisitionSimpleVO customerRequisitionSimpleVO = this.convertToVO(r, customerVO);
                FintechResponse<CustomerBankCardVO> customerBankCardVOFintechResponse = customerServiceFeign.getCustomerBankCard(currentLoginUserId);
                if (!customerBankCardVOFintechResponse.isOk()) {
                    throw FInsuranceBaseException.buildFromErrorResponse(customerBankCardVOFintechResponse);
                }
                if (null != customerBankCardVOFintechResponse.getData()) {
                    customerRequisitionSimpleVO.setHasBindCard(1);//当前登录客户是否绑定过银行卡（1表示是，默认为0）
                }
                //服务费支付单信息
                if (RequisitionStatus.WaitingPayment.getCode().equals(r.getRequisitionStatus()) && null != r.getPaymentOrderNumber()) {//如果订单是待支付状态，查询服务费
                    FintechResponse<PaymentOrderVO> paymentOrderVOFintechResponse = paymentOrderServiceFeign.getByOrderNumber(r.getPaymentOrderNumber());
                    if (!paymentOrderVOFintechResponse.isOk()) {
                        throw FInsuranceBaseException.buildFromErrorResponse(paymentOrderVOFintechResponse);
                    }
                    if (null != paymentOrderVOFintechResponse.getData()) {
                        Long deadlinePaymentTime = DateCommonUtils.getEndTimeOfDate(DateCommonUtils.getAfterDay(r.getAuditSuccessTime(), 1)).getTime();
                        customerRequisitionSimpleVO.setDeadlinePaymentTime(deadlinePaymentTime);
                        customerRequisitionSimpleVO.setServiceFee(paymentOrderVOFintechResponse.getData().getTotalAmount());
                        customerRequisitionSimpleVO.setDebtStatus(paymentOrderVOFintechResponse.getData().getPaymentStatus());
                    }
                }

                if (RequisitionStatus.Rejected.getCode().equals(r.getRequisitionStatus())) {//如果申请单状态为退回状态，查询退回原因
                    FintechResponse<String> stringFintechResponse = entityAuditLogServiceFeign.getRemarkByEntityIdAndSome(r.getId(), EntityType.REQUISITION.getCode(), r.getLatestAuditBatch(), AuditStatus.REJECTED.getCode());
                    if (null != stringFintechResponse.getData()) {
                        customerRequisitionSimpleVO.setRejectedRemark("退回原因:" + stringFintechResponse.getData());
                    }
                }
                list.add(customerRequisitionSimpleVO);
            }
        }
        return FintechResponse.responseData(Pagination.createInstance(pageIndex, pageSize, response.getData().getTotalRowsCount(),list));
    }


    /**
     * 查询客户申请单车辆信息
     */
    @RequestMapping(value = "/query-requisition-detail/page", method = RequestMethod.GET)
    public FintechResponse<List<WeChatSimpleRequisitionDetailVO>> pageRequisitionDetailByRequisitionId(@RequestParam(value = "id") @NotNull Integer id,
                                                                                                             @RequestParam(value = "pageIndex", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
                                                                                                             @RequestParam(value = "pageSize", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageSize){
        return FintechResponse.responseData(commonRequisitionService.pageRequisitionDetailByRequisitionId(id, pageIndex, pageSize));
    }

    /**
     * 重新申请
     * @param idVO
     * @return
     */
    @RequestMapping(value = "/reapply", method = RequestMethod.POST)
    public FintechResponse<VoidPlaceHolder> reapply(@RequestBody IdVO idVO) {
        FintechResponse<RequisitionVO> requisitionVOFintechResponse = requisitionServiceFeign.getRequisitionById(idVO.getId());
        if (!requisitionVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(requisitionVOFintechResponse);
        }
        if (!RequisitionStatus.Rejected.getCode().equals(requisitionVOFintechResponse.getData().getRequisitionStatus())) {//不是退回状态不允许重新申请
            throw new FInsuranceBaseException(107030);
        }
        RequisitionStatusVO statusVO = new RequisitionStatusVO();
        statusVO.setRequisitionId(idVO.getId());
        statusVO.setRequisitionStatus(RequisitionStatus.Submitted.getCode());
        requisitionServiceFeign.changeRequisitionStatus(statusVO);
        return FintechResponse.voidReturnInstance();
    }

    /**
     * 撤销申请单
     * @param idVO
     * @return
     */
    @PostMapping(value = "/revoke")
    public FintechResponse<VoidPlaceHolder> Revoke(@RequestBody @Validated IdVO idVO) {
        FintechResponse<RequisitionVO> response = requisitionServiceFeign.getRequisitionById(idVO.getId());
        if (null == response.getData()) {
            throw new FInsuranceBaseException(107033);
        }
        RequisitionVO requisitionVO = response.getData();
        if (!RequisitionStatus.Submitted.getCode().equals(requisitionVO.getRequisitionStatus())) {//不是待客户确认状态（已提交）不允许撤销申请单
            throw new FInsuranceBaseException(107034);
        }
        //当前登录人
        Integer currentUserId = this.getCurrentUserId();
        if(!currentUserId.equals(requisitionVO.getCreateBy())){
            throw new FInsuranceBaseException(107047);
        }
        // 删除该申请单的相关还款计划和合同信息
        FintechResponse<List<ContractVO>> contractVOResponse = contractServiceFeign.getContractByRequisitionId(requisitionVO.getId());
        if (!contractVOResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(contractVOResponse);
        }
        List<ContractVO> contractVOList = contractVOResponse.getData();
        if (contractVOList != null && contractVOList.size() > 0) {
            for (ContractVO c : contractVOList) {
                //删除还款计划
                FintechResponse<VoidPlaceHolder> repaymentPlanResponse = repaymentPlanServiceFeign.deleteRepaymentPlanByContractNumber(c.getContractCode());
                if (!repaymentPlanResponse.isOk()) {
                    throw FInsuranceBaseException.buildFromErrorResponse(repaymentPlanResponse);
                }
                FintechResponse<VoidPlaceHolder> contractDeleteResponse = contractServiceFeign.delete(c.getContractId());//删除合同
                if (!contractDeleteResponse.isOk()) {
                    throw FInsuranceBaseException.buildFromErrorResponse(contractDeleteResponse);
                }
            }
        }
        //将申请单状态变为草稿
        FintechResponse<VoidPlaceHolder> result = requisitionServiceFeign.changeRequisitionStatus(new RequisitionStatusVO(idVO.getId(), RequisitionStatus.Draft.getCode()));
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }


    /**
     * 撤销申请单
     * @param requisitionId 申请单号
     * @return
     */
    @GetMapping(value = "/list-carnumber")
    public FintechResponse<List<ToPayVO>> listCarnumber(@RequestParam(value = "requisitionId") Integer requisitionId) {
        FintechResponse<List<RequisitionDetailVO>> response = requisitionServiceFeign.listRequisitionDetailByRequisitionId(requisitionId);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        if (null == response.getData()) {
            throw new FInsuranceBaseException(107033);
        }
        List<ToPayVO> toPayVOS = new ArrayList<>();
        if (null != response.getData() && response.getData().size() > 0) {
            for (RequisitionDetailVO vo : response.getData()) {
                ToPayVO toPayVO = new ToPayVO();
                toPayVO.setId(vo.getId());
                toPayVO.setContractId(vo.getContractId());
                toPayVO.setCarNumber(vo.getCarNumber());
                toPayVOS.add(toPayVO);
            }
         }

        return FintechResponse.responseData(toPayVOS);
    }


    //客户申请单详情
    private CustomerRequisitionSimpleVO convertToVO(RequisitionVO r, CustomerVO customerVO) {
        CustomerRequisitionSimpleVO vo = new CustomerRequisitionSimpleVO();
        if (null == r) {
            return vo;
        }
        if (null != customerVO) {
            vo.setCompanyName(customerVO.getCompanyOf());
        }
        vo.setCarNum(r.getCarNum());
        vo.setInsuranceNum(r.getInsuranceNum());
        vo.setRequisitionId(r.getId());
        vo.setRequisitionStatus(r.getRequisitionStatus());
        vo.setTotalApplyAmount(r.getTotalApplyAmount());
        vo.setIsChannelApplication(r.getChannelApplication() ? 1 : 0);
        vo.setTotalPayAmount(r.getTotalPayAmount());
        vo.setProductType(r.getProductType());
        if (r.getCustomerId().equals(getCurrentUserId())) {//是当前登录用户创建的申请单,则可以操作
            vo.setCanEdit(1);
        }
        return vo;
    }
}
