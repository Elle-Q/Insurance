package com.fintech.insurance.micro.dto.biz;


import com.fintech.insurance.commons.enums.ProductType;

import java.io.Serializable;

/**
 * @Author: Man LIU
 * @Description: 合同文件请求参数vo
 * @Date: 2017/11/9 18:22
 */
public class SaveContractRespondVO implements Serializable {

    //合同信息vo
    private ContractInfoVO contractInfoVO;

    //合同文件请求vo
    private ContractFileRequestVO contractFileRequestVO;

    public ContractInfoVO getContractInfoVO() {
        return contractInfoVO;
    }

    public void setContractInfoVO(ContractInfoVO contractInfoVO) {
        this.contractInfoVO = contractInfoVO;
    }

    public ContractFileRequestVO getContractFileRequestVO() {
        return contractFileRequestVO;
    }

    public void setContractFileRequestVO(ContractFileRequestVO contractFileRequestVO) {
        this.contractFileRequestVO = contractFileRequestVO;
    }
}