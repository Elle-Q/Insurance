package com.fintech.insurance.micro.biz.service.contract;

import com.fintech.insurance.commons.enums.ApplicationType;
import com.fintech.insurance.commons.enums.ContractSignUserType;
import com.fintech.insurance.commons.enums.RepayType;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.utils.*;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.biz.persist.entity.*;
import com.fintech.insurance.micro.biz.service.ContractService;
import com.fintech.insurance.micro.dto.customer.CustomerBankCardVO;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.finance.EnterpriseBankVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.dto.finance.PaymentOrderVO;
import com.fintech.insurance.micro.dto.finance.RepaymentPlanWordVO;
import com.fintech.insurance.micro.dto.thirdparty.ContractInfoResponseVO;
import com.fintech.insurance.micro.dto.thirdparty.ContractSignVO;
import com.fintech.insurance.micro.dto.thirdparty.SignLocationVO;
import com.fintech.insurance.micro.feign.finance.EnterpriseBankServiceFeign;
import com.fintech.insurance.micro.feign.finance.PaymentOrderServiceFeign;
import com.fintech.insurance.micro.feign.finance.RepaymentPlanServiceFeign;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 保单贷款合同生成
 * @Author: Yong Li
 * @Date: 2018/1/6 14:08
 */
@Service("dkContractGenerator")
public class DKContractGenerator extends AbstractContractGenerator {

    @Autowired
    private PaymentOrderServiceFeign paymentOrderServiceFeign;

    @Autowired
    private EnterpriseBankServiceFeign enterpriseBankServiceFeign;

    public ContractInfoResponseVO buildServiceContract(Integer contractId) {
        Contract contract = contractDao.getById(contractId);
        // 获取客户信息
        CustomerVO customerVO = getContractCustomer(contract);
        // 获取车辆信息
        RequisitionDetail requisitionDetail = getCarInfo(contract);

        Map<String, Object> valueMap = new HashMap<String, Object>();
        // 正式的合同号, 在用户支付服务单成功之后生成
        if (contract.getCustomerContractNumber() != null) {
            valueMap.put("contractNumber", contract.getCustomerContractNumber());
        } else {
            valueMap.put("contractNumber", "-");
        }
        valueMap.put("userName", customerVO.getName());
        valueMap.put("userIdentifier", customerVO.getIdNum());
        valueMap.put("userMobile", customerVO.getMobile());
        valueMap.put("carNum", requisitionDetail.getCarNumber());
        valueMap.put("insuranceFee", NumberFormatorUtils.convertFinanceNumberToShowString(Double.valueOf(requisitionDetail.getCommercialInsuranceAmount())));
        valueMap.put("borrowAmount", NumberFormatorUtils.convertFinanceNumberToShowString(contract.getContractAmount().doubleValue()));

        Product product = contract.getRequisition().getProduct();
        //第一期服务费率包括"其他"费用率
        valueMap.put("firstInstalRate", NumberFormatorUtils.convertFinanceNumberToShowString(product.getServiceFeeRate() + product.getOtherFeeRate()));
        valueMap.put("firstInstalFee", NumberFormatorUtils.convertFinanceNumberToShowString(
                new BigDecimal(product.getServiceFeeRate() + product.getOtherFeeRate()).divide(NumberFormatorUtils.TEN_THOUSAND).
                        multiply(contract.getContractAmount()).doubleValue()));
        valueMap.put("monthRate", 0.0);
        valueMap.put("monthFee", 0.0);
        FintechResponse<List<FinanceRepaymentPlanVO>> repaymentPlanResponse = refundServiceFeign.getListByContractNumber(contract.getContractNumber());
        if (!repaymentPlanResponse.isOk() && repaymentPlanResponse.getData() != null) {
            throw FInsuranceBaseException.buildFromErrorResponse(repaymentPlanResponse);
        }
        valueMap.put("repayDay", DateCommonUtils.getDayOfDate(repaymentPlanResponse.getData().get(0).getRepayDate()));
        Date submitDate = contract.getRequisition().getSubmissionDate() != null ? contract.getRequisition().getSubmissionDate() : new Date();
        valueMap.put("year", String.valueOf(DateCommonUtils.getYearOfDate(submitDate)));
        valueMap.put("month", DateCommonUtils.getMonthOfDate(submitDate));
        valueMap.put("day", DateCommonUtils.getDayOfDate(submitDate));
        valueMap.put("icon", iconImageStr);
        boolean isSign = StringUtils.isNoneBlank(contract.getCustomerContractNumber());
        return generateContractFileData(isSign, valueMap,"保单贷款服务合同模板.ftl", 4,  customerVO, true);
    }

