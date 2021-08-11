package com.fintech.insurance.micro.dto.biz;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Clayburn
 * @Description: 合同详情VO
 * @Date: 2017/11/10 14:02
 */
public class ContractDetailVO implements Serializable {

    // 合同ID
    private Integer contractId;
    // 客户姓名
    private String customerName;
    // 客户手机号码
    private String customerMobile;
    // 合同基本信息
    private ContractBaseDetailVO contractBaseDetailVO;
    // 产品信息
    private ProductVO productVO;
    // 还款信息
    private List<RefundVO> refundVOList;

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public ContractBaseDetailVO getContractBaseDetailVO() {
        return contractBaseDetailVO;
    }

    public void setContractBaseDetailVO(ContractBaseDetailVO contractBaseDetailVO) {
        this.contractBaseDetailVO = contractBaseDetailVO;
    }

    public ProductVO getProductVO() {
        return productVO;
    }

    public void setProductVO(ProductVO productVO) {
        this.productVO = productVO;
    }

    public List<RefundVO> getRefundVOList() {
        return refundVOList;
    }

    public void setRefundVOList(List<RefundVO> refundVOList) {
        if (refundVOList != null && refundVOList.size() > 0) {
            this.refundVOList = refundVOList;
        }
    }
}
