package com.fintech.insurance.micro.finance.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.finance.persist.entity.YjfLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/12 16:30
 */
@Repository
public interface YjfLogDao extends JpaRepository<YjfLog, Integer>, BaseEntityDao<YjfLog, Integer> {
}