    @Override
    public ContractInfoResponseVO buildBorrowContract(Integer contractId) {
        Contract contract = contractDao.getById(contractId);

        // 获取客户信息
        CustomerVO customerVO = getContractCustomer(contract);

        // 获取车辆信息
        RequisitionDetail requisitionDetail = getCarInfo(contract);

        Map<String, Object> valueMap = new HashMap<String, Object>();
        // 正式的合同号, 在用户支付服务单成功之后生成
        if (contract.getCustomerContractNumber() != null) {
            valueMap.put("contractNumber", contract.getCustomerContractNumber());
        } else {
            valueMap.put("contractNumber", "-");
        }
        valueMap.put("userName", customerVO.getName());
        //valueMap.put("userIdentifier", customerVO.getIdNum());
        valueMap.put("userMobile", customerVO.getMobile());
        valueMap.put("insuranceNumber", requisitionDetail.getCommercialInsuranceNumber());

        String loanAmount = NumberFormatorUtils.convertFinanceNumberToShowString(contract.getContractAmount().doubleValue());
        valueMap.put("loanAmount", loanAmount);
        valueMap.put("totalLoanAmount", loanAmount);
        valueMap.put("totalLoanAmountCap", NumberFormatorUtils.number2CNMontrayUnit(new BigDecimal(loanAmount)));

        String borrowAmountStr = NumberFormatorUtils.convertFinanceNumberToShowString(contract.getContractAmount().doubleValue());
        valueMap.put("borrowAmount", borrowAmountStr);
        valueMap.put("borrowAmountCap", NumberFormatorUtils.number2CNMontrayUnit(new BigDecimal(borrowAmountStr)));

        valueMap.put("userBankcardName", "-");
        valueMap.put("userBankcardNum", "-");
        if (StringUtils.isNotBlank(contract.getRequisition().getPaymentOrderNumber())) {//服务费支付单已经生成
            FintechResponse<PaymentOrderVO> paymentOrderVOFintechResp = paymentOrderServiceFeign.getByOrderNumber(contract.getRequisition().getPaymentOrderNumber());
            if (!paymentOrderVOFintechResp.isOk()) {
                throw FInsuranceBaseException.buildFromErrorResponse(paymentOrderVOFintechResp);
            }
            // 如果支付银行卡的信息有， 则覆盖
            PaymentOrderVO paymentOrder = paymentOrderVOFintechResp.getData();
            if (null != paymentOrder && StringUtils.isNotBlank(paymentOrder.getPaymentAccountNumer())) {
                FintechResponse<CustomerBankCardVO> bankcardResp = customerServiceFeign.getBankCardInfoByNumber(customerVO.getAccountId(), paymentOrder.getPaymentAccountNumer());
                if (bankcardResp.isOk() && bankcardResp.getData() != null) {
                    CustomerBankCardVO bankCardVO = bankcardResp.getData();
                    valueMap.put("userBankcardNum", bankCardVO.getAccountNumber());
                    // 根据银行代码获取银行名称
                    FintechResponse<EnterpriseBankVO> bankResp = enterpriseBankServiceFeign.getEnterpriseBank(ApplicationType.DEBT.getCode(), bankCardVO.getAccountBank());
                    if (bankResp.isOk() && null != bankResp.getData()) {
                        valueMap.put("userBankcardName", bankResp.getData().getBankName());
                    }
                }
            }
        }

        valueMap.put("totalInstalment", contract.getBusinessDuration());

        Product product = contract.getRequisition().getProduct();
        ProductRate monthRate = productRateDao.getProductRateByProductIdAndDuration(product.getId(), contract.getBusinessDuration());
        valueMap.put("monthRate", NumberFormatorUtils.convertFinanceNumberToShowString(monthRate.getInterestRate()));
        FintechResponse<List<FinanceRepaymentPlanVO>> repaymentPlanResponse = refundServiceFeign.getListByContractNumber(contract.getContractNumber());
        if (!repaymentPlanResponse.isOk() && repaymentPlanResponse.getData() != null) {
            throw FInsuranceBaseException.buildFromErrorResponse(repaymentPlanResponse);
        }
        valueMap.put("repayDay", DateCommonUtils.getDayOfDate(repaymentPlanResponse.getData().get(0).getRepayDate()));
        // 逾期滞纳每天利率
        valueMap.put("overdueFeeRate", NumberFormatorUtils.convertFinanceNumberToShowString(product.getOverdueFineRate()));
        //保证金
        BigDecimal assureMoney = CalculationFormulaUtils.getAssureMoney(contract.getContractAmount(), contract.getBusinessDuration());

        String assuranceMoneyStr = NumberFormatorUtils.convertFinanceNumberToShowString(assureMoney.doubleValue());
        valueMap.put("securityAmount", assuranceMoneyStr);
        valueMap.put("securityAmountCap", NumberFormatorUtils.number2CNMontrayUnit(new BigDecimal(assuranceMoneyStr)));
        Date submitDate = contract.getRequisition().getSubmissionDate() != null ? contract.getRequisition().getSubmissionDate() : new Date();
        valueMap.put("year", String.valueOf(DateCommonUtils.getYearOfDate(submitDate)));
        valueMap.put("month", DateCommonUtils.getMonthOfDate(submitDate));
        valueMap.put("day", DateCommonUtils.getDayOfDate(submitDate));

        //还款方式
        valueMap.put("repayMethod", RepayType.codeOf(product.getRepayType()).getDesc());

        // 设置还款计划
        valueMap.put("repaymentPlans", buildContractRepaymentPlans(contract.getContractNumber(), contract.getContractAmount()));

        valueMap.put("icon", iconImageStr);

        boolean isSign = StringUtils.isNoneBlank(contract.getCustomerContractNumber());
        return generateContractFileData(isSign, valueMap,
                "保单贷款借款合同模板.ftl", 8,  customerVO, false);
        /*Map<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("contractNumber", "test");
        valueMap.put("userName", "test");
        //valueMap.put("userIdentifier", "test");
        valueMap.put("userMobile", "test");
        valueMap.put("carNum", "test");

        valueMap.put("insuranceNumber", "P001");

        valueMap.put("loanAmount", "100.23");
        valueMap.put("totalLoanAmount", "100.23");
        valueMap.put("totalLoanAmountCap", "testFDSFD");

        valueMap.put("monthRate", "0.36");
        valueMap.put("repayDay", "12");

        valueMap.put("totalInstalment", "5");

        valueMap.put("userBankcardName", "-");
        valueMap.put("userBankcardNum", "-");

        valueMap.put("repayMethod", "AAAAAAAA");
        valueMap.put("overdueFeeRate", "100.99");

        Date reportDate = new Date();
        valueMap.put("year", "2014");
        valueMap.put("month", "05");
        valueMap.put("day", "2");

        // 设置还款计划
        List<RepaymentPlanWordVO> plans = new ArrayList<RepaymentPlanWordVO>();
        plans.add(new RepaymentPlanWordVO("2", "2017-02-02", "300.36", "20.3", "562.3", "100.32"));
        plans.add(new RepaymentPlanWordVO("3", "2017-03-02", "300.36", "20.3", "562.3", "100.32"));

        valueMap.put("repaymentPlans", plans);
        valueMap.put("icon", iconImageStr);

        CustomerVO customerVO = new CustomerVO();
        customerVO.setMobile("13902443572");

        try {
            WordUtil.createWordFile(valueMap, "保单贷款借款合同模板.ftl", new File("serviceContract.doc"), ContractService.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return null;*/
    }

