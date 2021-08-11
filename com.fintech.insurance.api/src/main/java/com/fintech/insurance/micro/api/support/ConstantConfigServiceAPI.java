package com.fintech.insurance.micro.api.support;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.support.ConstantConfigVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @Author: Clayburn
 * @Description: 常量配置服务
 * @Date: 2017/11/09 09:49
 */
@RequestMapping(value = "/support/constant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface ConstantConfigServiceAPI {
    /**
     * 拿到最大逾期天数，还款提醒提前天数，还款提前天数的常量配置
     * @return
     */
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    FintechResponse<List<ConstantConfigVO>> getConstantConfig();

    /**
     * 更新常量配置
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> updateConstantConfig(@RequestBody Map<String, Object> map);

    /**
     * 获取还款提醒提前天数
     * @return
     */
    @RequestMapping(value = "/get-ahead-reminddays", method = RequestMethod.GET)
    FintechResponse<ConstantConfigVO> getAheadRemindDays();
}
