package com.fintech.insurance.micro.biz.service;

import com.alibaba.fastjson.JSONArray;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.enums.*;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.cache.RedisSequenceFactory;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.biz.event.RequisitionLifeCycleEvent;
import com.fintech.insurance.micro.biz.persist.dao.ContractDao;
import com.fintech.insurance.micro.biz.persist.dao.RequisitionDao;
import com.fintech.insurance.micro.biz.persist.entity.Contract;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import com.fintech.insurance.micro.dto.biz.LoanVO;
import com.fintech.insurance.micro.dto.biz.OperationRecordVO;
import com.fintech.insurance.micro.dto.biz.RecordVO;
import com.fintech.insurance.micro.dto.finance.VoucherVO;
import com.fintech.insurance.micro.feign.finance.RecordAccountServiceFeign;
import com.fintech.insurance.micro.feign.finance.RefundServiceFeign;
import com.fintech.insurance.micro.feign.retrieval.BizQueryFeign;
import com.fintech.insurance.micro.feign.system.EntityOperationLogServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/18 17:36
 */
@Service
public class LoanServiceImpl implements LoanService, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    private ApplicationContext applicationContext;

    @Autowired
    private RedisSequenceFactory redisSequenceFactory;

    @Autowired
    private RequisitionDao requisitionDao;

    @Autowired
    private RecordAccountServiceFeign recordAccountServiceFeign;

    @Autowired
    private BizQueryFeign bizQueryFeign;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private EntityOperationLogServiceFeign entityOperationLogServiceFeign;

    @Autowired
    private RefundServiceFeign refundServiceFeign;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<LoanVO> pageLoanInfo(String requisitionNumber, RequisitionStatus requisitionStatus,
                                           ProductType productType, String channelName, String customerName, Integer pageIndex, Integer pageSize) {
        FintechResponse<Pagination<LoanVO>> response =  bizQueryFeign.pageLoanVO(requisitionNumber, requisitionStatus, productType, channelName, customerName, pageIndex, pageSize);

        if (response == null || !response.isOk()) {
            throw new FInsuranceBaseException(104928);
        }

        return response.getData();
    }

    @Override
    @Transactional
    public void recordLoan(RecordVO recordVO) {
        // 1、 更改订单状态
        Requisition requisition = requisitionDao.getRequisitionByRequisitionNumber(recordVO.getCode());
        if (requisition == null) {
            logger.error("申请单不存在 with recordLoan error requisition requisitionNumber=[" + recordVO.getCode() + "]");
            throw new FInsuranceBaseException(104904, new Object[]{recordVO.getCode()});
        }
        String oldStatus = requisition.getRequisitionStatus();
        RequisitionStatus requisitionStatus = RequisitionStatus.codeOf(requisition.getRequisitionStatus());
        if(requisitionStatus != RequisitionStatus.WaitingLoan){
            String status = requisitionStatus == null ? "" : requisitionStatus.getCode() ;
            logger.error("当前状态不允许放款 with recordLoan requisitionStatus requisition requisitionNumber=[" + recordVO.getCode() + "]，requisitionStatus=["+ status +"]");
            throw new FInsuranceBaseException(105932);
        }
        Set<RequisitionDetail> detailSet = requisition.getDetails();
        // 更改合同状态, 确认放款之后由初始状态变为还款中
        List<Contract> contractList = new ArrayList<Contract>();
        for(RequisitionDetail detail : detailSet){
            Contract contract = detail.getContract();
            if(contract == null){
                logger.error("合同不存在 with recordLoan error contract requisitiondetailId=[" + detail.getId() + "]");
                throw new FInsuranceBaseException(104908, new Object[]{recordVO.getCode()});
            }
            contractList.add(contract);
        }
        if(contractList.size() < 1){
            logger.error("合同不存在 with recordLoan error contractList requisitionNumber=[" + recordVO.getCode() + "]");
            throw new FInsuranceBaseException(104908, new Object[]{recordVO.getCode()});
        }
        for (Contract contract : contractList) {
            contract.setContractStatus(ContractStatus.Refunding.getCode());
            FintechResponse<VoidPlaceHolder> contractNumberList = refundServiceFeign.updateRefundStatusByContractNumber(requisition.getRepayDayType(), contract.getContractNumber());
            if(!contractNumberList.isOk()){
                throw FInsuranceBaseException.buildFromErrorResponse(contractNumberList);
            }
            contractDao.save(contract);
        }
        requisition.setLoanTime(new Date());
        requisition.setRequisitionStatus(RequisitionStatus.Loaned.getCode());

        // 2、上传图片
        VoucherVO voucherVO = new VoucherVO();
        voucherVO.setAccountType(AccountType.LOAN.getCode());
        voucherVO.setTransactionSerial(redisSequenceFactory.generateSerialNumber(BizCategory.VH));
        voucherVO.setUserId(FInsuranceApplicationContext.getCurrentUserId());
        voucherVO.setRequisitionId(requisition.getId());
        voucherVO.setRequisitionCode(requisition.getRequisitionNumber());
        voucherVO.setRemark(recordVO.getRemark());
        voucherVO.setVoucher(JSONArray.toJSON(recordVO.getImgKey()).toString());
        // 放款金额
        voucherVO.setAccountAmount(requisition.getTotalApplyAmount().doubleValue());
        logger.info("确认放款凭证上传");
        recordAccountServiceFeign.recordAccount(voucherVO);


        // 生成操作记录
        OperationRecordVO operationrRecordVO = new OperationRecordVO();
        operationrRecordVO.setOperationRemark(recordVO.getRemark());
        operationrRecordVO.setEntityType(Requisition.class.getSimpleName());
        operationrRecordVO.setOperationType(OperationType.LOANED.getCode());
        operationrRecordVO.setEntityId(requisition.getId());
        operationrRecordVO.setUserId(FInsuranceApplicationContext.getCurrentUserId());
        entityOperationLogServiceFeign.createLog(operationrRecordVO);
        requisitionDao.save(requisition);
        //放款成功后通知已放款
        // i do not know what could i say fuck!
        if (!RequisitionStatus.Loaned.getCode().equals(oldStatus)) {
            this.applicationContext.publishEvent(new RequisitionLifeCycleEvent(requisition, oldStatus, RequisitionStatus.Loaned.getCode()));
        }
    }
}
