package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.commons.enums.AdStatus;
import com.fintech.insurance.micro.support.persist.entity.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AdvertisementComplexDao {
	Page<Advertisement> findAllAdvertisement(Integer positionId, AdStatus status, int pageIndex, int pageSize);


    Advertisement advertisementExist(Integer id, String title);
}
