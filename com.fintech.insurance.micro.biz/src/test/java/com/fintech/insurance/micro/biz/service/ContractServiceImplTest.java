package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.biz.ServiceTestApplication;
import com.fintech.insurance.micro.biz.service.contract.ContractGeneratorService;
import com.fintech.insurance.micro.dto.biz.ContractVO;
import freemarker.template.TemplateException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@ActiveProfiles("junit")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceTestApplication.class})
public class ContractServiceImplTest {
    @Autowired
    ContractService contractService;

    @Autowired
    @Qualifier("ckContractGenerator")
    private ContractGeneratorService ckContractGenerator;

    @Autowired
    @Qualifier("dkContractGenerator")
    private ContractGeneratorService dkContractGenerator;

    @Test
    public void pageContract() throws Exception {
        Pagination<ContractVO> page = contractService.pageContract(null, null,null, null, null, null, null, 1, Integer.MAX_VALUE);

        Assert.assertNotNull(page.getItems());

    }

    @Test
    public void testGenerateContractSerialNum() {
        String contractNum1 = contractService.generateContractSerialNum("CX01", "001", 12, DateCommonUtils.formDateString("2017-11-12"));

        System.out.println(contractNum1);

        String contractNum2 = contractService.generateContractSerialNum("DK17", "011", 04, DateCommonUtils.formDateString("2017-11-12"));

        System.out.println(contractNum2);
    }

    @Test
    public void testGenerateServiceContract() throws IOException, TemplateException {
        //ckContractGenerator.buildBorrowContract(4921);
        //ckContractGenerator.buildServiceContract(4893);

        ckContractGenerator.buildBorrowContract(5179);
    }

    @Test
    public void testCleanContractDataByRequisitionId() throws IOException, TemplateException {
        contractService.cleanContractDataByRequisitionId(4704);
    }

}