package com.fintech.insurance.micro.support.controller;

import com.fintech.insurance.commons.enums.AdStatus;
import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.support.AdvertisementServiceAPI;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.support.AdPositionVO;
import com.fintech.insurance.micro.dto.support.AdvertisementVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/15 18:17
 */
@RestController
@RequestMapping(value = "/support/adv", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdvertisementController extends BaseFintechController implements AdvertisementServiceAPI {
    private static final Logger logger = LoggerFactory.getLogger(AdvertisementController.class);


    @Autowired
    private com.fintech.insurance.micro.support.service.AdvertisementService advertisementService;

    // 需要分页
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public FintechResponse<Pagination<AdvertisementVO>> getAllAdvertisement(@RequestParam(value = "positionId", required = false) Integer positionId,
                                                                            @RequestParam(value = "adStatus", required = false) String adStatus,
                                                                            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        AdStatus advertisementStatus = null;
        // 判断广告状态的正确性
        try {
            if (StringUtils.isNotBlank(adStatus)) {
                advertisementStatus = AdStatus.fromType(adStatus);
            }
        } catch (Exception e) {
            throw new FInsuranceBaseException(102006, new Object[]{"adStatus = " + adStatus});
        }
        return FintechResponse.responseData(advertisementService.findAllAdvertisement(positionId, advertisementStatus, pageIndex, pageSize));
    }

    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public FintechResponse<VoidPlaceHolder> saveOrUpdateAdvertisement(@RequestBody @Validated AdvertisementVO advertisementVO) throws Exception {
        advertisementService.saveOrUpdateAdvertisement(advertisementVO);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public FintechResponse<VoidPlaceHolder> deleteAdvertisement(@RequestBody IdVO idVO) {
        advertisementService.deleteAdvertisement(idVO.getId());
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public FintechResponse<AdvertisementVO> getAdvertisementById(@RequestParam("id") Integer id) {
        return FintechResponse.responseData(advertisementService.getAdvertisementById(id));
    }



    @Override
    @RequestMapping(value = "/judge-title", method = RequestMethod.GET)
    public Boolean judgeAdvertisement(@RequestParam("id")Integer id, @RequestParam("title") String title) {
        return null;
    }

    @Override
    @RequestMapping(value = "/list-adposition", method = RequestMethod.GET)
    public FintechResponse<List<AdPositionVO>> listAllPosition() {
        return FintechResponse.responseData(advertisementService.listAllPosition());
    }
}
