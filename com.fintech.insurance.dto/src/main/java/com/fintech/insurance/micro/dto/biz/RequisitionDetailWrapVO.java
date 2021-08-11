package com.fintech.insurance.micro.dto.biz;

import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.support.BranchVO;

import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/12 18:22
 */
public class RequisitionDetailWrapVO {

    private RequisitionVO requisitionVO;

    private CustomerVO customerVO;

    private BranchVO branchVO;

    private List<DurationVO> durationVOList;

    public RequisitionVO getRequisitionVO() {
        return requisitionVO;
    }

    public void setRequisitionVO(RequisitionVO requisitionVO) {
        this.requisitionVO = requisitionVO;
    }

    public CustomerVO getCustomerVO() {
        return customerVO;
    }

    public void setCustomerVO(CustomerVO customerVO) {
        this.customerVO = customerVO;
    }

    public BranchVO getBranchVO() {
        return branchVO;
    }

    public void setBranchVO(BranchVO branchVO) {
        this.branchVO = branchVO;
    }

    public List<DurationVO> getDurationVOList() {
        return durationVOList;
    }

    public void setDurationVOList(List<DurationVO> durationVOList) {
        this.durationVOList = durationVOList;
    }
}
