package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.micro.biz.ServiceTestApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: (some words)
 * @Author: qxy
 * @Date: 2017/11/14 9:16
 */
@ActiveProfiles("junit")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceTestApplication.class})
@Transactional
public class RequisitionServiceTest {
}
