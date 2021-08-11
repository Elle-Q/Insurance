package com.fintech.insurance.service.agg.impl;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.dto.support.BranchVO;
import com.fintech.insurance.micro.dto.support.InsuranceCompanyVO;
import com.fintech.insurance.micro.feign.support.InsuranceCompanyConfigServiceFeign;
import com.fintech.insurance.micro.vo.wechat.WeChatInsuranceBranchVO;
import com.fintech.insurance.micro.vo.wechat.WeChatInsuranceCompanyVO;
import com.fintech.insurance.service.agg.CommonInsuranceCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Description: (some words)
 * @Author: Administrator
 * @Date: 2017/12/11 0011 16:12
 */
@Service
public class CommonInsuranceCompanyServiceImpl implements CommonInsuranceCompanyService {


    @Autowired
    private InsuranceCompanyConfigServiceFeign companyConfigServiceFeign;

    @Override
    public List<WeChatInsuranceCompanyVO> queryAllInsuranceCompany() {
       FintechResponse<List<InsuranceCompanyVO>>  listFintechResponse = companyConfigServiceFeign.listAllInsuranceCompany();
       if(!listFintechResponse.isOk()){
           throw new FInsuranceBaseException(listFintechResponse.getCode());
       }
       List<WeChatInsuranceCompanyVO> companyVOList = new ArrayList<WeChatInsuranceCompanyVO>();
       if (listFintechResponse != null && listFintechResponse.getData() != null){
           for(InsuranceCompanyVO insuranceCompanyVO : listFintechResponse.getData()){
               WeChatInsuranceCompanyVO companyVO = new WeChatInsuranceCompanyVO();
               companyVO.setCompanyId(insuranceCompanyVO.getId());
               companyVO.setCompanyName(insuranceCompanyVO.getCompanyName());
               Set<BranchVO> branches = insuranceCompanyVO.getBranches();
               setInsuranceBranch(companyVO, branches);
               companyVOList.add(companyVO);
           }
       }
       return companyVOList;
    }

    //赋值公司支部
    private void setInsuranceBranch(WeChatInsuranceCompanyVO companyVO , Set<BranchVO> branches){
        if(branches != null){
            List<WeChatInsuranceBranchVO> branchVOList = new ArrayList<WeChatInsuranceBranchVO>();
            for(BranchVO branchVO : branches){
                WeChatInsuranceBranchVO insuranceBranchVO = new WeChatInsuranceBranchVO();
                insuranceBranchVO.setBranchId(branchVO.getId());
                insuranceBranchVO.setBranchName(branchVO.getBranchName());
                branchVOList.add(insuranceBranchVO);
            }
            companyVO.setInsuranceBranchVOList(branchVOList);
        }
    }
}
