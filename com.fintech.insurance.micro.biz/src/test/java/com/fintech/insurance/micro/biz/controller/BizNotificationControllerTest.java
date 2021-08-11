package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.commons.enums.RefundStatus;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.biz.service.ContractService;
import com.fintech.insurance.micro.biz.service.RequisitionService;
import com.fintech.insurance.micro.dto.biz.ContractVO;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendParamVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendResultVO;
import com.fintech.insurance.micro.feign.finance.RepaymentPlanServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.sms.SMSServiceFeign;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Description: (some words)
 * @Author: East
 * @Date: 2017/11/20 0020 15:10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(BizNotificationController.class)
public class BizNotificationControllerTest {

    @Autowired
    private BizNotificationController bizNotificationController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc; // 模拟MVC对象

    @MockBean
    private RequisitionService requisitionService;

    @MockBean
    private ContractService contractService;

    @MockBean
    private SMSServiceFeign smsServiceFeign;

    @MockBean
    private RepaymentPlanServiceFeign repaymentPlanServiceFeign;

    @Before // 在测试开始前初始化工作
    public void setup() {
        // mock requisitionService
        RequisitionVO requisitionVO = new RequisitionVO();
        requisitionVO.setRequisitionNumber("APP001");
        requisitionVO.setChannelUserName("Lucy");
        requisitionVO.setChannelUserMobile("15120611011");
        requisitionVO.setCustomerName("East");
        requisitionVO.setCustomerMobile("15120622022");

        given(this.requisitionService.getRequisitionById(anyInt())).willReturn(requisitionVO);

        // mock smsServiceClient
        SMSSendResultVO smsSendResultVO = new SMSSendResultVO();
        smsSendResultVO.setBizId("B0000001");
        smsSendResultVO.setSequenceId("Seq000001");
        smsSendResultVO.setExpireTime(DateTime.now().minusMillis(5).toDate());

        given(smsServiceFeign.sendSMS(any(SMSSendParamVO.class))).willReturn(FintechResponse.responseData(smsSendResultVO));

        //mock contractService
        ContractVO contractVO = new ContractVO();
        contractVO.setRequisitionNumber("APP001");
        contractVO.setChannelUserName("Lucy");
        contractVO.setChannelUserMobile("15120611011");
        contractVO.setCustomerName("East");
        contractVO.setCustomerMobile("15120622022");

        given(this.contractService.getContractById(anyInt())).willReturn(contractVO);

        // mock repaymentPlanServiceClient
        FinanceRepaymentPlanVO repaymentPlanVO = new FinanceRepaymentPlanVO();
        repaymentPlanVO.setTotalInstalment(12);
        repaymentPlanVO.setTotalInstalment(5);
        repaymentPlanVO.setRepayTotalAmount(50000.00D);
        repaymentPlanVO.setRepayDate(DateTime.now().minusDays(5).toDate());
        RefundStatus[] refundStatuses = new RefundStatus[] {RefundStatus.HAS_REFUND, RefundStatus.FAIL_REFUND, RefundStatus.OVERDUE};
        repaymentPlanVO.setRepayStatus(refundStatuses[RandomUtils.nextInt(0, refundStatuses.length - 1)]);

        given(repaymentPlanServiceFeign.getRepaymentPlan(anyInt())).willReturn(FintechResponse.responseData(repaymentPlanVO));
    }

    @Test
    public void testSendAuditNotification() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/biz/notification/send-approval-notification").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("requisitionId", "1");
        MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.code").value("0")).andReturn();
    }

    @Test
    public void testSendLoanConfirmNotification() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/biz/notification/send-loan-confirm-notification").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("requisitionId", "1");
        MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.code").value("0")).andReturn();
    }

    @Test
    public void testSendRepaymentRemindNotification() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/biz/notification/send-repay-remind-notification").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("contractId", "1").param("repaymentPlanId", "1");
        MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.code").value("0")).andReturn();
    }

    @Test
    public void testSendOverdueRemindNotification() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/biz/notification/send-overdue-remind-notification").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("contractId", "1").param("repaymentPlanId", "1");
        MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.code").value("0")).andReturn();
    }

    @Test
    public void testSendRepaymentResultNotification() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/biz/notification/send-repay-notification").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).param("contractId", "1").param("repaymentPlanId", "1");
        MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.code").value("0")).andReturn();
    }

}
