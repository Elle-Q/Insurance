package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.components.persist.BaseEntityDao;
import com.fintech.insurance.micro.support.persist.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementDao extends JpaRepository<Advertisement, Integer>, BaseEntityDao<Advertisement, Integer>, AdvertisementComplexDao {
    Advertisement getByTitleAndIdIsNotAndAdvertisementPosition_IdAndDisplayFlagTrue(String title, Integer id, Integer advertisementPositionId);
}
