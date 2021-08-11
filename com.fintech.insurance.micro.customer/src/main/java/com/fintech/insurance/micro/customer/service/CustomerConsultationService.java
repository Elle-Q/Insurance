package com.fintech.insurance.micro.customer.service;


import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.customer.CustomerConsultationVO;

import java.util.Date; /**
 * @Author: qxy
 * @Description:
 * @Date: 2017/11/20 13:53
 */
public interface CustomerConsultationService {
    /**
     * 分页查询
     * @param submmitStartTime
     * @param submmitEndTime
     * @param mobile
     * @param name
     * @return
     */
    Pagination<CustomerConsultationVO> query(Date submmitStartTime, Date submmitEndTime, String mobile, String name, Integer pageIndex, Integer pageSize);

    /**
     *  WX保存咨询信息
     * @param customerConsultationVO
     */
    Integer save(CustomerConsultationVO customerConsultationVO);
}
