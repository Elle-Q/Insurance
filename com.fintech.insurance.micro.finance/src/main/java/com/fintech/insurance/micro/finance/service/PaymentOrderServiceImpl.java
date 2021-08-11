package com.fintech.insurance.micro.finance.service;

import com.alibaba.fastjson.JSONArray;
import com.fintech.insurance.commons.enums.*;
import com.fintech.insurance.commons.exceptions.NullParameterException;
import com.fintech.insurance.commons.utils.CalculationFormulaUtils;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.cache.RedisSequenceFactory;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.dto.biz.ContractVO;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.dto.finance.PaymentOrderDetailVO;
import com.fintech.insurance.micro.dto.finance.PaymentOrderVO;
import com.fintech.insurance.micro.dto.finance.VoucherVO;
import com.fintech.insurance.micro.feign.biz.ContractServiceFeign;
import com.fintech.insurance.micro.feign.biz.RequisitionServiceFeign;
import com.fintech.insurance.micro.finance.persist.dao.AccountVoucherDao;
import com.fintech.insurance.micro.finance.persist.dao.PaymentOrderDao;
import com.fintech.insurance.micro.finance.persist.dao.PaymentOrderDetailDao;
import com.fintech.insurance.micro.finance.persist.entity.AccountVoucher;
import com.fintech.insurance.micro.finance.persist.entity.PaymentOrder;
import com.fintech.insurance.micro.finance.persist.entity.PaymentOrderDetail;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service
public class PaymentOrderServiceImpl implements PaymentOrderService{

    private static final Logger LOG = LoggerFactory.getLogger(PaymentOrderServiceImpl.class);

    @Autowired
    private PaymentOrderDao paymentOrderDao;

    @Autowired
    private AccountVoucherDao accountVoucherDao;

    @Autowired
    private RequisitionServiceFeign requisitionServiceFeign;

    @Autowired
    private PaymentOrderDetailDao paymentOrderDetailDao;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ContractServiceFeign contractServiceFeign;

    @Autowired
    private RedisSequenceFactory redisSequenceFactory;


    @Override
    public void manTreat(RequisitionVO requisitionVO) {
        PaymentOrder paymentOrder = paymentOrderDao.getByOrderNumber(requisitionVO.getPaymentOrderNumber());
        if (null == paymentOrder) {
            LOG.error("null patmentOrder with requisitionNumber[" + requisitionVO.getRequisitionNumber() + "]");
            throw new FInsuranceBaseException(105004);
        }
        if (!paymentOrder.getPaymentStatus().equals(DebtStatus.FAILED.getCode())) {
            throw new FInsuranceBaseException(105015);//支付失败由人工处理
        }
        paymentOrder.setManualFlag(true);
        paymentOrder.setUpdateAt(new Date());
        paymentOrderDao.save(paymentOrder);
    }

    @Override
    public void saveVoucher(VoucherVO voucherVO) {
        if (null == voucherVO.getRequisitionCode()) {
            LOG.error("null RequisitionNumber");
            throw new FInsuranceBaseException(105002);
        }
        PaymentOrder paymentOrder = paymentOrderDao.getById(voucherVO.getPaymentOrderId());
        AccountVoucher accountVoucher = new AccountVoucher();
        accountVoucher.setRequisitionId(voucherVO.getRequisitionId());
        accountVoucher.setRequisitionCode(voucherVO.getRequisitionCode());
        accountVoucher.setPaymentOrder(paymentOrder);
        accountVoucher.setTransactionSerial(voucherVO.getTransactionSerial());
        accountVoucher.setVoucher(voucherVO.getVoucher());
        accountVoucher.setRemark(voucherVO.getRemark());
        accountVoucher.setAccountType(voucherVO.getAccountType());
        accountVoucher.setUserId(voucherVO.getUserId());
        accountVoucher.setAccountAmount(new BigDecimal(voucherVO.getAccountAmount()));
        accountVoucher.setCreateAt(new Date());
        accountVoucherDao.save(accountVoucher);
    }

    @Override
    @Transactional(readOnly = true)
    public String[] getByUserAndEntityId(Integer userId, Integer entityId, String accountType) {
        FintechResponse<RequisitionVO> requisitionVOResponse = requisitionServiceFeign.getRequisitionById(entityId);
        if (requisitionVOResponse == null || null == requisitionVOResponse.getData()) {
            throw new FInsuranceBaseException(105004);
        }
        AccountVoucher accountVoucher = accountVoucherDao.getByUserAndCodeAndType(userId, requisitionVOResponse.getData().getRequisitionNumber(), accountType);
        if (null == accountVoucher) {
            return new String[0];
        }
        List<String> list = JSONArray.parseArray(accountVoucher.getVoucher(), String.class);
        return list.toArray(new String[list.size()]);
    }

