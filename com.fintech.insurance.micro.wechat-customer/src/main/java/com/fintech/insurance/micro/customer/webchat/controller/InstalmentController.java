package com.fintech.insurance.micro.customer.webchat.controller;

import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.annotation.RequireWechatLogin;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechWechatController;
import com.fintech.insurance.micro.dto.biz.ContractVO;
import com.fintech.insurance.micro.dto.biz.InstalmentDetailVO;
import com.fintech.insurance.micro.dto.biz.RequisitionInfoVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.feign.biz.ContractServiceFeign;
import com.fintech.insurance.micro.feign.biz.RequisitionServiceFeign;
import com.fintech.insurance.micro.feign.finance.RefundServiceFeign;
import com.fintech.insurance.micro.vo.wechat.InstalmentVO;
import com.fintech.insurance.micro.vo.wechat.RepaymentPlanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/9 15:15
 */
@RestController
@RequestMapping(value = "/wechat/customer/instalment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequireWechatLogin
public class InstalmentController extends BaseFintechWechatController {

    @Autowired
    private ContractServiceFeign contractServiceFeign;

    @Autowired
    private RefundServiceFeign refundServiceFeign;

    @Autowired
    private RequisitionServiceFeign requisitionServiceFeign;

    @GetMapping(value = "/page")
    public FintechResponse<Pagination<InstalmentVO>> pageInstalment(@RequestParam(value = "contractStatus", defaultValue = "") String contractStatus,
                                                                    @RequestParam(value = "pageIndex", defaultValue = BasicConstants.DEFAULT_PAGE_INDEX) Integer pageIndex,
                                                                    @RequestParam(value = "pageSize", defaultValue = BasicConstants.DEFAULT_PAGE_SIZE) Integer pageSize) {

        FintechResponse<Pagination<ContractVO>> response = contractServiceFeign.pageWechatContract(contractStatus, pageIndex, pageSize);
        if (response == null || response.getData() == null) {
            throw new FInsuranceBaseException(107004);
        }
        Pagination<ContractVO> tempPage = response.getData();
        Pagination<InstalmentVO> resultPage = Pagination.createInstance(pageIndex, pageSize, tempPage.getTotalRowsCount(), convertList(tempPage.getItems()));

        return FintechResponse.responseData(resultPage);
    }

    @GetMapping(value = "/detail")
    public FintechResponse<InstalmentDetailVO> getInstalmentDetailByContractId(@RequestParam(value = "contractId") Integer contractId) {
        FintechResponse<InstalmentDetailVO> response = contractServiceFeign.getWeChatContractDetailByContractId(contractId);
        if (response == null || response.getData() == null) {
            throw new FInsuranceBaseException(107005);
        }
        return response;
    }

    @GetMapping(value = "/detail-plan")
    public FintechResponse<List<RepaymentPlanVO>> getRepaymentPlan(@RequestParam(value = "contractNumber") String contractNumber) {
        FintechResponse<List<FinanceRepaymentPlanVO>> response = refundServiceFeign.getListByContractNumber(contractNumber);
        if (!response.isOk() || response == null || response.getData() == null) {
            throw new FInsuranceBaseException(107006);
        }
        List<FinanceRepaymentPlanVO> tempList = response.getData();
        return FintechResponse.responseData(convertPlanVOList(tempList));
    }

    @GetMapping(value = "/requisition-info")
    public FintechResponse<RequisitionInfoVO> getWeChatRequisitionDetail(@RequestParam(value = "requisitionId")Integer requisitionId) {
        FintechResponse<RequisitionInfoVO> response = requisitionServiceFeign.getWeChatRequistionDetailVO(requisitionId);
        if(!response.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(response);
        }
        if (response.getData() == null) {
            throw new FInsuranceBaseException(107007);
        }
        return response;
    }

    private List<InstalmentVO> convertList(List<ContractVO> vos) {
        if (vos == null) {
            return Collections.EMPTY_LIST;
        }
        List<InstalmentVO> resultList = new ArrayList<>();
        for (ContractVO vo : vos) {
            resultList.add(convertContractVOToInstalmentVO(vo));
        }

        return resultList;
    }

    private InstalmentVO convertContractVOToInstalmentVO(ContractVO contractVO) {
        if (contractVO == null) {
            return null;
        }
        InstalmentVO vo = new InstalmentVO();
        vo.setContractId(contractVO.getContractId());
        vo.setContractNumber(contractVO.getContractCode());
        vo.setChannelName(contractVO.getChannelName());
        vo.setRefundStatus(contractVO.getRefundStatus());
        vo.setTotalInstalment(contractVO.getTotalPhase());
        vo.setCurrentInstalment(contractVO.getRefundPhase());
        vo.setRepayTotalAmount(contractVO.getRepayTotalAmount());
        vo.setRepayDate(contractVO.getRepayDate());
        vo.setOverdueFlag(contractVO.getOverdueFlag());
        vo.setOverdueDays(Integer.parseInt(contractVO.getOverdueDays() + ""));
        vo.setCarNumber(contractVO.getCarNumber());
        return vo;
    }

    private List<RepaymentPlanVO> convertPlanVOList(List<FinanceRepaymentPlanVO> planVOList) {
        if (planVOList == null) {
            return Collections.EMPTY_LIST;
        }
        List<RepaymentPlanVO> resultList = new ArrayList<>();
        for (FinanceRepaymentPlanVO repaymentPlanVO : planVOList) {
            resultList.add(convertVOToVO(repaymentPlanVO));
        }
        return resultList;
    }

    private RepaymentPlanVO convertVOToVO(FinanceRepaymentPlanVO planVO) {
        if (planVO == null) {
            return null;
        }
        RepaymentPlanVO vo = new RepaymentPlanVO();
        vo.setRepaymentPlanId(planVO.getId());
        vo.setRefundStatus(planVO.getRepayStatus().getCode());
        vo.setTotalInstalment(planVO.getTotalInstalment());
        vo.setCurrentInstalment(planVO.getCurrentInstalment());
        vo.setRepayTotalAmount(planVO.getRepayTotalAmount());
        vo.setRepayInterestAmount(planVO.getRepayInterestAmount());
        vo.setRepayDate(planVO.getRepayDate());
        vo.setOverdueFlag(planVO.getOverdueFlag());
        vo.setOverdueDays(planVO.getOverdueDays());
        vo.setRepayOverdueAmount(planVO.getOverdueFine());
        return vo;
    }
}
