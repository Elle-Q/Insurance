package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.support.AdPositionVO;
import com.fintech.insurance.micro.dto.support.AdvertisementVO;
import com.fintech.insurance.micro.feign.support.AdvertisementServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/7 14:50
 */
@RestController
@RequestMapping(value = "/management/adv", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdvManageController extends BaseFintechManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(ThirdPartyManageController.class);

    @Autowired
    private AdvertisementServiceFeign advertisementServiceFeign;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    FintechResponse<Pagination<AdvertisementVO>> getAllAdvertisement(@RequestParam(value = "positionId", required = false) Integer positionId,
                                                                     @RequestParam(value = "adStatus", required = false) String adStatus,
                                                                     @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                     @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        FintechResponse<Pagination<AdvertisementVO>> result = advertisementServiceFeign.getAllAdvertisement(positionId, adStatus, pageIndex, pageSize);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }
    /**
     * 新建，更新和删除广告
     * @param advertisementVO
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    void saveOrUpdateAdvertisement(@RequestBody @Validated AdvertisementVO advertisementVO) throws Exception {
        FintechResponse<VoidPlaceHolder> result = advertisementServiceFeign.saveOrUpdateAdvertisement(advertisementVO);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    void deleteAdvertisement(@RequestBody IdVO idVO) {
        advertisementServiceFeign.deleteAdvertisement(idVO);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    FintechResponse<AdvertisementVO> getAdvertisementById(@RequestParam("id")Integer id) {
        FintechResponse<AdvertisementVO> result = advertisementServiceFeign.getAdvertisementById(id);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    @RequestMapping(value = "/list-adposition", method = RequestMethod.GET)
    FintechResponse<List<AdPositionVO>> listAllPosition() {
        FintechResponse<List<AdPositionVO>> result = advertisementServiceFeign.listAllPosition();
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }
}
