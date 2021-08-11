package com.fintech.insurance.micro.api;

import com.fintech.insurance.commons.annotations.FinanceDataPoint;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.TestDateVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping(value = "/thirdparty/hello", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface HelloServiceAPI {

    @RequestMapping(value = "/test/hello", method = RequestMethod.GET)
    public FintechResponse<String> index(@FinanceDataPoint @RequestParam(name = "amount") Long amount, @FinanceDataPoint @RequestParam(name = "ratio") Double ratio);

    @RequestMapping(value = "/test/body", method = RequestMethod.POST)
    public FintechResponse<TestDateVO> index2(@RequestBody TestDateVO vo);

    @RequestMapping(value = "/test/exception", method = RequestMethod.GET)
    public FintechResponse<String> testException(@RequestParam("key") String key);

    @RequestMapping(value = "/test/void", method = RequestMethod.GET)
    public void testReturnVoid();

    @RequestMapping(value = "/test/async", method = RequestMethod.GET)
    public void testAsync();
}
