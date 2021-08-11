package com.fintech.insurance.micro.biz.controller;

import com.fintech.insurance.commons.constants.VoidPlaceHolder;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.components.web.context.FInsuranceApplicationContext;
import com.fintech.insurance.micro.api.biz.ProductBusinessServiceAPI;
import com.fintech.insurance.micro.biz.service.ChannelService;
import com.fintech.insurance.micro.biz.service.ProductService;
import com.fintech.insurance.micro.biz.service.RequisitionService;
import com.fintech.insurance.micro.dto.biz.*;
import com.fintech.insurance.micro.dto.validate.groups.Save;
import com.fintech.insurance.micro.dto.validate.groups.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
public class ProductController extends BaseFintechController implements ProductBusinessServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private RequisitionService requisitionService;


    @Override
    public FintechResponse<Integer> saveProduct(@Validated(Save.class) @RequestBody ProductVO productVO) {
        if (productVO == null) {
            throw new FInsuranceBaseException(104101);
        }
        //获取当前登录用id
        Integer currentLoginUserId = this.getCurrentLoginUserId();
        if(currentLoginUserId == null){
            throw new FInsuranceBaseException(101506);
        }
        // 新增产品
        if (productVO.getId() == null){
            productVO.setCreateBy(currentLoginUserId);
        }
        productVO.setUpdateBy(currentLoginUserId);
        if(productVO.getChannelIds() == null || productVO.getChannelIds().length< 1){
            throw new FInsuranceBaseException(107041);
        }
        //产品利率
        List<ProductRateVO> productRateVOList = productVO.getProductRateVOList();
        //新增产品，产品利率不能空
        if (productVO.getId() == null && (productVO.getProductRateVOList() == null ||  productVO.getProductRateVOList().size() < 1)) {
            throw new FInsuranceBaseException(104106);
        }
        if (productVO.getId() == null && productRateVOList != null) {
            for (ProductRateVO rateVO : productRateVOList) {
                //验证利率的参数不能为空
                if (rateVO.getBusinessDuration() == null || rateVO.getLoanRatio() == null || rateVO.getInterestRate() == null) {
                    throw new FInsuranceBaseException(104106);
                }
            }
        }
        return FintechResponse.responseData(productService.save(productVO));
    }

    @Override
    public FintechResponse<VoidPlaceHolder> onShelveProduct(@Validated(Update.class) @RequestBody ProductVO productVO) {
        if (productVO == null || productVO.getId() == null) {
            throw new FInsuranceBaseException(104101);
        }
        productService.onShelves(productVO);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<VoidPlaceHolder> downShelveProduct(@Validated(Update.class) @RequestBody ProductVO productVO) {
        if (productVO == null || productVO.getId() == null) {
            throw new FInsuranceBaseException(104101);
        }
        productService.downShelves(productVO);
        return FintechResponse.responseData(VoidPlaceHolder.instance());
    }

    @Override
    public FintechResponse<ProductChannelDetailVO> getProductInfoById(@RequestParam(value = "id") Integer id) {
        ProductVO productVO = productService.getProductVOById(id);
        List<ChannelVO> channelList = channelService.queryChannelByProductIdAndBelongFlag(id, true);

        ProductChannelDetailVO detailVO = new ProductChannelDetailVO(productVO, channelList);

        return FintechResponse.responseData(detailVO);
    }

    @Override
    public FintechResponse<Pagination<ProductVO>> queryProduct(String productType, String productName, String repayDayType,
                                                               Integer isOnsale, @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                               @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        Pagination<ProductVO> page = productService.queryProduct(productType, productName, repayDayType, isOnsale, pageIndex, pageSize);
        return FintechResponse.responseData(page);
    }

    @Override
    public FintechResponse<ProductVO> findWeChatProductInfoById(Integer id) {
        ProductVO vo = productService.getProductVOById(id);
        return FintechResponse.responseData(vo);
    }

    @Override
    public FintechResponse<ChannelProductVO> queryWeChatProductInfoByChannelId(@RequestParam(value = "requisitionId", required =  false ) Integer requisitionId,
                                                                               @RequestParam(value = "channelId", required =  false ) Integer channelId,
                                                                               @RequestParam(value = "productType", required =  false) String productType,
                                                                               @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                               @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        //登录用户
        Integer currentLoginUserId = this.getCurrentLoginUserId();
        //是否为客户端
        boolean isCustomer = FInsuranceApplicationContext.isCustomer();
        ChannelVO channelVO = null;
        //获取最近使用的渠道信息
        if(channelId == null) {
            LOG.info("queryProductInfoByChannelId with requisitionId=[" + requisitionId + "],currentLoginUserId=[" + currentLoginUserId + "]");
            //查询上次已近使用的渠道
            channelVO = channelService.getAcquiescenceChannelByRequisitionIdAndCustomerId(requisitionId, currentLoginUserId );
        }else{
            channelVO = channelService.getDetail(channelId);
        }
        if(channelVO == null){
            LOG.info("queryProductInfoByChannelId null channelVO with requisitionId=[" + requisitionId + "],currentLoginUserId=[" + currentLoginUserId + "],channelId=[" + channelId + "]");
            throw new FInsuranceBaseException(107049);
        }
        //客户端选中渠道判断冻结
        if(isCustomer && channelVO.getIsLocked().equals(1)){
            throw new FInsuranceBaseException(107044);
        }
        //获取业务单的产品
        Integer productId = null;
        if(requisitionId != null){
            RequisitionVO requisitionVO =  requisitionService.getRequisitionById(requisitionId);
            productId = requisitionVO == null ? null : requisitionVO.getProductId();
        }
        Pagination<SimpleProductVO> page = productService.queryWeChatProductInfoByChannelId( channelVO.getId(), productType, pageIndex, pageSize);
        ChannelProductVO channelProductVO = new ChannelProductVO();
        channelProductVO.setChannelCode(channelVO.getChannelCode());
        channelProductVO.setChannelId(channelVO.getId());
        channelProductVO.setProductId(productId);
        channelProductVO.setChannelName(channelVO.getChannelName());
        channelProductVO.setSimpleProductVOList(page == null ? null : page.getItems());
        return FintechResponse.responseData(channelProductVO);
    }


}
