package com.fintech.insurance.micro.support.service;

import com.fintech.insurance.micro.dto.support.BankInfoVO;
import com.fintech.insurance.micro.support.persist.dao.AppBankDao;
import com.fintech.insurance.micro.support.persist.entity.AppBank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/15 14:13
 */
@Service
public class BankInfoServiceImpl implements BankInfoService {
    @Autowired
    private AppBankDao appBankDao;

    @Override
    @Transactional(readOnly = true)
    public List<BankInfoVO> listAllBankInfo() {
        return entitiesToVOs(appBankDao.findAll());
    }

    private BankInfoVO entityToVO(AppBank entity) {
        if (entity == null) {
            return null;
        }
        BankInfoVO vo = new BankInfoVO();
        vo.setId(entity.getId());
        vo.setName(entity.getBankName());
        vo.setCode(entity.getBankCode());

        return vo;
    }

    private List<BankInfoVO> entitiesToVOs(List<AppBank> entities) {
        if (entities == null || entities.size() <= 0) {
            return Collections.EMPTY_LIST;
        }
        List<BankInfoVO> resultList = new ArrayList<>();
        for (AppBank appBank : entities) {
            resultList.add(entityToVO(appBank));
        }

        return resultList;
    }

}
