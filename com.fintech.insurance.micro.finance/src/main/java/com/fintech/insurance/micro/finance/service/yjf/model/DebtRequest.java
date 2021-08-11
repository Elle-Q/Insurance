package com.fintech.insurance.micro.finance.service.yjf.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fintech.insurance.commons.constants.YjfConstants;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.finance.service.yjf.YjfService;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description: 扣款请求对象
 * @Author: Yong Li
 * @Date: 2017/12/11 20:29
 */
@YjfService(name = YjfConstants.SERVICE_CODE_DEBT)
public class DebtRequest extends YjfRequest {

    /**
     * 商户扣款订单号
     */
    @JSONField(name = "merchOrderNo")
    private String platformOrderNum;

    /**
     * 交易场景: 默认选汽车分期
     *
     * APPLIANCES：3C分期
     * MOTOROLA：电摩分期
     * TOURISM：旅游分期
     * EDUCATION：教育分期
     * COSMETOLOGY：美容分期
     * RENTAL：租房分期
     * RENOVATION：装修分期
     * AUTOMOBILE：汽车分期
     * AGRICULTURE：农业分期
     * COMPREHENSIVE：综合性电商分期
     * CASH：现金贷
     */
    @JSONField(name = "transScenario")
    private String transScenario = "AUTOMOBILE";


    /**
     * 扣款金额，风控配置上限
     * 如: 88.88
     */
    @JSONField(name = "transAmount")
    private Double amount;

    /**
     * 扣款银行卡号，这里不支持支付账户ID。否则有划转其他账户资金风险
     */
    @JSONField(name = "bankCardNo")
    private String bankCardNo;

    /**
     * 客户姓名
     */
    @JSONField(name = "name")
    private String customerName;

    /**
     * 客户身份证号
     */
    @JSONField(name = "certNo")
    private String certNo;

    /**
     * 预留手机号（用于发送短信）
     */
    @JSONField(name="mobileNo")
    private String mobileNo;

    /**
     * "录入交易背景"的商户订单号 ，等同于"录入交易背景"接口中的merchOrderNo；
     *  这里传入商户合同号, 或者多个合同号（用于反查合同）
     */
    @JSONField(name = "backgroundMerchOrderNo")
    private String contractInfo;

    /**
     * 放款订单号
     * 当使用放款和代扣接口组合时传入
     */
    @JSONField(name = "loanMerchOrderNo")
    private String loanMerchOrderNo;

    public String getPlatformOrderNum() {
        return platformOrderNum;
    }

    public void setPlatformOrderNum(String platformOrderNum) {
        this.platformOrderNum = platformOrderNum;
    }

    public String getTransScenario() {
        return transScenario;
    }

    public void setTransScenario(String transScenario) {
        this.transScenario = transScenario;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getContractInfo() {
        return contractInfo;
    }

    public void setContractInfo(String contractInfo) {
        if (StringUtils.isNotEmpty(contractInfo) && contractInfo.length() >= 40) { //backgroundMerchOrderNo该字段不能传超过40个字符以上
            contractInfo = contractInfo.substring(0, 36) + "...";
        }
        this.contractInfo = contractInfo;
    }

    public String getLoanMerchOrderNo() {
        return loanMerchOrderNo;
    }

    public void setLoanMerchOrderNo(String loanMerchOrderNo) {
        this.loanMerchOrderNo = loanMerchOrderNo;
    }
}
