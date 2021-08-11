package com.fintech.insurance.micro.api.biz;

import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;


@RequestMapping(value = "/biz/contract", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface ContractServiceAPI {
    /**
     * 条件查询合同
     * @param contractCode  合同编号
     * @param contractStatus 合同状态
     * @param channelName 渠道名称
     * @param customerName 客户名称
     * @param loanBeginDate 放款开始时间
     * @param loanEndDate 放款结束时间
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    FintechResponse<Pagination<ContractVO>> pageContract(@RequestParam(value = "contractCode", defaultValue = "") String contractCode,
                                        @RequestParam(value = "contractStatus", defaultValue = "")String contractStatus,
                                        @RequestParam(value = "channelName", defaultValue = "")String channelName,
                                        @RequestParam(value = "customerName", defaultValue = "")String customerName,
                                        @RequestParam(value = "carNumber", defaultValue = "")String carNumber,
                                        @RequestParam(value = "loanBeginDate", required = false)Long loanBeginDate,
                                        @RequestParam(value = "loanEndDate", required = false)Long loanEndDate,
                                        @RequestParam(value = "pageIndex", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
                                        @RequestParam(value = "pageSize", defaultValue = BasicConstants.DEFAULT_PAGE_SIZE) Integer pageSize);

    /**
     * 微信端查询分期信息
     * @param contractStatus 合同的状态
     * @return
     */
    @GetMapping(value = "/page-wechat-contract")
    FintechResponse<Pagination<ContractVO>> pageWechatContract(@RequestParam(value = "contractStatus", defaultValue = "") String contractStatus,
                                                               @RequestParam(value = "pageIndex", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
                                                               @RequestParam(value = "pageSize", defaultValue = BasicConstants.DEFAULT_PAGE_SIZE) Integer pageSize);

    /**
     * 根据订单id查询合同
     * @param requisitionId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping(value = "page-wechat-contract-by-requisitionid")
    FintechResponse<Pagination<ContractSimpleDetail>> pageWechatContractByRequisitionId(@RequestParam(value = "requisitionId", defaultValue = "") Integer requisitionId,
                                                                                        @RequestParam(value = "pageIndex", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
                                                                                        @RequestParam(value = "pageSize", defaultValue = BasicConstants.DEFAULT_PAGE_SIZE) Integer pageSize);

    @GetMapping(value = "/detail-wechat-contract")
    FintechResponse<InstalmentDetailVO> getWeChatContractDetailByContractId(@RequestParam(value = "contractId") Integer contractId);

    /**
     * 根据合同编号查询合同详情
     * @param contractCode
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    FintechResponse<ContractDetailVO> getContractDetailByNo(@RequestParam("contractCode") String contractCode);


    @RequestMapping(value = "/get-contractcode", method = RequestMethod.GET)
    List<ContractVO> getContractByRequisitionNumberAndCustomerName(@RequestParam(value = "requisitionNumber", defaultValue = "") String requisitionNumber,
                                                                   @RequestParam(value = "customerName", defaultValue = "") String customerName);

    @RequestMapping(value = "/get-requisition", method = RequestMethod.GET)
    RequisitionVO getRequisition(@RequestParam(value = "contractNumber", defaultValue = "") String contractNumber);

    @RequestMapping(value = "/get-requisition-contract", method = RequestMethod.GET)
    FintechResponse<ContractInfoVO> getRequisitionContractInfoVOByRequisitionId(@RequestParam(value = "requisitionId") @NotNull Integer requisitionId,
                                                                                @RequestParam(value = "month") @NotNull @Min(1) Integer month);

    @RequestMapping(value = "/save-requisition-contract", method = RequestMethod.GET)
    FintechResponse<ContractFileRequestVO> saveRequisitionContractInfoVO(@RequestParam(value = "requisitionId") Integer requisitionId);

    @GetMapping(value = "/update-status")
    FintechResponse<VoidPlaceHolder> updateContractStatus(@RequestParam(value = "contractNumber") String contractNumber,
                                                          @RequestParam(value = "targetStatus") String targetStatus);

    /**
     *  WX根据申请单id查询所有合同信息
     * @param id  申请单id
     * @return
     */
    @RequestMapping(value = "/get-contract-by-requisition-id", method = RequestMethod.GET)
    FintechResponse<List<ContractVO>> getContractByRequisitionId(@RequestParam(value = "id") Integer id);

    /**
     *  把合同状态改变成为待退保
     * @param contractNumbers  合同号集合
     * @return
     */
    @RequestMapping(value = "/change-status-to-surrender", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> changeStatusToSurrenderByContractNumber(@RequestBody List<String> contractNumbers);

    //获取合同详情
    @GetMapping(value = "/get-contract-info-by-id")
    FintechResponse<ContractVO> getContractInfoByContractId(@RequestParam(value = "contractId") Integer contractId);

    /**
     *  WX根据申请单id查询所有合同信息
     * @param contractId  合同id
     * @return
     */
    @RequestMapping(value = "/get-requisition-contract-file", method = RequestMethod.GET)
    FintechResponse<String> getRequisitionContractFile(@RequestParam(value = "contractId") Integer contractId,
                                                       @RequestParam(value = "isServiceContract") Integer isServiceContract,
                                                       @RequestParam(value = "isPictureNeeded") Boolean isPictureNeeded);

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    FintechResponse<VoidPlaceHolder> delete(@RequestParam(value = "contractId") Integer contractId);

    /**
     * 根据合同编号查询合同详情
     * @param contractNumber
     * @return
     */
    @RequestMapping(value = "/detail-by-contract-number", method = RequestMethod.GET)
    FintechResponse<ContractVO> getContractDetailByContractNumber(@RequestParam(value = "contractNumber", required = false) String contractNumber);

    /**
     * 根据合同编号查询逾期合同
     * @param contractNumber
     * @return
     */
    @RequestMapping(value = "/get-overdue-contract-vo-by-contract-number", method = RequestMethod.GET)
    FintechResponse<ContractVO> getOverdueContractVOByContractNumber(@RequestParam(value = "contractNumber", required = false) String contractNumber);

    /**
     * 根据合同编号查询罚息率
     * @param contractNumber
     * @return
     */
    @RequestMapping(value = "/get-overdue-fine-rate-by-contract-number", method = RequestMethod.GET)
    FintechResponse<Double> getOverdueFineRateByContractNumber(String contractNumber);

    /**
     * 生成合同文件
     * @return
     */
    @RequestMapping(value = "/generate-contract-file-for-requsitions", method = RequestMethod.GET)
    FintechResponse<VoidPlaceHolder> generateContractFileForRequsitions();
}