    @Override
    public Integer savePaymentOrder(String requisitionNumber) {
        FintechResponse<RequisitionVO> requisitionResponse = requisitionServiceFeign.getRequisitionByNumber(requisitionNumber);
        if (!requisitionResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(requisitionResponse);
        }

        // 获取申请单
        RequisitionVO requisitionVO = requisitionResponse.getData();
        if (null == requisitionVO) {
            throw new FInsuranceBaseException(105012);
        }

        // 清除已经存在的支付单记录
        PaymentOrder existingPaymentOrder = paymentOrderDao.getByOrderNumber(requisitionVO.getPaymentOrderNumber());
        if (null != existingPaymentOrder) {//如果该申请单已经存在支付订单信息，则删除旧信息
            deletePaymentOrderAndPaymentOrderDetail(existingPaymentOrder.getId());
        }

        //服务费明细
        PaymentOrderDetail paymentOrderDetailVOForService = new PaymentOrderDetail();
        //保证金明细
        PaymentOrderDetail paymentOrderDetailVOForGuarantee = new PaymentOrderDetail();
        //期初款明细
        PaymentOrderDetail paymentOrderDetailVOForInitialPayment = new PaymentOrderDetail();

        //查询该申请单下的所有合同信息
        FintechResponse<List<ContractVO>> contractVOFintechResponse = contractServiceFeign.getContractByRequisitionId(requisitionVO.getId());
        if (!contractVOFintechResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(contractVOFintechResponse);
        }
        if (contractVOFintechResponse.getData().size() < 1) {
            throw new FInsuranceBaseException(105013);
        }
        List<ContractVO> contractVOList = contractVOFintechResponse.getData();
        //服务费
        paymentOrderDetailVOForService.setItemType(ItemType.SERVICEFEE.getCode());
        paymentOrderDetailVOForService.setPrice(requisitionVO.getServiceFee());
        paymentOrderDetailVOForService.setQuantity(BigDecimal.ONE.intValue());
        paymentOrderDetailVOForService.setSubTotal(paymentOrderDetailVOForService.getPrice());
        paymentOrderDetailVOForService.setFormulaText(String.format("%s x %s", String.valueOf(requisitionVO.getTotalApplyAmount() / 100f),
                String.valueOf((requisitionVO.getServiceFeeRate() + requisitionVO.getOtherFeeRate()) / 10000f)));
        paymentOrderDetailVOForService.setUpdateAt(new Date());

        if (RepayDayType.INITIAL_PAYMENT.getCode().equals(requisitionVO.getRepayDayType())) {//期初还款->生成期初款
            paymentOrderDetailVOForInitialPayment.setItemType(ItemType.INITIAL_PAYMENT_FEE.getCode());
            paymentOrderDetailVOForInitialPayment.setPrice(this.caculateInitialPaymentFee(contractVOList));//期初款
            paymentOrderDetailVOForInitialPayment.setQuantity(BigDecimal.ONE.intValue());
            paymentOrderDetailVOForInitialPayment.setSubTotal(paymentOrderDetailVOForInitialPayment.getPrice());
            paymentOrderDetailVOForInitialPayment.setRemark("单个合同每期应还金额第1期：截取后（单个车辆借款金额/借款期限）+尾差");
            paymentOrderDetailVOForInitialPayment.setUpdateAt(new Date());
        }

        if (requisitionVO.getProductType().equals(ProductType.CAR_INSTALMENTS.getCode())) {//车险分期产生保证金费用
            paymentOrderDetailVOForGuarantee.setItemType(ItemType.GUARANTEE.getCode());
            paymentOrderDetailVOForGuarantee.setPrice(this.caculateGuaranteeFree(contractVOList));//保证金
            paymentOrderDetailVOForGuarantee.setQuantity(BigDecimal.ONE.intValue());
            paymentOrderDetailVOForGuarantee.setSubTotal(paymentOrderDetailVOForGuarantee.getPrice());
            paymentOrderDetailVOForService.setRemark("截取后（单个车辆保单金额/借款期限）");
            paymentOrderDetailVOForGuarantee.setUpdateAt(new Date());
        }

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setCustomerId(requisitionVO.getCustomerId());
        paymentOrder.setPaymentStatus(DebtStatus.WAITINGDEBT.getCode());
        paymentOrder.setOrderNumber(redisSequenceFactory.generateSerialNumber(BizCategory.PON));

        //设置支付订单金额
        Double orderAmount = (null == paymentOrderDetailVOForGuarantee.getSubTotal() ? 0: paymentOrderDetailVOForGuarantee.getSubTotal())
                + paymentOrderDetailVOForService.getSubTotal() + (null == paymentOrderDetailVOForInitialPayment.getSubTotal() ? 0: paymentOrderDetailVOForInitialPayment.getSubTotal());
        paymentOrder.setOrderAmount(new BigDecimal(orderAmount)); // 折扣前金额
        paymentOrder.setDiscountAmount(BigDecimal.ZERO); // 折扣金额
        paymentOrder.setTotalAmount(paymentOrder.getOrderAmount().subtract(paymentOrder.getDiscountAmount())); // 折扣前金额 - 折扣金额
        paymentOrder.setPaymentAmount(paymentOrder.getTotalAmount());
        paymentOrder.setCreateAt(new Date());

        // 更新申请单的服务费支付单
        paymentOrderDao.save(paymentOrder);

        // 更新申请单中的支付订单号
        requisitionVO.setPaymentOrderNumber(paymentOrder.getOrderNumber());
        requisitionServiceFeign.saveRequisition(requisitionVO);//申请单关联支付订单

        // 创建支付明细记录
        // 1. 保存保证金支付明细
        paymentOrderDetailVOForService.setPaymentOrder(paymentOrder);
        paymentOrderDetailDao.save(paymentOrderDetailVOForService);

        // 2. 保存期初款支付明细
        if (RepayDayType.INITIAL_PAYMENT.getCode().equals(requisitionVO.getRepayDayType())) {
            paymentOrderDetailVOForInitialPayment.setPaymentOrder(paymentOrder);
            paymentOrderDetailDao.save(paymentOrderDetailVOForInitialPayment);
        }

        // 3. 保存服务费支付明细
        if (requisitionVO.getProductType().equals(ProductType.CAR_INSTALMENTS.getCode())) {
            paymentOrderDetailVOForGuarantee.setPaymentOrder(paymentOrder);
            paymentOrderDetailDao.save(paymentOrderDetailVOForGuarantee);
        }

        return paymentOrder.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentOrderVO getByOrderNumber(String requisitionNumber) {
        PaymentOrder paymentOrder = paymentOrderDao.getByOrderNumber(requisitionNumber);
        return this.toVO(paymentOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentOrderDetailVO> listByOrderId(Integer id) {
        List<PaymentOrderDetail> paymentOrderDetailList = paymentOrderDetailDao.listByOrderId(id);
        List<PaymentOrderDetailVO> paymentOrderDetailVOS = new ArrayList<>();
        if (null != paymentOrderDetailList && paymentOrderDetailList.size() > 0) {
            for (PaymentOrderDetail p : paymentOrderDetailList) {
                //PaymentOrderDetailVO paymentOrderDetailVO = this.toPaymentOrderDetailVO(p);
                //paymentOrderDetailVOS.add(paymentOrderDetailVO);
            }
        }
        if (paymentOrderDetailList.size() < 1) {
            return Collections.emptyList();
        } else {
            return paymentOrderDetailVOS;
        }
    }

    @Override
    public void deletePaymentOrderAndPaymentOrderDetail(Integer paymentOrderId) {
        List<PaymentOrderDetail> paymentOrderDetailList = paymentOrderDetailDao.listByOrderId(paymentOrderId);
        if (null != paymentOrderDetailList && paymentOrderDetailList.size() > 0) {
            for (PaymentOrderDetail p : paymentOrderDetailList) {
                paymentOrderDetailDao.delete(p);
            }
        }
        PaymentOrder paymentOrder = paymentOrderDao.getById(paymentOrderId);
        paymentOrderDao.delete(paymentOrder);
    }

    @Override
    @Transactional
    public void changePaymentOrderStatus(String paymentOrderNumber, String debtTransactionSerialNum, DebtStatus status) {
        PaymentOrder paymentOrder = paymentOrderDao.getByOrderNumber(paymentOrderNumber);
        if (paymentOrder == null) {
            throw new FInsuranceBaseException(105009);
        }
        paymentOrder.setTransactionSerial(debtTransactionSerialNum); // 设置扣款的交易序列号
        paymentOrder.setPaymentStatus(status.getCode());

        paymentOrderDao.save(paymentOrder);
    }

    @Override
    public void changeDebtStatus(List<String> debtStatusList) {
        List<PaymentOrder> paymentOrderList = paymentOrderDao.findPaymentOrderByDebtStatus(debtStatusList);
        for(PaymentOrder paymentOrder : paymentOrderList){
            this.changeDebtStatus(paymentOrder);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void changeDebtStatus(PaymentOrder paymentOrder) {
        if (paymentOrder != null) {
            DebtStatus status = null;
            try {
                if(StringUtils.isNoneBlank(paymentOrder.getTransactionSerial(), paymentOrder.getBankAccountNumber())) {
                    if (!SystemProfile.PROD.equals(FInsuranceApplicationContext.getSystemProfile())) {// TODO; just to fix the YJF payment issue for testing
                        status = DebtStatus.SETTLED;
                    } else {
                        status = paymentService.queryDebtStatus(paymentOrder.getTransactionSerial(), paymentOrder.getBankAccountNumber());
                    }
                    //status = paymentService.queryDebtStatus(paymentOrder.getTransactionSerial(), paymentOrder.getBankAccountNumber());

                    LOG.info("query the payment order: {}, debt serial number: {}, status: {}", paymentOrder.getOrderNumber(),
                            paymentOrder.getTransactionSerial(), status);
                }

                if(status != null && !status.getCode().equals( paymentOrder.getPaymentStatus())){
                    paymentService.updatePaymentOrderDebtStatus(paymentOrder.getTransactionSerial(), status);
                    LOG.info("update payment order success: {}, debt serial number: {}, status: {}", paymentOrder.getOrderNumber(),
                            paymentOrder.getTransactionSerial(), status);
                }
            } catch (Exception e) {
                LOG.error("Update service fee debt order: {} failed.", paymentOrder.getOrderNumber());
            }
        }
    }

    @Override
    public void updateBankAccountNumber(Integer paymentOrderId, String bankCardNumber) {
        PaymentOrder paymentOrder = paymentOrderDao.getById(paymentOrderId);
        if (paymentOrder == null) {
            throw new FInsuranceBaseException(105009);
        }
        paymentOrder.setBankAccountNumber(bankCardNumber); // 设置扣款的交易序列号
        paymentOrder.setUpdateAt(new Date());

        paymentOrderDao.save(paymentOrder);
    }

    private PaymentOrderVO toVO(PaymentOrder paymentOrder) {
        if (null == paymentOrder) {
            return null;
        }
        PaymentOrderVO vo = new PaymentOrderVO();
        vo.setId(paymentOrder.getId());
        vo.setOrderNumber(paymentOrder.getOrderNumber());
        vo.setPaymentAccountNumer(paymentOrder.getBankAccountNumber());
        vo.setPaymentStatus(paymentOrder.getPaymentStatus());
        vo.setManualFlag(paymentOrder.getManualFlag());
        vo.setCustomerId(paymentOrder.getCustomerId());
        vo.setOrderAmount(paymentOrder.getOrderAmount().doubleValue());
        vo.setPaymentAmount(paymentOrder.getPaymentAmount().doubleValue());
        vo.setTotalAmount(paymentOrder.getTotalAmount().doubleValue());
        vo.setDiscountAmount(paymentOrder.getDiscountAmount().doubleValue());
        return vo;
    }

    //计算期初款
    private Double caculateInitialPaymentFee(List<ContractVO> contractVOList) {
        Double guaranteeFree = 0.0;
        if (null != contractVOList && contractVOList.size() > 0) {
            for (ContractVO c : contractVOList) {
                try {
                    BigDecimal fee = CalculationFormulaUtils.getFirstRepayMoney(new BigDecimal(c.getBorrowAmount()), c.getTotalPhase(), c.getInterestRate(), RepayDayType.INITIAL_PAYMENT);
                    guaranteeFree += fee.doubleValue();
                } catch (NullParameterException e) {
                    throw new FInsuranceBaseException(105014);
                }
            }
        }
        return guaranteeFree;
    }

    //计算保证金费用
    private Double caculateGuaranteeFree(List<ContractVO> contractVOList) {
        Double guaranteeFree = 0.0;
        if (null != contractVOList && contractVOList.size() > 0) {
            for (ContractVO c : contractVOList) {
                try {
                    BigDecimal fee = CalculationFormulaUtils.getAssureMoney(new BigDecimal(c.getBorrowAmount()), c.getTotalPhase());
                    guaranteeFree += fee.doubleValue();
                } catch (NullParameterException e) {
                    throw new FInsuranceBaseException(104107);
                }

            }
        }
        return guaranteeFree;
    }

    @Override
    @Transactional(readOnly = true)
    public String getBankcardNumByDebtSerialNum(String debtSerialNum) {
        if (StringUtils.isBlank(debtSerialNum)) {
            return null;
        }
        PaymentOrder paymentOrder = paymentOrderDao.getByTransactionSerial(debtSerialNum);
        if (null == paymentOrder) {
            return null;
        }
        return paymentOrder.getBankAccountNumber();
    }
}
