package com.fintech.insurance.micro.support.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.support.ConstantConfigServiceAPI;
import com.fintech.insurance.micro.dto.support.ConstantConfigVO;
import com.fintech.insurance.micro.support.service.ConstantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: Clayburn
 * @Description: 常量配置管理
 * @Date: 2017/11/09 09:49
 */
@RestController
public class ConstantController extends BaseFintechController implements ConstantConfigServiceAPI {
    private static final Logger log = LoggerFactory.getLogger(ConstantController.class);

    @Autowired
    private ConstantService constantService;

    @Override
    public FintechResponse<List<ConstantConfigVO>> getConstantConfig() {
        return FintechResponse.responseData(constantService.getConstantConfig());
    }


    @Override
    public FintechResponse<VoidPlaceHolder> updateConstantConfig(@RequestBody Map<String, Object> map) {
        constantService.updateConstantConfig(map);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<ConstantConfigVO> getAheadRemindDays() {
        return FintechResponse.responseData(constantService.getAheadRemindDays());
    }
}
