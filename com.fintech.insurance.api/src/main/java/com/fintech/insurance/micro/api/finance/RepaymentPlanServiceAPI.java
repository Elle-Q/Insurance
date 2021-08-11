package com.fintech.insurance.micro.api.finance;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.dto.finance.OverdueDataVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/16 0016 20:15
 */
@RequestMapping(value = "/finance/repayment-plan", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface RepaymentPlanServiceAPI {

    /**
     * 获取还款计划
     *
     * @param repaymentPlanId 还款计划ID
     * @return
     */
    @RequestMapping(path = "/get-repayment-plan")
    FintechResponse<FinanceRepaymentPlanVO> getRepaymentPlan(@RequestParam(name = "repaymentPlanId", required = true) Integer repaymentPlanId);

    /**
     * 获取还款计划的合同号集合
     *
     * @param contractStatus 还款状态
     * @return
     */
    @RequestMapping(path = "/find-repayment-plan")
    FintechResponse<Map<String,FinanceRepaymentPlanVO>> findAllRepaymentPlanByContractStatus(ContractStatus contractStatus);

    /**
     * 查询客户还款计划
     * @param customeId     客户id
     * @param days          查询最近天数
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(path = "/page-repayment-plan-by-customeid", method = RequestMethod.GET)
    FintechResponse<Pagination<FinanceRepaymentPlanVO>> pageRepaymentPlanByCustomeId(@RequestParam(value = "customeId") Integer customeId,
                                                                                     @RequestParam(value = "days", defaultValue = "") Integer days,
                                                                                     @RequestParam(value = "pageIndex") Integer pageIndex,
                                                                                     @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * 确认合同退保操作：
     * Note: 之所以放在Finance模块有两个原因： 1. 确认退保应该属于财务（Finance）方面的业务； 2. 涉及到还款计划的状态更新，
     * 为便于管理，统一先更新还款计划的状态，然后由还款计划状态的变化触发合同状态的更新
     * @param contractNumber 合同编号
     */
    @RequestMapping(value = "/confirm-return", method = RequestMethod.GET)
    FintechResponse<VoidPlaceHolder> confirmReturnInsurance(@RequestParam(value="contractNumber") String contractNumber);

    /**
     * 创建还款计划
     * @param repaymentPlanVO 还款计划集合
     */
    @RequestMapping(value = "/create_init_repayment_plan", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> createInitRepaymentPlan(@RequestBody FinanceRepaymentPlanVO repaymentPlanVO);

    /**
     * 删除还款计划
     * @param contractNumberStr 合同号集合
     */
    @RequestMapping(value = "/delete_repayment_plan_by_contract_number", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> deleteRepaymentPlanByContractNumbers(@RequestBody List<String> contractNumberStr);

    /**
     * 删除还款计划
     * @param contractCode 合同号
     */
    @RequestMapping(value = "/delete_by_contract_number", method = RequestMethod.GET)
    FintechResponse<VoidPlaceHolder> deleteRepaymentPlanByContractNumber(@RequestParam(value = "contractCode") String contractCode);

    /**
     * 期初还款更新第一期还款计划
     * @param idVO  业务单id
     * @return
     */
    @RequestMapping(value = "/update_by_initial_payment_success", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> updateFirstRepaymentPlanBInitialPaymentSuccess(@RequestBody IdVO idVO);

    @RequestMapping(value = "get_overdue_data_by_repayment_plan", method = RequestMethod.POST)
    FintechResponse<OverdueDataVO> getOverdueDataVOByRepaymentPlanVO(@RequestBody FinanceRepaymentPlanVO repaymentPlanVO);

    /**
     * 查询已逾期的还款计划
     * @param code
     * @return
     */
    //FintechResponse<List<RepaymentPlanVO>> listRepaymentPlanByStatus(String code);
}