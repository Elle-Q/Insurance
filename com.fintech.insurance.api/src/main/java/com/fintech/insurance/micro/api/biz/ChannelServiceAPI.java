package com.fintech.insurance.micro.api.biz;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import com.fintech.insurance.micro.dto.biz.ProductChannelVO;
import com.fintech.insurance.micro.dto.validate.groups.Save;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping(value = "/biz/channel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface ChannelServiceAPI {

    /**
     * 创建编辑渠道
     * @param channelVO
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    FintechResponse<Integer> saveChannel(@Validated(Save.class) @RequestBody ChannelVO channelVO);

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
                                       @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);

    /**
     * 查看渠道详情
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    FintechResponse<ChannelVO> getChannelDetail(@RequestParam(value = "id") Integer id);


    /**
     * 冻结渠道
     */
    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> freezeChannel(@Validated({Update.class}) @RequestBody(required = false) ChannelVO channelVO);

    /**
     * 解冻渠道
     * @param channelVO
     */
    @RequestMapping(value = "/unfreeze", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> unfreezeChannel(@Validated({Update.class}) @RequestBody(required = false) ChannelVO channelVO);

    /**
     * 查询待增加的渠道
     * @param productId 产品id
     * @return
     */
    @RequestMapping(value = "/waiting-add-channel/list", method = RequestMethod.GET)
    public FintechResponse<List<ChannelVO>> queryWaitingForAddChannel(@RequestParam(value = "productId", required = false) Integer productId);

    /**
     * 新增产品渠道
     * @param pcVO 产品渠道VO
     * @return
     */
    @RequestMapping(value = "/product-channel/add", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> addProductChannel(@Validated(Save.class) @RequestBody ProductChannelVO pcVO);

    /**
     * 删除产品渠道
     * @param pcVO 产品渠道VO
     * @return
     */
    @RequestMapping(value = "/product-channel/delete", method = RequestMethod.POST)
    FintechResponse<VoidPlaceHolder> deleteProductChannel(@Validated(Update.class) @RequestBody ProductChannelVO pcVO);

    /**
     *WX获取用户渠道
     * @param channelCodeList 用户渠道code
     * @return
     */
    @RequestMapping(value = "/find-by-customer-id/page", method = RequestMethod.POST)
    public FintechResponse<Pagination<ChannelVO>> findCustomerChannelByChannelCodes(@RequestBody List<String> channelCodeList);

    /**
     * 查看渠道详情
     */
    @RequestMapping(value = "/get-channel-by-code", method = RequestMethod.GET)
    FintechResponse<ChannelVO> getChannelDetailByChannelCode(@RequestParam(value = "channelCode") String channelCode);

    /**
     * 查看渠道详情
     */
    @RequestMapping(value = "/get-acquiescence-channel-by-customer", method = RequestMethod.GET)
    FintechResponse<ChannelVO> getAcquiescenceChannelByRequisitionIdAndCustomerId(@RequestParam(value = "requisitionId" ,required = false) Integer requisitionId, @RequestParam(value = "customerId" ,required = true) Integer customerId);
}
