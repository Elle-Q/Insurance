package com.fintech.insurance.micro.retrieval.mapper;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/1 16:22
 */
public class InstalmentInfo {
    // 已完成期数
    private Integer completeInstalment;
    // 总期数
    private Integer totalInstalment;

    public Integer getCompleteInstalment() {
        return completeInstalment;
    }

    public void setCompleteInstalment(Integer completeInstalment) {
        this.completeInstalment = completeInstalment;
    }

    public Integer getTotalInstalment() {
        return totalInstalment;
    }

    public void setTotalInstalment(Integer totalInstalment) {
        this.totalInstalment = totalInstalment;
    }
}
