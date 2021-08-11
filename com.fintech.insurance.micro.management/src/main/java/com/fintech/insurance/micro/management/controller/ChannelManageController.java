package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import com.fintech.insurance.micro.dto.validate.groups.Save;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import com.fintech.insurance.micro.feign.biz.ChannelServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/7 12:19
 */
@RestController
@RequestMapping(value = "/management/channel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ChannelManageController extends BaseFintechManagementController {

    @Autowired
    private ChannelServiceFeign channelServiceFeign;

    /**
     * 创建编辑渠道
     * @param channelVO
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    FintechResponse<Integer> saveChannel(@Validated(Save.class) @RequestBody ChannelVO channelVO) {
        FintechResponse<Integer> result = channelServiceFeign.saveChannel(channelVO);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 分页查询渠道信息
     * @param channelCode
     * @param channelName
     * @param companyName
     * @param mobile
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    FintechResponse<Pagination<ChannelVO>> queryChannel(@RequestParam(value = "channelCode", defaultValue = "") String channelCode,
                                                        @RequestParam(value = "channelName", defaultValue = "") String channelName,
                                                        @RequestParam(value = "companyName", defaultValue = "") String companyName,
                                                        @RequestParam(value = "mobile", defaultValue = "") String mobile,
                                                        @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                        @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        FintechResponse<Pagination<ChannelVO>> result = channelServiceFeign.queryChannel(channelCode, channelName, companyName, mobile, pageIndex, pageSize);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 查看渠道详情
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    FintechResponse<ChannelVO> getChannelDetail(@RequestParam(value = "id") Integer id) {
        FintechResponse<ChannelVO> result = channelServiceFeign.getChannelDetail(id);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }


    /**
     * 冻结渠道
     */
    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    void freezeChannel(@Validated({Update.class}) @RequestBody(required = false) ChannelVO channelVO) {
        FintechResponse<VoidPlaceHolder> result = channelServiceFeign.freezeChannel(channelVO);

        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
    }

    /**
     * 解冻渠道
     * @param channelVO
     */
    @RequestMapping(value = "/unfreeze", method = RequestMethod.POST)
    void unfreezeChannel(@Validated({Update.class}) @RequestBody(required = false) ChannelVO channelVO) {
        FintechResponse<VoidPlaceHolder> result = channelServiceFeign.unfreezeChannel(channelVO);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
    }
}
