package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.dto.biz.TestDateVO;
import com.fintech.insurance.micro.feign.thirdparty.HelloServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/1 17:52
 */
@RestController
@RequestMapping(value = "/biz/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BizTestController extends BaseFintechController {

    @Autowired
    private HelloServiceFeign helloServiceFeign;

    @RequestMapping(value = "/sendData", method = RequestMethod.POST)
    public FintechResponse<TestDateVO> test(@RequestBody TestDateVO vo) {
        System.out.println(vo);
        FintechResponse<TestDateVO> thirdResult = helloServiceFeign.index2(vo);

        System.out.println("ThirdParty result " + thirdResult.toString());
        return thirdResult;
    }


}
