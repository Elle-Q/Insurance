package com.fintech.insurance.micro.api.support;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.IdVO;
import com.fintech.insurance.micro.dto.support.AdPositionVO;
import com.fintech.insurance.micro.dto.support.AdvertisementVO;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: Clayburn
 * @Description:
 * @Date: 2017/11/15 18:25
 */
@RequestMapping(value = "/support/adv", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface AdvertisementServiceAPI {
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    FintechResponse<Pagination<AdvertisementVO>> getAllAdvertisement(@RequestParam(value = "positionId", required = false) Integer positionId,
                                                                     @RequestParam(value = "adStatus", required = false) String adStatus,
                                                                     @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                     @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);
    /**
     * 新建，更新和删除广告
     * @param advertisementVO
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> saveOrUpdateAdvertisement(@RequestBody @Validated AdvertisementVO advertisementVO) throws Exception;

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> deleteAdvertisement(@RequestBody IdVO idVO);

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    FintechResponse<AdvertisementVO> getAdvertisementById(@RequestParam("id")Integer id);

    @RequestMapping(value = "/judge", method = RequestMethod.GET)
    Boolean judgeAdvertisement(@RequestParam("id")Integer id, @RequestParam("title") String title);

    @RequestMapping(value = "/list-adposition", method = RequestMethod.GET)
    FintechResponse<List<AdPositionVO>> listAllPosition();
}