package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.biz.persist.entity.Contract;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import com.fintech.insurance.micro.dto.biz.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: (合同管理)
 * @Author: yongNeng Liu
 * @Date: 2017/11/13 10:28
 */
public interface ContractService {

    public ContractVO getContractById(Integer id);

    /**
     * 根据合同编号查询合同详情
     *
     * @param contractCode   合同编号
     * @param contractStatus 合同状态
     * @param channelName    渠道名称
     * @param customerName   客户名称
     * @param loadBeginDate  放款开始时间
     * @param loadEndDate    放款结束时间
     * @param pageIndex
     * @param pageSize
     * @return
     * @params contractNumber 合同号
     */
    Pagination<ContractVO> pageContract(String contractCode, ContractStatus contractStatus, String channelName, String customerName,
                                        String carNumber, Date loadBeginDate, Date loadEndDate, int pageIndex, int pageSize);

    ContractDetailVO getDetailByContractNumber(String contractCode);

    /**
     * 通过订单号获取所有的合同编号
     * @param requisitionNumber
     * @return
     */
    List<ContractVO> getContractByRequisitionNumberAndCustomerName(String requisitionNumber, String customerName);

    RequisitionVO getRequisitionByContractNumber(String contractNumber);

    /**
     *  查询待导出的合同数据
     * @param voList 查询出来的业务数据数据转
     * @param type 产品类型
     * @param status 产品状态
     * @return
     */
    List<Map<String,Object>> convertListToMap(List<BizReportVO> voList, ProductType type, ContractStatus status);

    /**
     *  查询待导出的合同数据
     * @param channelCode 渠道编号
     * @param customerIds 用户账号集合
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 产品类型
     * @param status 合同状态
     * @param pageIndex 页数
     * @param pageSize 每页数
     * @return
     */
    Pagination<BizReportVO> queryAllBizReportContract(String channelCode, List<Integer> customerIds, Date startTime, Date endTime, ProductType type,
                                                      Integer companyId, ContractStatus status, Integer pageIndex, Integer pageSize);

    /**
     * 分页查询该客户下的所有合同
     * @param contractStatus
     * @return
     */
    Pagination<ContractVO> pageContractVOByContractStatus(ContractStatus contractStatus, Integer pageIndex, Integer pageSize);

    /**
     * 微信端分期详情
     * @param contractId
     * @return
     */
    InstalmentDetailVO getWeChatContractDetailByContractId(Integer contractId);

    /**
     * 创建合同信息
     * @param requisition 业务申请单
     * @param requisitionDetail 车辆信息
     * @param month 创建贷款
     * @return
     */
    public  Contract createContract(Requisition requisition, RequisitionDetail requisitionDetail, Integer month);

    /**
     * 获取合同详情
     * @param requisition 业务单
     * @param detailList 合同集合
     * @return
     */
    ContractInfoVO buildContractInfoVO(Requisition requisition, List<RequisitionDetail> detailList);

    //保存合同
    Integer saveContract(Contract contract);

    /**
     * 更新指定的合同<code>contractNumber</code>到<code>targetStatus</code>
     * @param contractNumber 合同编号
     * @param targetStatus 目标状态
     */
    void updateContractStatus(String contractNumber, ContractStatus targetStatus);

    Pagination<ContractSimpleDetail> pageWechatContractByRequisitionId(Integer requisitionId, Integer pageIndex, Integer pageSize);

    /**
     *  WX根据申请单id查询所有合同信息
     * @param id  申请单id
     * @return
     */
    List<ContractVO> getContractByRequisitionId(Integer id);


    /**
     *  WX根据申请单id删除所有合同信息
     * @param requisitionId  申请单id
     * @return
     */
    void cleanContractDataByRequisitionId(Integer requisitionId);

    //获取合同文件
    String getRequisitionContractFile(Integer contractId, boolean isServiceContract, Boolean isPictureNeeded);

    //生成合同号，并根据指定参数清空合同文件
    void generateContractInfoOfRequsition(Integer requsitionId, boolean isAllFilesRegenrate, boolean isGenerateContractNumber);

    //生成合同文件
    void generateContractFileOfRequsition(Integer requsitionId);

    //把合同变成待退保
    void changeStatusToSurrenderByContractNumber(List<String> contractNumbers);

    /**
     * 生成合同编号
     *
     *
     合同编号生成规则：产品编码（4位）+分期（2位）+渠道编码（3位）+年月（4位）+流水编号（4位）；

     1、产品编码： CX：车险分期，DK:保单贷款；产品编码的两位用34进制生成；

     范围（CX01~CXZZ，DK01~DKZZ)

     2、分期：（03对应3个月分期，04代表4个月分期，05代表5个月分期,06代表6个月分期，07代表7个月分期，

     08代表8个月分期，09代表9个月分期，10代表10个月分期，12代表12个月分期）

     3、渠道编码：系统自动生成3位

     4、年月：1711（2017年11月）

     5、流水码：根据产品编码、分期和渠道编码组合唯一性进行流水编码；


     举例： CX01  03  001 17110001      车险分期01号产品，3个月分期的合同，属于001 渠道，2017年(以年为单位)创建的  0001号合同；
     *
     * @param productCode 产品代码
     * @param channelCode 渠道代码
     * @param contractInstalmentNum 合同分期数
     * @param generateContractDate 生成合同日期， 可以指定具体的日期， 如果不指定，则使用系统当前日期
     * @return
     */
    String generateContractSerialNum(String productCode, String channelCode, int contractInstalmentNum, Date generateContractDate);

    //根据合同id查询合同
    ContractVO getContractInfoByContractId(Integer contractId);

    /**
     * 删除合同
     * @param contractId 合同id
     */
    void delete(Integer contractId);

    //根据合同号查询合同
    ContractVO getContractDetailByContractNumber(String contractNumber);

    //获取过期的合同vo
    ContractVO getOverdueContractVOByContractNumber(String contractNumber);

    //根据合同号查询罚息率
    Double getOverdueFineRateByContractNumber(String contractNumber);

    //生成合同文件
    void generateContractFileForRequsitions();
}
