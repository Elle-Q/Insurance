package com.fintech.insurance.micro.retrieval.persist;

import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.biz.BizReportVO;
import com.fintech.insurance.micro.retrieval.mapper.BizReportMapper;
import com.fintech.insurance.micro.retrieval.persist.base.BaseNativeSQLDao;

import java.util.Date;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/30 18:44
 */
public interface ReportQueryDao extends BaseNativeSQLDao<BizReportVO,BizReportMapper> {
    Pagination<BizReportVO> pageBizReportVO(String channelCode, String customerName, Integer companyId, Date startTime, Date endTime, String carNumber,
                                            ProductType type, ContractStatus contractStatus, Integer pageIndex, Integer pageSize);
}
