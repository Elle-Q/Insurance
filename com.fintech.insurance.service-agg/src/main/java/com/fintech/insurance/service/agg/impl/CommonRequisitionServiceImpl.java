package com.fintech.insurance.service.agg.impl;

import com.fintech.insurance.commons.enums.LoanAccountType;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.dto.biz.CustomerRequisitionVO;
import com.fintech.insurance.micro.dto.biz.SimpleRequisitionDetailVO;
import com.fintech.insurance.micro.dto.customer.CustomerLoanBankVO;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.thirdparty.ImageVO;
import com.fintech.insurance.micro.feign.biz.RequisitionServiceFeign;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.support.InsuranceCompanyConfigServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import com.fintech.insurance.micro.vo.wechat.WeChatCustomerRequisitionVO;
import com.fintech.insurance.micro.vo.wechat.WeChatSimpleRequisitionDetailVO;
import com.fintech.insurance.service.agg.CommonRequisitionService;
import com.fintech.insurance.service.agg.ThirdPartyService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Administrator
 * @Date: 2017/12/11 0011 16:12
 */
@Service
public class CommonRequisitionServiceImpl implements CommonRequisitionService {

    private static final Logger logger = LoggerFactory.getLogger(CommonRequisitionServiceImpl.class);

    @Autowired
    private RequisitionServiceFeign requisitionServiceFeign;

    @Autowired
    private InsuranceCompanyConfigServiceFeign companyConfigServiceFeign;

    @Autowired
    private CustomerServiceFeign customerServiceFeign;



    public FintechResponse<WeChatCustomerRequisitionVO> getCustomerRequisitionVOById(Integer id) {
        FintechResponse<CustomerRequisitionVO> requisitionVOFintechResponse = requisitionServiceFeign.getCustomerRequisitionVOById(id);
        if(!requisitionVOFintechResponse.isOk()){
            throw FInsuranceBaseException.buildFromErrorResponse(requisitionVOFintechResponse);
        }
        CustomerRequisitionVO customerRequisitionVO = requisitionVOFintechResponse.getData();
        FintechResponse<CustomerVO> customerVOFintechResponse = customerServiceFeign.getCustomerAccountInfoById(customerRequisitionVO.getCustomerAccountInfoId());
        CustomerVO customerVO = null;
        if(!customerVOFintechResponse.isOk()){
           throw FInsuranceBaseException.buildFromErrorResponse(customerVOFintechResponse);
        }
        customerVO = customerVOFintechResponse.getData();
        if(customerVO == null){
            customerVO = new CustomerVO();
        }
        WeChatCustomerRequisitionVO requisitionVO = new WeChatCustomerRequisitionVO();
        requisitionVO.setCustomerId(customerVO.getAccountId());
        requisitionVO.setCustomerAccountInfoId(customerVO.getAccountInfoId());
        //??????id
        requisitionVO.setId(id);
        //????????????
        requisitionVO.setCustomerName(customerVO.getName());
        //??????????????????
        requisitionVO.setCustomerMobile(customerVO.getMobile());
        //????????????
        requisitionVO.setCompanyName(customerVO.getCompanyOf());
        //???????????????
        requisitionVO.setIdNumber(customerVO.getIdNum());
        //????????????
        requisitionVO.setOtherResourceList(customerRequisitionVO.getOtherResourceList());

        requisitionVO.setIdCardEvidence(customerRequisitionVO.getIdCardEvidence());
        //??????code
        requisitionVO.setChannelCode(customerRequisitionVO.getChannelCode());
        //??????id
        requisitionVO.setProductId(customerRequisitionVO.getProductId());
        //????????????id
        requisitionVO.setChannelUserId(customerRequisitionVO.getChannelUserId());
        CustomerLoanBankVO customerLoanBankVO = customerRequisitionVO.getCustomerLoanBankVO();
        if(StringUtils.isNoneBlank(customerLoanBankVO.getLoanAccountNumber()) && !StringUtils.equals(customerLoanBankVO.getLoanAccountType(), LoanAccountType.PERSONAL_LOAN_TYPE.getCode())) {
            CustomerLoanBankVO bankVO = companyConfigServiceFeign.getByAccountNumberAndType(customerLoanBankVO.getLoanAccountNumber(), customerLoanBankVO.getLoanAccountType());
            requisitionVO.setCompanyId(bankVO == null ? null : bankVO.getCompanyId());
            requisitionVO.setBranchId(bankVO == null ? null : bankVO.getBranchId());
        }
        requisitionVO.setProductType(customerLoanBankVO.getLoanAccountType());
        return FintechResponse.responseData(requisitionVO);
    }

    @Override
    public List<WeChatSimpleRequisitionDetailVO> pageRequisitionDetailByRequisitionId(Integer requisitionId, Integer pageIndex, Integer pageSize) {
        pageIndex = 1;
        pageSize = Integer.MAX_VALUE;
        FintechResponse<Pagination<SimpleRequisitionDetailVO>> detailVOPaginationFintechResponse = requisitionServiceFeign.pageRequisitionDetailByRequisitionId(requisitionId, pageIndex, pageSize);
        if(!detailVOPaginationFintechResponse.isOk()){
            throw new FInsuranceBaseException(detailVOPaginationFintechResponse.getCode());
        }
        Pagination<SimpleRequisitionDetailVO> detailVOPagination = detailVOPaginationFintechResponse.getData();
        List<WeChatSimpleRequisitionDetailVO> list = new ArrayList<WeChatSimpleRequisitionDetailVO>();
        for(SimpleRequisitionDetailVO vo : detailVOPagination.getItems()){
            WeChatSimpleRequisitionDetailVO detailVO = new WeChatSimpleRequisitionDetailVO();
            detailVO.setCarNumber(vo.getCarNumber());
            detailVO.setId(vo.getId());
            detailVO.setInsuranceNumber(vo.getInsuranceNumber());
            detailVO.setIsCanUse(vo.getIsCanUse());
            list.add(detailVO);
        }
        return list;
    }
}
