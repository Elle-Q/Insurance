package com.fintech.insurance.micro.biz.persist.dao;

import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.micro.biz.persist.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Date;
import java.util.List;

/**
 * @Description: (合同管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
@NoRepositoryBean
public interface ContractComplexDao {

    /**
     *
     * @param contractCodes
     * @param beginDate
     * @param endDate
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Page<Contract> pageContract(List<String> contractCodes, ContractStatus contractStatus, Date beginDate, Date endDate, int pageIndex, int pageSize);

    /**
     *  查询待导出的合同数据
     * @param channelCode 渠道编号
     * @param customerIds 用户账号集合
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 产品类型
     * @param contractStatus 合同状态
     * @param pageIndex 页数
     * @param pageSize 每页数
     * @return
     */
    Page<Contract> findAllContract(String channelCode, List<Integer> customerIds, Date startTime, Date endTime, ProductType type,
                                          Integer companyId, ContractStatus contractStatus, Integer pageIndex, Integer pageSize);

    List<Contract> getByRequisitionNumberAndCustomerIds(String requisitionNumber, List<Integer> customerIds);


    /**
     * 查询该客户的所有合同
     * @param customerId
     * @param contractStatus
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Page<Contract> pageContractByCustomerIdAndContractStatus(Integer customerId, ContractStatus contractStatus, Integer pageIndex, Integer pageSize);

    Page<Contract> pageContractByNormalChannelUserIdAndContractStatus(Integer channelUserId, ContractStatus contractStatus, Integer pageIndex, Integer pageSize);

    //根据合同号查询合同
    List<Contract> findByContractNumbers(List<String> contractNumbers);

    // 查询合同编号前缀为指定值的合同后缀最大的合同号
    String getLargestContractNumberByNumberPrefix(String contractNumberPrefix);
}
