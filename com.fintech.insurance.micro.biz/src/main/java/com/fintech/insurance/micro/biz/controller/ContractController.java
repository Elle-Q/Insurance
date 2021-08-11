package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.biz.ContractServiceAPI;
import com.fintech.insurance.micro.biz.event.RequisitionLifeCycleEvent;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import com.fintech.insurance.micro.biz.service.BizAsyncService;
import com.fintech.insurance.micro.biz.service.ContractService;
import com.fintech.insurance.micro.biz.service.RequisitionService;
import com.fintech.insurance.micro.dto.biz.*;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Clayburn
 * @Description: 合同管理
 * @Date: 2017/11/9 18:19
 */
@RestController
@Validated
public class ContractController extends BaseFintechController implements ContractServiceAPI, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(ContractController.class);

    private ApplicationContext applicationContext;

    @Autowired
    private ContractService contractService;

    @Autowired
    private RequisitionService requisitionService;

    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    @Autowired
    private BizAsyncService bizAsyncService;

    //查询客户是否被冻结
    private void checkoutCustomerIsLocked(Integer customerId){
        if(customerId == null){
            LOG.error("checkoutCustomerIsLocked failed with null customerId");
            throw new FInsuranceBaseException(107031);
        }
        FintechResponse<Boolean> fintechResponse = customerServiceFeign.getCustomerLockedStatusById(customerId);
        if(!fintechResponse.isOk()){
            LOG.error("checkoutCustomerIsLocked failed with error code=["+ fintechResponse.getCode() +"]" );
            throw FInsuranceBaseException.buildFromErrorResponse(fintechResponse);
        }
        if(fintechResponse.getData() == null || fintechResponse.getData()){
            LOG.error("checkoutCustomerIsLocked failed with error customerId=["+ customerId +"]" );
            throw new FInsuranceBaseException(107032);
        }
    }

    @Override
    public FintechResponse<Pagination<ContractVO>> pageContract(@RequestParam(value = "contractCode", defaultValue = "") String contractCode,
                                                                @RequestParam(value = "contractStatus", defaultValue = "")String contractStatus,
                                                                @RequestParam(value = "channelName", defaultValue = "")String channelName,
                                                                @RequestParam(value = "customerName", defaultValue = "")String customerName,
                                                                @RequestParam(value = "carNumber", defaultValue = "")String carNumber,
                                                                @RequestParam(value = "loanBeginDate", required = false)Long loanBeginDate,
                                                                @RequestParam(value = "loanEndDate", required = false)Long loanEndDate,
                                                                @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        ContractStatus status = null;
        try {
            if (StringUtils.isNotBlank(contractStatus)) {
                status = ContractStatus.codeOf(contractStatus);
            }
        } catch (Exception e) {
            throw new FInsuranceBaseException("104104", new Object[]{contractCode, ContractStatus.class.getName()});
        }
        Date begin = loanBeginDate == null ? null : DateCommonUtils.truncateDay(new Date(loanBeginDate));
        Date end = loanEndDate == null ? null : DateCommonUtils.getEndTimeOfDate(loanEndDate);
        return FintechResponse.responseData(contractService.pageContract(contractCode, status, channelName, customerName, carNumber, begin, end, pageIndex, pageSize));
    }

    @Override
    public FintechResponse<Pagination<ContractVO>> pageWechatContract(String contractStatus, Integer pageIndex, Integer pageSize) {
        ContractStatus status = null;
        if (StringUtils.isNotBlank(contractStatus)) {
            try {
                status = ContractStatus.codeOf(contractStatus);
            } catch (Exception e) {
                throw new FInsuranceBaseException("104104", new Object[]{contractStatus, ContractStatus.class.getName()});
            }
        }
        Pagination<ContractVO> page = contractService.pageContractVOByContractStatus(status, pageIndex, pageSize);
        return FintechResponse.responseData(page);
    }

    @Override
    public FintechResponse<Pagination<ContractSimpleDetail>> pageWechatContractByRequisitionId(Integer requisitionId, Integer pageIndex, Integer pageSize) {
        return FintechResponse.responseData(contractService.pageWechatContractByRequisitionId(requisitionId, pageIndex, pageSize));
    }

    @Override
    public FintechResponse<InstalmentDetailVO> getWeChatContractDetailByContractId(Integer contractId) {
        return FintechResponse.responseData(contractService.getWeChatContractDetailByContractId(contractId));
    }

    @Override
    public FintechResponse<ContractDetailVO> getContractDetailByNo(@RequestParam("contractCode") String contractCode) {
        return FintechResponse.responseData(contractService.getDetailByContractNumber(contractCode));
    }

    @Override
    public List<ContractVO> getContractByRequisitionNumberAndCustomerName(@RequestParam(value = "requisitionNumber") String requisitionNumber,@RequestParam(value = "customerName") String customerName) {
        return contractService.getContractByRequisitionNumberAndCustomerName(requisitionNumber, customerName);
    }


    @Override
    public RequisitionVO getRequisition(@RequestParam(value = "contractNumber", defaultValue = "") String contractNumber) {
        return contractService.getRequisitionByContractNumber(contractNumber);
    }

    @Override
    public FintechResponse<ContractInfoVO> getRequisitionContractInfoVOByRequisitionId(@RequestParam(value = "requisitionId") Integer requisitionId, @RequestParam(value = "month")  Integer month) {
        LOG.info("Get requisition detail by requsitionId = " + requisitionId + " , month=" + month);
        if(requisitionId == null || month == null){
            throw new FInsuranceBaseException(104901);
        }

        Requisition requisition = requisitionService.getRequisitionEntityById(requisitionId);
        if(requisition == null){
            throw new FInsuranceBaseException(104904,new Object[]{"requisitionId=" + requisitionId});
        }

        //三种情况才能重新提交
        if(!StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Draft.getCode())
                && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Rejected.getCode())
                && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Canceled.getCode()) ){
            LOG.error("getRequisitionContractInfoVOByRequisitionId failed with repeat with requsitionId = " + requisition.getId() + " , month=" + month);
            //获取合同信息
            return FintechResponse.responseData(buildContractInfoVO(requisition, month));
        }

        this.checkoutCustomerIsLocked(requisition.getCustomerId());
        if(requisition.getProduct() == null || requisition.getProduct().getProductRateSet() == null || requisition.getProduct().getProductRateSet().size() < 1){
            throw new FInsuranceBaseException(104106);
        }
        if(requisition.getDetails() == null || requisition.getDetails().size() < 1){
            throw new FInsuranceBaseException(104927);
        }
        if(StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Loaned.getCode())){
            throw new FInsuranceBaseException(105929);
        }

        requisitionService.createContractDataForRequsition(requisition, month);
        LOG.info("The requisition has " + requisition.getDetails().size() + " requsition details, month=" + month);
        if(requisition.getDetails().size() < 1) {
            throw new FInsuranceBaseException(104933);
        }
        return FintechResponse.responseData(buildContractInfoVO(requisition, month));
    }

    //获取合同信息
    private ContractInfoVO buildContractInfoVO(Requisition requisition, Integer month){
        ContractInfoVO contractInfoVO = contractService.buildContractInfoVO(requisition, new ArrayList<RequisitionDetail>(requisition.getDetails()));
        if(contractInfoVO == null){
            LOG.error(" null contractInfoVO with requsitionId = " + requisition.getId() + " , month=" + month);
        }
        return contractInfoVO;
    }

    @Override
    public FintechResponse<ContractFileRequestVO> saveRequisitionContractInfoVO(@RequestParam(value = "requisitionId") Integer requisitionId) {
        if(requisitionId == null){
            throw new FInsuranceBaseException(104901);
        }
        Requisition requisition = requisitionService.getRequisitionEntityById(requisitionId);

        if(requisition == null){
            throw new FInsuranceBaseException(104904,new Object[]{"requisitionId=" + requisitionId});
        }

        if (requisition.getChannel().getLocked()) {
            throw new FInsuranceBaseException(107044);
        }

        //三种情况才能重新提交
        if(!StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Draft.getCode())
                && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Rejected.getCode())
                && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Canceled.getCode()) ){
            throw new FInsuranceBaseException(105930);
        }

        this.checkoutCustomerIsLocked(requisition.getCustomerId());
        if(requisition.getProduct() == null || requisition.getProduct().getProductRateSet() == null || requisition.getProduct().getProductRateSet().size() < 1){
            throw new FInsuranceBaseException(104106);
        }
        if(requisition.getDetails() == null || requisition.getDetails().size() < 1){
            throw new FInsuranceBaseException(104927);
        }
        if(StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Loaned.getCode())){
            throw new FInsuranceBaseException(105929);
        }
        //检查业务单用的合法性
        FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getByCustomerIdAndChannelCode(requisition.getCustomerId(), requisition.getChannel().getChannelCode());
        if(!customerVOFintechResponse.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
        }
        if(customerVOFintechResponse.getData() == null){
            throw new FInsuranceBaseException(107040);
        }
        String oldStatus = requisition.getRequisitionStatus();
        //更新业务单信息
        requisition.setRequisitionStatus(RequisitionStatus.Submitted.getCode());
        requisition.setSubmissionDate(new Date());
        requisitionService.save(requisition);
        // i do not know what could i say fuck!
        if (!RequisitionStatus.Submitted.getCode().equals(oldStatus)) {
            this.applicationContext.publishEvent(new RequisitionLifeCycleEvent(requisition, oldStatus, RequisitionStatus.Submitted.getCode()));
        }

        CustomerVO customerVO = customerVOFintechResponse.getData();
        //产品类型
        ProductType productType = ProductType.codeOf(requisition.getProductType());
        ContractFileRequestVO contractFileRequestVO = new ContractFileRequestVO();
        contractFileRequestVO.setUserAccountId(customerVO.getIdNum());
        contractFileRequestVO.setMobile(customerVO.getMobile());
        contractFileRequestVO.setProductType(productType);
        contractFileRequestVO.setRequisitionId(requisitionId);

        //bizAsyncService.generateContractFileForRequsition(requisitionId);
        return FintechResponse.responseData(contractFileRequestVO);
    }

    @Override
    public FintechResponse<VoidPlaceHolder> updateContractStatus(String contractNumber, String targetStatus) {
        contractService.updateContractStatus(contractNumber, ContractStatus.codeOf(targetStatus));
        return FintechResponse.voidReturnInstance();
    }

    @Override
    public FintechResponse<List<ContractVO>> getContractByRequisitionId(@RequestParam(value = "id") Integer id) {
        return FintechResponse.responseData(contractService.getContractByRequisitionId(id));
    }

    @Override
    public FintechResponse<String> getRequisitionContractFile(@RequestParam(value = "contractId") Integer contractId,
                                                              @RequestParam(value = "isServiceContract") Integer isServiceContract,
                                                              @RequestParam(value = "isPictureNeeded") Boolean isPictureNeeded) {
        return FintechResponse.responseData(contractService.getRequisitionContractFile(contractId, 1 == isServiceContract, isPictureNeeded));
    }

    @Override
    public FintechResponse<VoidPlaceHolder> changeStatusToSurrenderByContractNumber(@RequestBody List<String> contractNumbers) {
        contractService.changeStatusToSurrenderByContractNumber(contractNumbers);
        return  FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<ContractVO> getContractInfoByContractId(@RequestParam(value = "contractId") Integer contractId) {
        ContractVO contractVO = contractService.getContractInfoByContractId(contractId);
        return FintechResponse.responseData(contractVO);
    }

    @Override
    public FintechResponse<VoidPlaceHolder> delete(@RequestParam(value = "contractId") Integer contractId) {
        contractService.delete(contractId);
        return  FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<ContractVO> getContractDetailByContractNumber(@RequestParam(value = "contractNumber", required = false) String contractNumber) {
        ContractVO contractVO = contractService.getContractDetailByContractNumber(contractNumber);
        return FintechResponse.responseData(contractVO);
    }

    @Override
    public FintechResponse<ContractVO> getOverdueContractVOByContractNumber(@RequestParam(value = "contractNumber", required = false) String contractNumber) {
        ContractVO contractVO = contractService.getOverdueContractVOByContractNumber(contractNumber);
        return FintechResponse.responseData(contractVO);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public FintechResponse<Double> getOverdueFineRateByContractNumber(@RequestParam(value = "contractNumber", required = false) String contractNumber) {
        Double overdueFineRate = contractService.getOverdueFineRateByContractNumber(contractNumber);
        return FintechResponse.responseData(overdueFineRate);
    }

    @Override
    public FintechResponse<VoidPlaceHolder> generateContractFileForRequsitions() {
        contractService.generateContractFileForRequsitions();
        return  FintechResponse.responseData(VoidPlaceHolder.instance());
    }
}