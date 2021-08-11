package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.commons.enums.ContractStatus;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.micro.biz.service.ContractService;
import com.fintech.insurance.micro.biz.service.ExportExcelService;
import com.fintech.insurance.micro.dto.customer.CustomerVO;
import com.fintech.insurance.micro.dto.finance.FinanceRepaymentPlanVO;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.finance.RepaymentPlanServiceFeign;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

/**
 * @Description: (some words)
 * @Author: Administrator
 * @Date: 2017/11/22 0022 20:32
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {BizReportController.class})
public class BizReportControllerTest extends  BaseControllerTests{
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerServiceFeign customerServiceFeign;

    @MockBean
    private ContractService contractService;

    @MockBean
    private RepaymentPlanServiceFeign repaymentPlanServiceClient;

    @MockBean
    private ExportExcelService service;

    final Random random = new Random();

    @Override
    public void setup() throws Exception {
        super.setup();
        Mockito.when(customerServiceFeign.listCustomerByName(Mockito.any(String.class))).thenAnswer(new Answer< List<CustomerVO>>() {
            @Override
            public  List<CustomerVO> answer(InvocationOnMock invocation) throws Throwable {
                List<CustomerVO> list = new ArrayList<CustomerVO>();
                CustomerVO vo = new CustomerVO();
                vo.setAccountInfoId(random.nextInt());
                return list;
            }
        });
        Mockito.when(this.repaymentPlanServiceClient.findAllRepaymentPlanByContractStatus(Mockito.any(ContractStatus.class))).thenAnswer(new Answer<Map<String,FinanceRepaymentPlanVO>>() {
            @Override
            public Map<String,FinanceRepaymentPlanVO> answer(InvocationOnMock invocation) throws Throwable {
                Map<String,FinanceRepaymentPlanVO> list = new HashMap<String,FinanceRepaymentPlanVO>();
                return list;
            }
        });
        Mockito.when(this.contractService.convertListToMap(Mockito.any(List.class),Mockito.any(ProductType.class),Mockito.any(ContractStatus.class))).thenAnswer(new Answer<Map<String,FinanceRepaymentPlanVO>>() {
            @Override
            public Map<String,FinanceRepaymentPlanVO> answer(InvocationOnMock invocation) throws Throwable {
                Map<String,FinanceRepaymentPlanVO> list = new HashMap<String,FinanceRepaymentPlanVO>();
                return list;
            }
        });
        Mockito.doNothing().when(this.service).bizReportExportExcel(Mockito.any(List.class),Mockito.any(String.class),Mockito.any(String.class));
    }

    @Test
    public void queryBizReport() {
    }

    @Test
    public void exportBizReport() {
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        service.bizReportExportExcel(mapList,"123631414@qq.com",null);
    }

}