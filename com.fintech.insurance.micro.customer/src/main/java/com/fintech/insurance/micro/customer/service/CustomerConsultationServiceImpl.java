package com.fintech.insurance.micro.customer.service;

import com.fintech.insurance.commons.enums.ProcessStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.customer.persist.dao.CustomerConsultationDao;
import com.fintech.insurance.micro.customer.persist.entity.CustomerConsultation;
import com.fintech.insurance.micro.dto.customer.CustomerConsultationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CustomerConsultationServiceImpl implements CustomerConsultationService{

    @Autowired
    private CustomerConsultationDao customerConsultationDao;

    @Override
    @Transactional(readOnly = true)
    public Pagination<CustomerConsultationVO> query(Date submmitStartTime, Date submmitEndTime, String mobile, String name, Integer pageIndex, Integer pageSize) {
        Page<CustomerConsultation> customerConsultationPage = customerConsultationDao.query(submmitStartTime, submmitEndTime, mobile, name, pageIndex, pageSize);
        List<CustomerConsultationVO> customerConsultationVOS = new ArrayList<>();
        List<CustomerConsultation> customerConsultationList = customerConsultationPage.getContent();
        if (null != customerConsultationList && customerConsultationList.size() > 0) {
            for (CustomerConsultation c : customerConsultationList) {
                CustomerConsultationVO vo = this.entityToVO(c);
                customerConsultationVOS.add(vo);
            }
        }
        return Pagination.createInstance(pageIndex, pageSize, customerConsultationPage.getTotalElements(), customerConsultationVOS);
    }

    @Override
    public Integer save(CustomerConsultationVO customerConsultationVO) {
        CustomerConsultation customerConsultation = new CustomerConsultation();
        /*CustomerConsultation preCustomerConsultation = customerConsultationDao.getByOauthTypeAndOauthAppidAndOauthAcount(
                customerConsultationVO.getOauthAppId(), customerConsultationVO.getOauthAccount(), customerConsultationVO.getOauthType());
        if (null != preCustomerConsultation) {//检查该微信用户是否之前提交过咨询信息
            customerConsultation = preCustomerConsultation;
            customerConsultation.setUpdateAt(new Date());
        }*/
        customerConsultation.setCreateAt(new Date());
        customerConsultation.setCustomerMobile(customerConsultationVO.getMobile());
        customerConsultation.setCustomerName(customerConsultationVO.getName());
        customerConsultation.setConsultContent(customerConsultationVO.getBorrowReason());
        customerConsultation.setProcessStatus(ProcessStatus.PENDING.getCode());
        customerConsultation.setOauthType(null == customerConsultationVO.getOauthType() ? "" : customerConsultationVO.getOauthType());
        customerConsultation.setOauthAppId(customerConsultationVO.getOauthAppId() == null ? "" : customerConsultationVO.getOauthAppId());
        customerConsultation.setOauthAcount(customerConsultationVO.getOauthAccount() == null ? "" : customerConsultationVO.getOauthAccount());
        customerConsultation.setWxUnionid(customerConsultationVO.getWxUnioinId() == null ? "" : customerConsultationVO.getWxUnioinId());
        customerConsultationDao.save(customerConsultation);
        return customerConsultation.getId();
    }

    private CustomerConsultationVO entityToVO(CustomerConsultation c) {
        CustomerConsultationVO vo = new CustomerConsultationVO();
        vo.setMobile(c.getCustomerMobile());
        vo.setName(c.getCustomerName());
        vo.setId(c.getId());
        vo.setSubmmitTime(c.getCreateAt());
        vo.setBorrowReason(c.getConsultContent());
        return vo;
    }
}
