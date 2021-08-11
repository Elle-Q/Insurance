package com.fintech.insurance.micro.support.service;

import com.fintech.insurance.commons.enums.AdStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.micro.dto.support.AdPositionVO;
import com.fintech.insurance.micro.dto.support.AdvertisementVO;

import java.util.List;

public interface AdvertisementService {
    /**
     * 分页显示所有广告
     * @param positionId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Pagination<AdvertisementVO> findAllAdvertisement(Integer positionId, AdStatus adStatus, int pageIndex, int pageSize);

    /**
     * 新建，更新和删除广告
     *
     * @param advertisementVO
     */
    public void saveOrUpdateAdvertisement(AdvertisementVO advertisementVO) throws Exception;

    public void deleteAdvertisement(Integer id);

    AdvertisementVO getAdvertisementById(Integer id);

    List<AdPositionVO> listAllPosition();
}
