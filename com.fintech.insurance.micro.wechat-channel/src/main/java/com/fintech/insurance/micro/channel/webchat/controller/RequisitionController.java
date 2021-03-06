package com.fintech.insurance.micro.channel.webchat.controller;

import com.fintech.insurance.commons.annotations.FinanceDuplicateSubmitDisable;
import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.*;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.biz.*;
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
import com.fintech.insurance.micro.vo.wechat.CustomerRequisitionSimpleVO;
import com.fintech.insurance.micro.vo.wechat.WeChatCustomerRequisitionVO;
import com.fintech.insurance.micro.vo.wechat.WeChatSimpleRequisitionDetailVO;
import com.fintech.insurance.service.agg.CommonRequisitionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @Description: ?????????????????????????????????
 * @Author: Yong Li
 * @Date: 2017/12/6 18:55
 */
@RestController
@RequestMapping(value = "/wechat/channel/requisition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequireWechatLogin
public class RequisitionController extends BaseFintechWechatController {

    private static final Logger log = LoggerFactory.getLogger(RequisitionController.class);

    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    @Autowired
    private RequisitionServiceFeign requisitionServiceFeign;

    @Autowired
    private CommonRequisitionService commonRequisitionService;

    @Autowired
    private SysUserServiceFeign sysUserServiceFeign;

    @Autowired
    private InsuranceCompanyConfigServiceFeign companyConfigServiceFeign;

    @Autowired
    private ContractServiceFeign contractServiceFeign;

    @Autowired
    private ProductBusinessServiceFegin productBusinessServiceFegin;

    @Autowired
    private EntityAuditLogServiceFeign entityAuditLogServiceFeign;

    @Autowired
    private RepaymentPlanServiceFeign repaymentPlanServiceFeign;

    @Autowired
    private PaymentOrderServiceFeign paymentOrderServiceFeign;

    @GetMapping(value = "/detail")
    public FintechResponse<WeChatRequisitionVO> getWeChatRequisitionDetail(@RequestParam("requisitionId") Integer requisitionId) {
        FintechResponse<WeChatRequisitionVO> response =  requisitionServiceFeign.getWeChatRequisitionVO(requisitionId);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    @GetMapping(value = "/instalment-detail")
    public FintechResponse<List<ContractSimpleDetail>> getWeChatContractByRequisitionId(@RequestParam(value = "requisitionId") Integer requisitionId) {
        FintechResponse<Pagination<ContractSimpleDetail>> response = contractServiceFeign.pageWechatContractByRequisitionId(requisitionId, 1, Integer.MAX_VALUE);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        //?????????????????????????????????
        List<ContractSimpleDetail> list = response.getData().getItems();
        return FintechResponse.responseData(list);
    }

    @PostMapping(value = "/apply-for")
    @FinanceDuplicateSubmitDisable(value = 10)
    public FintechResponse<VoidPlaceHolder> confirmApplyFor(@RequestBody @Validated IdVO idVO) {
        FintechResponse<VoidPlaceHolder> response = requisitionServiceFeign.submitRequisitionForAudit(idVO);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return FintechResponse.voidReturnInstance();
    }

    @PostMapping(value = "/again-apply-for")
    @FinanceDuplicateSubmitDisable(value = 10)
    public FintechResponse<WeChatRequisitionVO> againApplyFor(@RequestBody @Validated IdVO idVO) {
        FintechResponse<WeChatRequisitionVO> response = requisitionServiceFeign.againApplyFor(idVO);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        if (response.getData() == null) {
            throw new FInsuranceBaseException(107035);
        }
        return response;
    }

    @RequestMapping(value = "/save-customer-requisition", method = RequestMethod.POST)
    @FinanceDuplicateSubmitDisable(value = 10)
    public FintechResponse<Integer> saveCustomerRequisition(@RequestBody WeChatCustomerRequisitionVO weChatCustomerRequisitionVO) {
        //?????????????????????id
        Integer currentLoginUserId = this.getCurrentUserId();
        String channelCode = this.getCurrentUserChannelCode();
        CustomerRequisitionVO customerRequisitionVO = new CustomerRequisitionVO();
        //??????id
        customerRequisitionVO.setId(weChatCustomerRequisitionVO.getId());
        //????????????id
        customerRequisitionVO.setChannelUserId(currentLoginUserId);
        customerRequisitionVO.setChannelCode(channelCode);
        //??????code
        customerRequisitionVO.setChannelCode(StringUtils.isBlank(channelCode) ? weChatCustomerRequisitionVO.getChannelCode() : channelCode );
        //??????id
        customerRequisitionVO.setProductId(weChatCustomerRequisitionVO.getProductId());
        //??????id
        customerRequisitionVO.setCustomerId(weChatCustomerRequisitionVO.getCustomerId());
        //????????????
        customerRequisitionVO.setOtherResourceList(weChatCustomerRequisitionVO.getOtherResourceList());
        //?????????????????????
        customerRequisitionVO.setIdCardEvidence(weChatCustomerRequisitionVO.getIdCardEvidence());
        //?????????????????????????????????0
        customerRequisitionVO.setBusinessDuration(0);

        //????????????
        ProductVO productVO = new ProductVO();
        log.error("saveCustomerRequisition with productId=[" + weChatCustomerRequisitionVO.getProductId() + "]");
        if(weChatCustomerRequisitionVO.getProductId() != null) {
            FintechResponse<ProductVO> productVOFintechResponse = productBusinessServiceFegin.findWeChatProductInfoById(weChatCustomerRequisitionVO.getProductId());
            if (!productVOFintechResponse.isOk()) {
                throw FInsuranceBaseException.buildFromErrorResponse(productVOFintechResponse);
            }
            productVO = productVOFintechResponse.getData();
        }
        //????????????info id
        Integer customerAccountInfoId = weChatCustomerRequisitionVO.getCustomerAccountInfoId();
        customerRequisitionVO.setCustomerAccountInfoId(customerAccountInfoId);
        //??????????????????
        setCustomerLoanBankVO(customerRequisitionVO, productVO,  customerAccountInfoId, weChatCustomerRequisitionVO);
        customerRequisitionVO.setCreateBy(currentLoginUserId);
        customerRequisitionVO.setUpdateBy(currentLoginUserId);
        customerRequisitionVO.setChannelApplication(true);
        return requisitionServiceFeign.saveCustomerRequisition(customerRequisitionVO);
    }

    //??????????????????
    private void setCustomerLoanBankVO( CustomerRequisitionVO customerRequisitionVO,ProductVO productVO, Integer customerAccountInfoId,WeChatCustomerRequisitionVO weChatCustomerRequisitionVO){
        CustomerLoanBankVO bankVO = new CustomerLoanBankVO();
        if(productVO == null || StringUtils.equals(productVO.getProductType(), ProductType.CAR_INSTALMENTS.getCode())) {
            //?????????????????????
            if(weChatCustomerRequisitionVO.getCompanyId() != null || weChatCustomerRequisitionVO.getBranchId() != null) {
                bankVO = companyConfigServiceFeign.getLoanBankVO(weChatCustomerRequisitionVO.getCompanyId(), weChatCustomerRequisitionVO.getBranchId());
            }
        }else{
            //?????????????????????
            if (customerAccountInfoId != null && customerAccountInfoId != 0) {
                FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getCustomerAccountInfoById(customerAccountInfoId);
                if (!customerVOFintechResponse.isOk()) {
                    throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
                }
                CustomerVO customerVO = customerVOFintechResponse.getData();
                bankVO.setLoanAccountNumber(customerVO == null ? null : customerVO.getBankCard());
                bankVO.setLoanAccountName(customerVO == null ? null : customerVO.getName());
                bankVO.setLoanAccountType(LoanAccountType.PERSONAL_LOAN_TYPE.getCode());
            }
        }
        customerRequisitionVO.setCustomerLoanBankVO(bankVO == null ? new CustomerLoanBankVO() : bankVO);
    }

    @RequestMapping(value = "/get-customer-requisition-by-id", method = RequestMethod.GET)
    public FintechResponse<WeChatCustomerRequisitionVO> getCustomerRequisitionVOById(@RequestParam(value = "id") @NotNull Integer id) {
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
    public FintechResponse<RequisitionDetailInfoVO> getInsuranceDetail(@RequestParam(value = "id") @NotNull Integer id) {
        FintechResponse<RequisitionDetailInfoVO> requisitionDetailFintechResponse = requisitionServiceFeign.getRequisitionDetailInfoById(id);
        if(!requisitionDetailFintechResponse.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(requisitionDetailFintechResponse);
        }
        return requisitionDetailFintechResponse;
    }

    @RequestMapping(value = "/delete-requisition-detail", method = RequestMethod.POST)
    @FinanceDuplicateSubmitDisable(value = 10)
    public FintechResponse<VoidPlaceHolder> deleteRequisitionDetailById(@Validated @RequestBody IdVO idVO) {
         FintechResponse<VoidPlaceHolder> response = requisitionServiceFeign.deleteRequisitionDetailById(idVO);
         if (!response.isOk()) {
             throw FInsuranceBaseException.buildFromErrorResponse(response);
         }
         return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    /**
     * ???????????????????????????
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public FintechResponse<Pagination<CustomerRequisitionSimpleVO>> pageRequisitionByCustomerId(@RequestParam(value = "productType", defaultValue = "") String productType,
                                                                                                @RequestParam(value = "requisitionStatus", defaultValue = "") String requisitionStatus,
                                                                                                @RequestParam(value = "pageIndex", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
                                                                                                @RequestParam(value = "pageSize", defaultValue = BasicConstants.DEFAULT_PAGE_SIZE) Integer pageSize){
        Integer currentLoginUserId = getCurrentUserId();
        StringBuilder channelUserIds = new StringBuilder();
        FintechResponse<UserVO> userVOFintechResponse = sysUserServiceFeign.getUserById(currentLoginUserId);
        if (!userVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(userVOFintechResponse);
        }
        UserVO userVO = userVOFintechResponse.getData();
        if (null == userVO) {
            throw new FInsuranceBaseException(107025);
        }
        channelUserIds.append(userVO.getId());
        if (UserType.CHANNEL.getCode().equals(userVO.getUserType()) && userVO.isChannelAdmin()) {
            FintechResponse<List<UserVO>> userVOList = sysUserServiceFeign.listChannelUserByCode(userVO.getChannelCode());
            if (null != userVOList && userVOList.getData().size() > 0) {
                for (UserVO u : userVOList.getData()) {
                    channelUserIds.append("," + u.getId());
                }
            }
        }
        FintechResponse<Pagination<RequisitionVO>> response =  requisitionServiceFeign.pageRequisitionByCustomerIdOrChannelUserId("", channelUserIds.toString(), productType, requisitionStatus, pageIndex, pageSize);

        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }

        List<CustomerRequisitionSimpleVO> list = new ArrayList<>();
        if (null != response.getData().getItems() && response.getData().getItems().size() > 0) {
            for (RequisitionVO r : response.getData().getItems()) {
                FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getCustomerAccountInfoById(r.getCustomerAccountInfoId());
                if (!customerVOFintechResponse.isOk()) {
                    throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
                }
                CustomerVO customerVO = customerVOFintechResponse.getData();//????????????????????????
                CustomerRequisitionSimpleVO customerRequisitionSimpleVO = this.convertToVO(r, customerVO);

                if (r.getChannelUserId().equals(currentLoginUserId)) {//??????????????????????????????????????????????????????????????????
                    customerRequisitionSimpleVO.setCanEdit(1);
                }
                //????????????????????????
                if (RequisitionStatus.WaitingPayment.getCode().equals(r.getRequisitionStatus()) && null != r.getPaymentOrderNumber()) {//????????????????????????????????????????????????
                    FintechResponse<PaymentOrderVO> paymentOrderVOFintechResponse = paymentOrderServiceFeign.getByOrderNumber(r.getPaymentOrderNumber());
                    if (!paymentOrderVOFintechResponse.isOk()) {
                        throw FInsuranceBaseException.buildFromErrorResponse(paymentOrderVOFintechResponse);
                    }
                    customerRequisitionSimpleVO.setServiceFee(paymentOrderVOFintechResponse.getData().getTotalAmount());
                    customerRequisitionSimpleVO.setDebtStatus(paymentOrderVOFintechResponse.getData().getPaymentStatus());
                }

                if (RequisitionStatus.Rejected.getCode().equals(r.getRequisitionStatus())) {//?????????????????????????????????????????????????????????
                    FintechResponse<String> stringFintechResponse = entityAuditLogServiceFeign.getRemarkByEntityIdAndSome(r.getId(), EntityType.REQUISITION.getCode(), r.getLatestAuditBatch(), AuditStatus.REJECTED.getCode());
                    if (null != stringFintechResponse.getData()) {
                        customerRequisitionSimpleVO.setRejectedRemark("????????????:" + stringFintechResponse.getData());
                    }
                }
                // ????????????????????????????????????????????????
                if (r.getRequisitionStatus().equals(RequisitionStatus.Draft.getCode()) && !r.getChannelUserId().equals(currentLoginUserId)) {
                    continue;
                }
                list.add(customerRequisitionSimpleVO);
            }
        }
        return FintechResponse.responseData(Pagination.createInstance(pageIndex, pageSize, response.getData().getTotalRowsCount(),list));
    }

    /**
     * ?????????????????????????????????
     */
    @RequestMapping(value = "/query-requisition-detail/page", method = RequestMethod.GET)
    public FintechResponse<List<WeChatSimpleRequisitionDetailVO>> pageRequisitionDetailByRequisitionId(@RequestParam(value = "id") @NotNull Integer id,
                                                                                                    @RequestParam(value = "pageIndex", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
                                                                                                    @RequestParam(value = "pageSize", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageSize){
        return FintechResponse.responseData(commonRequisitionService.pageRequisitionDetailByRequisitionId(id, pageIndex, pageSize));
    }

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
        vo.setProductType(r.getProductType());
        vo.setIsChannelApplication(r.getChannelApplication() ? 1 : 0);
        vo.setTotalPayAmount(r.getTotalPayAmount());
        return vo;
    }

    @RequestMapping(value = "/statistic_requisition_info/get", method = RequestMethod.GET)
    public FintechResponse<StatisticRequisitionVO> statisticRequisitionInfoById(@RequestParam(value = "id") @NotNull Integer id) {
        FintechResponse<StatisticRequisitionVO> response = requisitionServiceFeign.statisticRequisitionInfoById(id);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return response;
    }

    /**
     * ???????????????
     * @param idVO
     * @return
     */
    @PostMapping(value = "/cancel")
    @FinanceDuplicateSubmitDisable(value = 10)
    public FintechResponse<VoidPlaceHolder> cancelApplyFor(@RequestBody @Validated IdVO idVO) {
        RequisitionStatusVO statusVO = new RequisitionStatusVO();
        statusVO.setRequisitionId(idVO.getId());
        statusVO.setRequisitionStatus(RequisitionStatus.Canceled.getCode());
        FintechResponse<VoidPlaceHolder>  response = requisitionServiceFeign.changeRequisitionStatus(statusVO);
        if (!response.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        return FintechResponse.voidReturnInstance();
    }

    /**
     * ???????????????
     * @param idVO
     * @return
     */
    @PostMapping(value = "/revoke")
    public FintechResponse<VoidPlaceHolder> Revoke(@RequestBody @Validated IdVO idVO) {
        FintechResponse<RequisitionVO> response = requisitionServiceFeign.getRequisitionById(idVO.getId());
        if (null == response.getData()) {
            throw new FInsuranceBaseException(107033);
        }
        //???????????????
        Integer currentUserId = this.getCurrentUserId();
        RequisitionVO requisitionVO = response.getData();
        if (!RequisitionStatus.Submitted.getCode().equals(requisitionVO.getRequisitionStatus())) {//??????????????????????????????????????????????????????????????????
            throw new FInsuranceBaseException(107034);
        }
        if(!currentUserId.equals(requisitionVO.getCreateBy())){
            throw new FInsuranceBaseException(107047);
        }
        //??????????????????????????????????????????????????????
        FintechResponse<List<ContractVO>> contractVOResponse = contractServiceFeign.getContractByRequisitionId(requisitionVO.getId());
        if (!contractVOResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(contractVOResponse);
        }
        List<ContractVO> contractVOList = contractVOResponse.getData();
        if (contractVOList != null && contractVOList.size() > 0) {
            for (ContractVO c : contractVOList) {
                //??????????????????
                FintechResponse<VoidPlaceHolder> repaymentPlanResponse = repaymentPlanServiceFeign.deleteRepaymentPlanByContractNumber(c.getContractCode());
                if (!repaymentPlanResponse.isOk()) {
                    throw FInsuranceBaseException.buildFromErrorResponse(repaymentPlanResponse);
                }
                FintechResponse<VoidPlaceHolder> contractDeleteResponse = contractServiceFeign.delete(c.getContractId());//????????????
                if (!contractDeleteResponse.isOk()) {
                    throw FInsuranceBaseException.buildFromErrorResponse(contractDeleteResponse);
                }
            }
        }
        requisitionVO.setRequisitionStatus(RequisitionStatus.Draft.getCode());//???????????????????????????
        requisitionVO.setUpdateBy(currentUserId);
        requisitionVO.setUpdateAt(new Date());
        FintechResponse<VoidPlaceHolder> result = requisitionServiceFeign.saveRequisition(requisitionVO);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }
}
