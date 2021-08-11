package com.fintech.insurance.micro.finance.persist;

import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.micro.finance.persist.dao.AccountVoucherDao;
import com.fintech.insurance.micro.finance.persist.dao.PaymentOrderDetailDao;
import com.fintech.insurance.micro.finance.persist.entity.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FinanceDaoTest {
    @Autowired
    AccountVoucherDao voucherDao;

    @Autowired
    PaymentOrderDetailDao paymentOrderDetailDao;

    @Test
    @Transactional
    public void save() {
        // 订单
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setCustomerId(23);
        paymentOrder.setPaymentStatus("支付状态");
        paymentOrder.setOrderNumber("订单号");
        paymentOrder.setOrderAmount( new BigDecimal("1111.22"));
        paymentOrder.setDiscountAmount( new BigDecimal("1111.22"));
        paymentOrder.setTotalAmount( new BigDecimal("1111.22"));
        paymentOrder.setPaymentMethod("支付方式");
        paymentOrder.setPaymentTime(new Date());
        paymentOrder.setPaymentAmount( new BigDecimal("1111.22"));
        paymentOrder.setTransactionSerial("交易序列号");
        paymentOrder.setCustomerVoucher("收据内容");
        paymentOrder.setRemark("备注");

        // 订单详情
        PaymentOrderDetail paymentOrderDetail = new PaymentOrderDetail();
        paymentOrderDetail.setItemType("服务费");
        paymentOrderDetail.setPrice(12.12);
        paymentOrderDetail.setQuantity(23);
        paymentOrderDetail.setSubTotal(12.12 * 23);
        paymentOrderDetail.setFormulaText("12.12 * 23");
        paymentOrderDetail.setRemark("订单详情备注");
        paymentOrderDetail.setPaymentOrder(paymentOrder);

        paymentOrderDetailDao.save(paymentOrderDetail);
        Assert.assertNotNull(paymentOrderDetail.getId());


        // 还款计划
        RepaymentPlan repaymentPlan = new RepaymentPlan();
        repaymentPlan.setCustomerId(222);
        repaymentPlan.setContractNumber("3333333343");
        repaymentPlan.setChannelId(44);
        repaymentPlan.setRepayDate(new Date());
        repaymentPlan.setRepayTotalAmount(new BigDecimal("345345435"));
        repaymentPlan.setRepayCapitalAmount(new BigDecimal("345345435"));
        repaymentPlan.setRepayInterestAmount(new BigDecimal("345345435"));
        repaymentPlan.setCurrentInstalment(2);
        repaymentPlan.setTotalInstalment(5);
        repaymentPlan.setRepayStatus(RefundStatus.OVERDUE);
        repaymentPlan.setManualFlag(false);

        // 还款记录
        RepaymentRecord repaymentRecord = new RepaymentRecord();
        repaymentRecord.setRepayDate(new Date());
        repaymentRecord.setRepayTime(new Date());
        repaymentRecord.setRepayTotalAmount(534534534L);
        repaymentRecord.setRepayCapitalAmount(4405803840L);
        repaymentRecord.setRepayInterestAmount(84080L);
        repaymentRecord.setOverdueInterestAmount(4535L);
        repaymentRecord.setOverdueFlag(false);
        repaymentRecord.setPrepaymentFlag(false);
        repaymentRecord.setPrepaymentPenaltyAmount(4563L);
        repaymentRecord.setCustomerVoucher("ssssss");
        repaymentRecord.setTransactionSerial("98799hfiu0");
        repaymentRecord.setConfirmStatus("确认状态");
        repaymentRecord.setConfirmBy(45);
        repaymentRecord.setCustomerVoucher("sdfwe");
        repaymentRecord.setRemark("ddddddd");

        repaymentRecord.setRepaymentPlan(repaymentPlan);

        AccountVoucher accountVoucher = new AccountVoucher();
        accountVoucher.setAccountType("类型1");
        accountVoucher.setTransactionSerial("122313");
        accountVoucher.setUserId(22);
        accountVoucher.setVoucher("收据1");
        accountVoucher.setAccountAmount(new BigDecimal("345345435"));
        accountVoucher.setPaymentOrder(paymentOrder);
        accountVoucher.setRepaymentRecord(repaymentRecord);

        voucherDao.save(accountVoucher);

        Assert.assertNotNull(accountVoucher.getId());


    }
}