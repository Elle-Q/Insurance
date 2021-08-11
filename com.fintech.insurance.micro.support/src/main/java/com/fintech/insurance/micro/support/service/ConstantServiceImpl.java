package com.fintech.insurance.micro.support.service;

import com.alibaba.fastjson.JSONArray;
import com.fintech.insurance.commons.enums.DataType;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.dto.support.ConstantConfigVO;
import com.fintech.insurance.micro.support.persist.dao.ConfigPropertyDao;
import com.fintech.insurance.micro.support.persist.entity.ConfigProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/15 8:52
 */
@Service
public class ConstantServiceImpl implements ConstantService {
    @Autowired
    private ConfigPropertyDao configPropertyDao;

    @Override
    @Transactional(readOnly = true)
    public List<ConstantConfigVO> getConstantConfig() {
        List<ConfigProperty> propertyList = configPropertyDao.findAll();
        return entitiesToVOs(propertyList);
    }

    @Override
    @Transactional
    public void updateConstantConfig(Map<String, Object> map) {
        if (map == null || map.size() <= 0) {
            return;
        }
        List<ConfigProperty> propertyList = configPropertyDao.findAll();
        if (propertyList == null || propertyList.size() <= 0) {
            throw new FInsuranceBaseException(102010);
        }
        for (ConfigProperty config : propertyList) {
            // 如果configCode并没有在数据库中配置
            if (map.get(config.getConfigCode()) != null) {
                if (config.getDataType().equals(DataType.NUMBER.getCode())) {
                    try {
                        Integer.parseInt(map.get(config.getConfigCode()).toString());
                    } catch (Exception e) {
                        throw new FInsuranceBaseException(102014, new Object[]{map.get(config.getConfigCode()).toString()});
                    }
                }
                config.setConfigValue(map.get(config.getConfigCode()).toString());
                config.setUpdateBy(FInsuranceApplicationContext.getCurrentUserId());
                config.setUpdateAt(new Date());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ConstantConfigVO getAheadRemindDays() {
        ConfigProperty configProperty = configPropertyDao.getAheadRemindDays();
        return this.entityToVO(configProperty);
    }


    public void setConfigPropertyDao(ConfigPropertyDao configPropertyDao) {
        this.configPropertyDao = configPropertyDao;
    }

    private ConstantConfigVO entityToVO(ConfigProperty entity) {
        if (entity == null) {
            return null;
        }
        ConstantConfigVO vo = new ConstantConfigVO();
        vo.setConfigCode(entity.getConfigCode());
        vo.setConfigName(entity.getConfigName());
        vo.setUnitSuffix(entity.getUnitSuffix());
        if (entity.getCollection()) { // 如果是集合类型, value为json数组
            List<Object> objects = JSONArray.parseArray(entity.getConfigValue(), Object.class);
            vo.setConfigValue(objects);
        } else {
            if (entity.getDataType().equals(DataType.NUMBER.getCode())) {
                vo.setConfigValue(Integer.valueOf(entity.getConfigValue()));
            } else {
                vo.setConfigValue(entity.getConfigValue());
            }
        }
        return vo;
    }

    private List<ConstantConfigVO> entitiesToVOs(List<ConfigProperty> entityList) {
        if (entityList == null || entityList.size() <= 0) {
            return Collections.EMPTY_LIST;
        }
        List<ConstantConfigVO> resultList = new ArrayList<>();
        for (ConfigProperty entity : entityList) {
            ConstantConfigVO vo = entityToVO(entity);
            resultList.add(vo);
        }

        return resultList;
    }
}
