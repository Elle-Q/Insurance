package com.fintech.insurance.micro.biz.persist.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/12/6 14:34
 */
@Repository
public interface TempDao extends JpaRepository<RepaymentPlanTest, Integer> {
}
