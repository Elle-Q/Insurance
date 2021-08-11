package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.biz.ChannelServiceAPI;
import com.fintech.insurance.micro.biz.persist.entity.Channel;
import com.fintech.insurance.micro.biz.service.ChannelService;
import com.fintech.insurance.micro.biz.service.RequisitionService;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import com.fintech.insurance.micro.dto.biz.ProductChannelVO;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.dto.validate.groups.Save;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.retrieval.BizQueryFeign;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: qxy
 * @Description: 渠道管理
 * @Date: 2017/11/17 18:19
 */
@RestController
public class ChannelController extends BaseFintechController implements ChannelServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(ChannelController.class);

    @Autowired
    private ChannelService channelService;

    @Autowired
    private BizQueryFeign bizQueryFeign;

    @Autowired
    private RequisitionService requisitionService;

    @Autowired
    private CustomerServiceFeign customerServiceFeign;

    @Override
    public FintechResponse<Integer> saveChannel(@Validated(Save.class) @RequestBody ChannelVO channelVO) {

        Integer currentLoginUserID = getCurrentLoginUserId() == null ? 0 : getCurrentLoginUserId();
        if (null == currentLoginUserID) {
            throw new FInsuranceBaseException(104516);
        }
        Channel channel = channelService.saveChannel(channelVO, currentLoginUserID);
        return FintechResponse.responseData(channel.getId());
    }

    @Override
    public FintechResponse<Pagination<ChannelVO>> queryChannel(@RequestParam(value = "channelCode", defaultValue = "") String channelCode,
                                                               @RequestParam(value = "channelName", defaultValue = "") String channelName,
                                                               @RequestParam(value = "companyName", defaultValue = "") String companyName,
                                                               @RequestParam(value = "mobile", defaultValue = "") String mobile,
                                                               @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                               @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {

        Pagination<ChannelVO> channelVOPagination = bizQueryFeign.queryChannel(channelCode,
                channelName, companyName, mobile, pageIndex, pageSize).getData();
        return FintechResponse.responseData(channelVOPagination);
    }

    @Override
    public FintechResponse<ChannelVO> getChannelDetail(@RequestParam(value = "id") Integer id) {
        if (null == id) {
            throw new FInsuranceBaseException(104515);
        }
        return FintechResponse.responseData(channelService.getDetail(id));
    }

    @Override
    public FintechResponse<VoidPlaceHolder> freezeChannel(@Validated({Update.class}) @RequestBody(required = false) ChannelVO channelVO) {
        if (null == channelVO || null == channelVO.getId()) {
            throw new FInsuranceBaseException(104101);
        }
        Boolean isLocked = true;
        channelService.freezeOrUnfreeze(channelVO.getId(), isLocked);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }


    @Override
    public FintechResponse<VoidPlaceHolder> unfreezeChannel(@Validated({Update.class}) @RequestBody(required = false) ChannelVO channelVO) {
        if (null == channelVO || null == channelVO.getId()) {
            throw new FInsuranceBaseException(104101);
        }
        Boolean isLocked = false;
        channelService.freezeOrUnfreeze(channelVO.getId(), isLocked);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    @RequestMapping(value = "/waiting-add-channel/list", method = RequestMethod.GET)
    public FintechResponse<List<ChannelVO>> queryWaitingForAddChannel(@RequestParam(value = "productId", required = false) Integer productId) {
        List<ChannelVO> voList = channelService.queryChannelByProductIdAndBelongFlag(productId, false);
        return FintechResponse.responseData(voList);
    }

    @Override
    @RequestMapping(value = "/product-channel/add", method = RequestMethod.POST)
    public FintechResponse<VoidPlaceHolder> addProductChannel(@Validated(Save.class) @RequestBody ProductChannelVO pcVO) {
        channelService.addProductChannel(pcVO);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    @RequestMapping(value = "/product-channel/delete", method = RequestMethod.POST)
    public FintechResponse<VoidPlaceHolder> deleteProductChannel(@Validated(Update.class) @RequestBody ProductChannelVO pcVO) {
        channelService.deleteProductChannel(pcVO);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<Pagination<ChannelVO>> findCustomerChannelByChannelCodes(@RequestBody List<String> channelCodeList) {
        if(channelCodeList == null || channelCodeList.size() < 1){
            throw new FInsuranceBaseException(104109);
        }
        Pagination<ChannelVO> channelList = channelService.findChannelByChannelCodes(channelCodeList, 1, Integer.MAX_VALUE);
        return FintechResponse.responseData(channelList);
    }

    @Override
    public FintechResponse<ChannelVO> getChannelDetailByChannelCode(@RequestParam(value = "channelCode", required = true) String channelCode) {
        if (StringUtils.isBlank(channelCode)) {
            throw new FInsuranceBaseException(104515);
        }
        return FintechResponse.responseData(channelService.getChannelDetailByChannelCode(channelCode));
    }

    @Override
    public FintechResponse<ChannelVO> getAcquiescenceChannelByRequisitionIdAndCustomerId(@RequestParam(value = "requisitionId" ,required = false) Integer requisitionId, @RequestParam(value = "customerId",required = true) Integer customerId) {
        ChannelVO channelVO = null;
        //查询上次已经使用的渠道
        RequisitionVO requisition = requisitionService.getAcquiescenceChannelByRequisitionIdAndCustomerId(requisitionId, customerId);
        if (requisition != null) {
            channelVO = channelService.getChannelDetailByChannelCode(requisition.getChannelCode());
        }
        if(channelVO != null && channelVO.getIsLocked().equals(0)){
            return FintechResponse.responseData(channelVO);
        }
        FintechResponse<List<String>> listFintechResponse = customerServiceFeign.findCustomerChannelCodeByCustomerId(customerId);
        if(!listFintechResponse.isOk() ){
            LOG.error("getAcquiescenceChannelByRequisitionIdAndCustomerId error listFintechResponse with customerId=[" + customerId + "]");
            throw FInsuranceBaseException.buildFromErrorResponse(listFintechResponse);
        }
        if(listFintechResponse.getData() == null || listFintechResponse.getData().size() < 1){
            LOG.error("getAcquiescenceChannelByRequisitionIdAndCustomerId null listFintechResponse with customerId=[" + customerId + "]");
            throw new FInsuranceBaseException(107050);
        }
        //渠道code集合
        List<String> list = new ArrayList<String>();
        list.addAll(listFintechResponse.getData());
        if(list != null && list.size() > 0) {
            Pagination<ChannelVO> channelVOPagination = channelService.findChannelByChannelCodes(list, 1, Integer.MAX_VALUE);
            if (channelVOPagination != null && !channelVOPagination.getItems().isEmpty()) {
                channelVO = channelVOPagination.getItems().get(0);
            }
        }
        return FintechResponse.responseData(channelVO);
    }
}
