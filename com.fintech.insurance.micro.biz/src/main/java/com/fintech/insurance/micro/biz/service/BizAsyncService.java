package com.fintech.insurance.micro.biz.service;

/**
 * @Description: 异步服务接口： 对于一些需要较长时间的非关键性业务服务， 可以写在这里， 交由Spring异步完成
 * @Author: Yong Li
 * @Date: 2018/1/15 15:50
 */
public interface BizAsyncService {

    /**
     * 生成合同文件， 并择机对合同文件践行签署
     *  @param requsitionId 申请单ID
     */
    void generateContractFileForRequsition(Integer requsitionId);

}
