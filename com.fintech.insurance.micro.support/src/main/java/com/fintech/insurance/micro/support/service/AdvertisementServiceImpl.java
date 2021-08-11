package com.fintech.insurance.micro.support.service;

import com.fintech.insurance.commons.enums.AdStatus;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.dto.support.AdPositionVO;
import com.fintech.insurance.micro.dto.support.AdvertisementVO;
import com.fintech.insurance.micro.dto.thirdparty.ImageVO;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import com.fintech.insurance.micro.support.persist.dao.ADPositionDao;
import com.fintech.insurance.micro.support.persist.dao.AdvertisementDao;
import com.fintech.insurance.micro.support.persist.entity.Advertisement;
import com.fintech.insurance.micro.support.persist.entity.AdvertisementPosition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {
    private static final Logger logger = LoggerFactory.getLogger(AdvertisementServiceImpl.class);

    @Autowired
    private AdvertisementDao advertisementDao;

    @Autowired
    private ADPositionDao adPositionDao;

    @Autowired
    private QiniuBusinessServiceFeign qiniuBusinessServiceFeign;


    @Override
    @Transactional(readOnly = true)
    public Pagination<AdvertisementVO> findAllAdvertisement(Integer positionId, AdStatus adStatus, int pageIndex, int pageSize) {
        Page<Advertisement> advertisements = advertisementDao.findAllAdvertisement(positionId, adStatus, pageIndex, pageSize);
        List<AdvertisementVO> resultList = entitiesToVOs(advertisements.getContent());
        return Pagination.createInstance(pageIndex, pageSize, advertisements.getTotalElements(), resultList);
    }

    @Override
    @Transactional
    public void saveOrUpdateAdvertisement(AdvertisementVO advertisementVO) throws Exception{
        // 确保Advertisement的title在数据库中唯一(这里面的逻辑无法口述)
        Advertisement temp = advertisementDao.getByTitleAndIdIsNotAndAdvertisementPosition_IdAndDisplayFlagTrue(advertisementVO.getTitle(),
                advertisementVO.getId() == null ? Integer.MIN_VALUE : advertisementVO.getId(), advertisementVO.getPositionId());
        if (temp != null) {
            throw new FInsuranceBaseException(102002);
        }
        if (advertisementVO.getId() == null) { // 新增广告
            Advertisement advertisement = advToEntity(advertisementVO);
            if (advertisement != null) {
                advertisement.setCreateBy(FInsuranceApplicationContext.getCurrentUserId());
                advertisementDao.save(advertisement);
            }
        } else { // 更新广告
            Advertisement advertisement = advertisementDao.getById(advertisementVO.getId());
            if (advertisement == null) {
                throw new FInsuranceBaseException(102007, new Object[]{"id = " + advertisementVO.getId()});
            }
            advertisement.setTitle(advertisementVO.getTitle());
            if (advertisementVO.getPositionId() == null) {
                throw new FInsuranceBaseException(102008, new Object[]{"positionId = " + advertisementVO.getPositionId()});
            }
            AdvertisementPosition adPosition = adPositionDao.getById(advertisementVO.getPositionId());
            if (adPosition == null) {
                throw new FInsuranceBaseException(102008, new Object[]{"positionId = " + advertisementVO.getPositionId()});
            }
            advertisement.setAdvertisementPosition(adPosition);
            advertisement.setUrl(advertisementVO.getUrl());
            advertisement.setStartAt(advertisementVO.getStartTime());
            advertisement.setEndAt(advertisementVO.getEndTime());
            advertisement.setImage(advertisementVO.getImgKey());
            advertisement.setDisplaySequence(advertisementVO.getSequence());
            advertisement.setUpdateBy(FInsuranceApplicationContext.getCurrentUserId());
            advertisement.setUpdateAt(new Date());
        }
    }

    @Override
    @Transactional
    public void deleteAdvertisement(Integer id) {
        Advertisement advertisement = advertisementDao.getById(id);
        if (advertisement != null) {
            advertisement.setDisplayFlag(false);
            advertisement.setUpdateBy(FInsuranceApplicationContext.getCurrentUserId());
            advertisement.setUpdateAt(new Date());

        } else {
            throw new FInsuranceBaseException(102007, new Object[]{"id = " + id});
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AdvertisementVO getAdvertisementById(Integer id) {
        Advertisement advertisement = advertisementDao.getById(id);
        if (advertisement == null) {
            throw new FInsuranceBaseException(102007, new Object[]{"id = " + id});
        }
        if (!advertisement.getDisplayFlag()) {
            throw new FInsuranceBaseException(102009, new Object[]{"id = " + id});
        }
        return entityToVO(advertisementDao.getById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdPositionVO> listAllPosition() {
        return positionListToVOs(adPositionDao.findAll());
    }

    // adposition -> vo
    private AdPositionVO positionToVO(AdvertisementPosition entity) {
        if (entity == null) {
            return null;
        }

        AdPositionVO adPositionVO = new AdPositionVO();
        adPositionVO.setId(entity.getId());
        adPositionVO.setPositionCode(entity.getCode());
        adPositionVO.setPositionName(entity.getName());
        adPositionVO.setDescription(entity.getDescription());

        return adPositionVO;
    }

    // adpositionList -> voList
    private List<AdPositionVO> positionListToVOs(List<AdvertisementPosition> entities) {
        if (entities == null || entities.size() <= 0) {
            return Collections.EMPTY_LIST;
        }
        List<AdPositionVO> resultList = new ArrayList<>();
        for (AdvertisementPosition entity : entities) {
            resultList.add(positionToVO(entity));
        }
        return resultList;
    }

    // vo -> entity
    private Advertisement advToEntity(AdvertisementVO vo) {
        if (vo == null) {
            return null;
        }
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(vo.getTitle());

        if (vo.getPositionId() == null) {
            throw new FInsuranceBaseException(102008, new Object[]{"positionId = " + vo.getPositionId()});
        }
        AdvertisementPosition adPosition = adPositionDao.getOne(vo.getPositionId());
        if (adPosition == null) {
            throw new FInsuranceBaseException(102008, new Object[]{"positionId = " + vo.getPositionId()});
        }
        advertisement.setAdvertisementPosition(adPosition);
        advertisement.setUrl(vo.getUrl());
        advertisement.setStartAt(vo.getStartTime());
        advertisement.setEndAt(vo.getEndTime());
        advertisement.setImage(vo.getImgKey());
        advertisement.setDisplaySequence(vo.getSequence());
        return advertisement;
    }

    // entity -> vo
    private AdvertisementVO entityToVO(Advertisement entity) {
        if (entity == null) {
            return null;
        }
        AdvertisementVO vo = new AdvertisementVO();
        vo.setId(entity.getId());
        vo.setTitle(entity.getTitle());
        vo.setUrl(entity.getUrl());
        vo.setStartTime(entity.getStartAt());
        vo.setEndTime(entity.getEndAt());
        vo.setImgKey(entity.getImage());
        vo.setSequence(entity.getDisplaySequence());
        if (entity.getAdvertisementPosition() != null) {
            vo.setPositionName(entity.getAdvertisementPosition().getName());
            vo.setPositionId(entity.getAdvertisementPosition().getId());
        }
        vo.setAdStatus(judgeAdvertisementStatus(entity));
        return vo;
    }

    // entityList -> voList
    private List<AdvertisementVO> entitiesToVOs(List<Advertisement> entities) {
        if (entities == null || entities.size() <= 0) {
            return Collections.EMPTY_LIST;
        }
        List<AdvertisementVO> voList = new ArrayList<>();
        for (Advertisement entity : entities) {
            voList.add(entityToVO(entity));
        }
        return voList;
    }

    // 判断广告状态
    private String judgeAdvertisementStatus(Advertisement entity) {
        long now =  System.currentTimeMillis();
        if (entity.getStartAt() == null || entity.getEndAt() == null) {
            return AdStatus.UNKNOWN.getCode();
        } else if (now < entity.getStartAt().getTime()) {
            return AdStatus.NOT_STARTED.getCode();
        } else if (now > entity.getEndAt().getTime()) {
            return AdStatus.FINISHED.getCode();
        } else {
            return AdStatus.UNDERWAY.getCode();
        }
    }

}
