package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.micro.support.persist.entity.AppBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/15 14:15
 */
@Repository
public interface AppBankDao extends JpaRepository<AppBank, Integer> {
}
