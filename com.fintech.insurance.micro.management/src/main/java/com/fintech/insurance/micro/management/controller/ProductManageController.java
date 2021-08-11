package com.fintech.insurance.micro.management.controller;

import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechManagementController;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import com.fintech.insurance.micro.dto.biz.ProductChannelDetailVO;
import com.fintech.insurance.micro.dto.biz.ProductChannelVO;
import com.fintech.insurance.micro.dto.biz.ProductVO;
import com.fintech.insurance.micro.dto.validate.groups.Find;
import com.fintech.insurance.micro.dto.validate.groups.Save;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import com.fintech.insurance.micro.feign.biz.ChannelServiceFeign;
import com.fintech.insurance.micro.feign.biz.ProductBusinessServiceFegin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/12/7 12:04
 */
@RestController
@RequestMapping(value = "/management/product", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductManageController extends BaseFintechManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductManageController.class);

    @Autowired
    private ProductBusinessServiceFegin productBusinessServiceFegin;

    @Autowired
    private ChannelServiceFeign channelServiceFeign;

    //保存产品
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public FintechResponse<Integer> saveProduct(@Validated(Save.class) @RequestBody ProductVO productVO) {
        FintechResponse<Integer> result = productBusinessServiceFegin.saveProduct(productVO);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    //上架
    @RequestMapping(value = "/on-shelves", method = RequestMethod.POST)
    public void onShelveProduct(@Validated(Find.class) @RequestBody ProductVO productVO) {
        productBusinessServiceFegin.onShelveProduct(productVO);
    }

    //下架
    @RequestMapping(value = "/down-shelves", method = RequestMethod.POST)
    public void downShelveProduct(@Validated(Find.class) @RequestBody ProductVO productVO) {
        productBusinessServiceFegin.downShelveProduct(productVO);
    }

    //获取产品详情
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public FintechResponse<ProductChannelDetailVO> getProductInfoById(@RequestParam(value = "id") Integer id) {
        FintechResponse<ProductChannelDetailVO> result = productBusinessServiceFegin.getProductInfoById(id);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    //查询产品
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public FintechResponse<Pagination<ProductVO>> queryProduct(String productType, String productName, String repayDayType,
                                                               Integer isOnsale, @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                               @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize){
        FintechResponse<Pagination<ProductVO>> result = productBusinessServiceFegin.queryProduct(productType, productName, repayDayType, isOnsale, pageIndex, pageSize);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return result;
    }

    /**
     * 查询待增加的渠道
     * @param productId 产品id
     * @return
     */
    @RequestMapping(value = "/available-channel/list", method = RequestMethod.GET)
    public FintechResponse<List<ChannelVO>> queryWaitingForAddChannel(@RequestParam(value = "productId", required = false) Integer productId) {
        FintechResponse<List<ChannelVO>> result = channelServiceFeign.queryWaitingForAddChannel(productId);
        if (!result.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(result);
        }
        return channelServiceFeign.queryWaitingForAddChannel(productId);
    }

    /**
     * 新增产品渠道
     * @param pcVO 产品渠道VO
     * @return
     */
    @RequestMapping(value = "/add-channel", method = RequestMethod.POST)
    void addProductChannel(@Validated(Save.class) @RequestBody ProductChannelVO pcVO) {
        channelServiceFeign.addProductChannel(pcVO);
    }

    /**
     * 删除产品渠道
     * @param pcVO 产品渠道VO
     * @return
     */
    @RequestMapping(value = "/delete-channel", method = RequestMethod.POST)
    void deleteProductChannel(@Validated(Update.class) @RequestBody ProductChannelVO pcVO) {
        channelServiceFeign.deleteProductChannel(pcVO);
    }
}
