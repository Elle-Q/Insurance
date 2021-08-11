package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.biz.LoanVO;
import com.fintech.insurance.micro.dto.biz.RecordVO;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/18 17:34
 */
public interface LoanService {
    /**
     * 条件查询放款信息
     * @param requisitionNumber 业务单号
     * @param requisitionStatus 订单状态
     * @param productType 产品类型
     * @param channelName 渠道名称
     * @param customerName 客户名称
     * @return
     */
    Pagination<LoanVO> pageLoanInfo(String requisitionNumber, RequisitionStatus requisitionStatus, ProductType productType, String channelName, String customerName, Integer pageIndex, Integer pageSize);

    void recordLoan(RecordVO recordVO);

}
