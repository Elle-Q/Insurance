package com.fintech.insurance.micro.thirdparty.controller;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.HelloServiceAPI;
import com.fintech.insurance.micro.dto.biz.TestDateVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/15 10:54
 */
@RestController
public class TestController extends BaseFintechController  implements HelloServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Override
    public FintechResponse<String> index(@FinanceDataPoint Long amount, @FinanceDataPoint Double ratio) {

        LOG.info("金融数据amount={}, ratio={}", amount, ratio);

        LOG.info("EEEEEEEEEEEE 测试消息");
        LOG.error("DEEEEEEEEEEE 错误消息");

        return FintechResponse.responseData(new String("oh, ye"));
    }

    @Override
    public FintechResponse<TestDateVO> index2(@RequestBody  TestDateVO vo) {
        return FintechResponse.responseOk(vo, "yes");
    }

    @Override
    public FintechResponse<String> testException(String key) {
        throw new FInsuranceBaseException(106311, new Object[]{"test", "abc"});
    }

    @Override
    public void testReturnVoid() {
        LOG.info("hello ....");
    }

    @Async
    @Override
    public void testAsync() {
        long time = System.currentTimeMillis();
        System.err.println(Thread.currentThread().getId());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.err.println("\nasyn total time:"+(System.currentTimeMillis()-time));
    }
}
