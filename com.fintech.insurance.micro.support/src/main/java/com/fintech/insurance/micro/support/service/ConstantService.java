package com.fintech.insurance.micro.support.service;

import com.fintech.insurance.micro.dto.support.ConstantConfigVO;

import java.util.List;
import java.util.Map;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/15 8:49
 */
public interface ConstantService {
    /**
     * 获取配置信息
     *
     * @return
     */
    List<ConstantConfigVO> getConstantConfig();

    /**
     * 更新配置信息
     *
     * @param map
     */
    void updateConstantConfig(Map<String, Object> map);

    ConstantConfigVO getAheadRemindDays();
}
