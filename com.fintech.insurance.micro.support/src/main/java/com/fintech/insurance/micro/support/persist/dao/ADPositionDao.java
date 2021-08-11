package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.micro.support.persist.entity.AdvertisementPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/15 16:57
 */
@Repository
public interface ADPositionDao extends JpaRepository<AdvertisementPosition, Integer> {
    AdvertisementPosition getByCode(String code);
    AdvertisementPosition getById(Integer id);
}
