package com.fintech.insurance.micro.api.biz;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.biz.ChannelProductVO;
import com.fintech.insurance.micro.dto.biz.ProductChannelDetailVO;
import com.fintech.insurance.micro.dto.biz.ProductVO;
import com.fintech.insurance.micro.dto.biz.SimpleProductVO;
import com.fintech.insurance.micro.dto.validate.groups.Find;
import com.fintech.insurance.micro.dto.validate.groups.Save;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

@RequestMapping(value = "/biz/product", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public interface ProductBusinessServiceAPI {

    //保存产品
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public FintechResponse<Integer> saveProduct(@Validated(Save.class) @RequestBody ProductVO productVO) ;

    //上架
    @RequestMapping(value = "/on-shelves", method = RequestMethod.POST)
    public FintechResponse<VoidPlaceHolder> onShelveProduct(@Validated(Find.class) @RequestBody ProductVO productVO) ;

    //下架
    @RequestMapping(value = "/down-shelves", method = RequestMethod.POST)
    public FintechResponse<VoidPlaceHolder> downShelveProduct(@Validated(Find.class) @RequestBody ProductVO productVO) ;

    //获取产品详情
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public FintechResponse<ProductChannelDetailVO> getProductInfoById(@RequestParam(value = "id") Integer id);

    //查询产品
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public FintechResponse<Pagination<ProductVO>> queryProduct(@RequestParam(value = "productType", defaultValue = "") String productType,
                                                               @RequestParam(value = "productName", defaultValue = "") String productName,
                                                               @RequestParam(value = "repayDayType", defaultValue = "") String repayDayType,
                                                               @RequestParam(value = "isOnsale", required = false) Integer isOnsale,
                                                               @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                               @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);

    //WX获取产品详情
    @RequestMapping(value = "/find-product-info", method = RequestMethod.GET)
    public FintechResponse<ProductVO> findWeChatProductInfoById(@RequestParam(value = "id") @NotNull Integer id);

    /**
     * WX获取产品列表
     * @param  requisitionId 业务id
     * @param channelId 渠道id
     * @param productType 产品类型
     * @param pageIndex 页数
     * @param pageSize 每页数
     * @return
     */
    @RequestMapping(value = "/query-by-channel-id/page", method = RequestMethod.GET)
    public FintechResponse<ChannelProductVO> queryWeChatProductInfoByChannelId(@RequestParam(value = "requisitionId", required =  false ) Integer requisitionId,
                                                                               @RequestParam(value = "channelId", required =  false ) Integer channelId,
                                                                               @RequestParam(value = "productType", required =  false) String productType,
                                                                               @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                               @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize);
}
