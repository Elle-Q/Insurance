package com.fintech.insurance.micro.biz.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @Description: (some words)
 * @Author: qxy
 * @Date: 2017/11/21 0021 17:40
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ChannelController.class})
public class RequisitionControllerTest extends BaseControllerTests{

    @Autowired
    private MockMvc mvc;

    @Override
    public void setup() throws Exception {
        super.setup();
    }

    @Test
    public void query() {
    }
}