    @Override
    protected ContractSignVO buildBorrowContractSignInfo(String userAccountId, String bestsignFileId) {
        ContractSignVO signVO = new ContractSignVO();
        signVO.setUserAccountId(userAccountId);
        signVO.setContractFileNum(bestsignFileId);
        signVO.setTitle("保单贷款借款合同");
        signVO.setDescription("保单贷款借款合同");
        signVO.setContractSignUserType(ContractSignUserType.LENDER);

        signVO.getUserSignLocations().add(new SignLocationVO(7, 0.64d, 0.43d));
        signVO.getEnterpriseSignLocations().add(new SignLocationVO(7, 0.64d, 0.24d));

        return signVO;
    }

    @Override
    protected ContractSignVO buildServiceContractSignInfo(String userAccountId, String bestsignFileId) {
        ContractSignVO signVO = new ContractSignVO();
        signVO.setUserAccountId(userAccountId);
        signVO.setContractFileNum(bestsignFileId);
        signVO.setTitle("保单贷款服务合同");
        signVO.setDescription("保单贷款服务合同");

        //"pageNum":4, "xaxis": 0.25, "yaxis": "0.787"
        signVO.getUserSignLocations().add(new SignLocationVO(4, 0.23d, 0.82d));
        //"pageNum":4, "xaxis": 0.6, "yaxis": "0.79"}
        signVO.getEnterpriseSignLocations().add(new SignLocationVO(4, 0.6d, 0.82d));

        return signVO;
    }
}
