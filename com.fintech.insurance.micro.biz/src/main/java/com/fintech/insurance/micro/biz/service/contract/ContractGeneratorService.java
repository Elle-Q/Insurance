package com.fintech.insurance.micro.biz.service.contract;

import com.fintech.insurance.micro.dto.thirdparty.ContractInfoResponseVO;

/**
 * @Description: 根据合同模板生成合同文件服务接口
 * @Author: Yong Li
 * @Date: 2018/1/6 13:43
 */
public interface ContractGeneratorService {

    /**
     * 生成服务合同
     * @param contractId
     * @return
     */
    ContractInfoResponseVO buildServiceContract(Integer contractId);

    /**
     * 生成借款合同
     * @param contractId
     * @return
     */
    ContractInfoResponseVO buildBorrowContract(Integer contractId);
}
