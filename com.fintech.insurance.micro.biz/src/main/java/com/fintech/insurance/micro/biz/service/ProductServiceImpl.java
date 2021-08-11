package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.enums.InterestType;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RepayDayType;
import com.fintech.insurance.commons.utils.NumberFormatorUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.biz.persist.dao.ProductDao;
import com.fintech.insurance.micro.biz.persist.entity.Channel;
import com.fintech.insurance.micro.biz.persist.entity.Product;
import com.fintech.insurance.micro.biz.persist.entity.ProductRate;
import com.fintech.insurance.micro.dto.biz.ProductRateVO;
import com.fintech.insurance.micro.dto.biz.ProductVO;
import com.fintech.insurance.micro.dto.biz.SimpleProductVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: (产品管理)
 * @Author: yongneng liu
 * @Date: 2017/11/14 0014 16:08
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private static final String PRODUCT_KEY ="product_key_";//产品缓存的key


    @Autowired
    private ProductDao productDao;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    @Transactional(readOnly = true)
    public ProductVO getProductVOById(Integer id) {
        if (id == null) {
            return null;
        }
        Product product = productDao.getProductById(id);
        if(product == null){
            throw new FInsuranceBaseException(104102);
        }
        ProductVO productVO = getVOByEntity(product);
        return productVO;
    }

    @Override
    public Integer save(ProductVO productVO) {
        if (productVO == null) {
            throw new FInsuranceBaseException(104101);
        }
        //产品id
        Integer id = productVO.getId();
        //根据名称查询产品集合
        if (id == null) {
            List<Product> oldProductList = null;
            if(StringUtils.isNoneBlank(productVO.getProductName())){
                oldProductList = productDao.findProductByProductName(productVO.getProductName());
            }
            if(oldProductList != null && oldProductList.size() > 0){
                throw new FInsuranceBaseException(104110);
            }
        }

        //产品
        Product product = null;
        if (id != null) {
            // 更新产品信息
            product = productDao.getProductById(id);
        }
        if (id != null && product == null) {
            throw new FInsuranceBaseException(104102);
        }
        //产品类型
        ProductType productType = ProductType.codeOf(productVO.getProductType());
        if(id == null && productType == null){
            throw new FInsuranceBaseException(104108);
        }
        if (product == null) {
            // 新增产品
            product = new Product();
        }
        //设置产品渠道
        setChannelSet(product, productVO);
        product.setProductName(productVO.getProductName());
        if(id == null && productType != null) {
            //设置产品code
            this.setProductCode(product, productType);
        }
        //产品类型和上架类型不允许编辑的时候改
        if(id == null) {
            product.setOnsale(true);
            product.setProductType(productVO.getProductType());
            product.setCreateAt(Calendar.getInstance().getTime());
            product.setCreateBy(productVO.getCreateBy() == null ? -1 : productVO.getCreateBy());
        }else{
            product.setUpdateAt(Calendar.getInstance().getTime());
            product.setUpdateBy(productVO.getUpdateBy() == null ? -1 : productVO.getUpdateBy());
        }
        product.setRepayType(productVO.getRepayType());
        if(StringUtils.equals(RepayDayType.INITIAL_PAYMENT.getCode(),productVO.getRepayDayType())) {
            product.setPrepaymentDays(0);
        }else{
            product.setPrepaymentDays(productVO.getPrepaymentDays());
        }
        if(StringUtils.isNoneBlank(productVO.getProductBanner())){
            product.setProductBanner(productVO.getProductBanner());
        }
        product.setRepayDayType(productVO.getRepayDayType());
        product.setServiceFeeRate(productVO.getServiceFeeRate());
        product.setOtherFeeRate(productVO.getOtherFeeRate());
        product.setPrepaymentPenaltyRate(productVO.getPrepaymentPenaltyRate());
        product.setPrepaymentDays(productVO.getPrepaymentDays());
        product.setOverdueFineRate(productVO.getOverdueFineRate());
        product.setMaxOverdueDays(productVO.getMaxOverdueDays());
        product.setProductDescription(productVO.getProductDescription());
        if(id == null) {
            //保存产品利率
            saveProductRate( product,  productVO.getProductRateVOList());
        }
        productDao.save(product);
        return product.getId();
    }

    //保存产品利率
    private void saveProductRate(Product product, List<ProductRateVO> productRateVOList) {
        if (product == null || productRateVOList == null || productRateVOList.size() < 1) {
            throw new FInsuranceBaseException(104106);
        }
        Set<ProductRate> productRates = new HashSet<ProductRate>();
        for(ProductRateVO rateVO : productRateVOList){
            ProductRate rate = new ProductRate();
            rate.setProduct(product);
            rate.setBusinessDuration(rateVO.getBusinessDuration());
            rate.setInterestType(rateVO.getInterestType() == null ? InterestType.INTEREST_BY_MONTHS.getCode() : rateVO.getInterestType());
            rate.setInterestRate(rateVO.getInterestRate());
            rate.setLoanRatio(rateVO.getLoanRatio());
            productRates.add(rate);
        }
        product.setProductRateSet(productRates);
    }

    //设置产品的渠道
    private synchronized void setChannelSet(Product product,ProductVO productVO){
        Integer[] channelIds = productVO.getChannelIds();
        if(channelIds == null || channelIds.length < 1){
            product.setChannelSet(new HashSet<Channel>());
            return;
        }
        //待保存的数据
        List<Integer> addIdList = new ArrayList<Integer>();
        for(int i=0;i<channelIds.length;i++) {
            addIdList.add(channelIds[i]);
        }
        //待新增渠道
        List<Channel> channelList = channelService.findChannelVOByIds(addIdList);
        if(channelList == null && channelList.size() < 1){
            product.setChannelSet(new HashSet<Channel>());
            return;
        }
        //旧的数据
        List<Integer> oldIdList = new ArrayList<Integer>();
        Set<Channel> channelSet = product.getChannelSet();
        if(channelSet == null){
            channelSet = new HashSet<Channel>();
        }else{
            for(Channel channel:channelSet){
                oldIdList.add(channel.getId());
            }
        }
        //待删除的数据
        List<Integer> deleteIdList = new ArrayList<Integer>();
        for(int i=0; i<oldIdList.size(); i++){
            if(!addIdList.contains(oldIdList.get(i))){
                deleteIdList.add(oldIdList.get(i));
            }
        }
        Iterator<Channel> it = channelSet.iterator();
        //删除数据
        while (it.hasNext()){
            Channel c = it.next();
            if(deleteIdList.contains(c.getId())){
                it.remove();
            }
        }
        for(Channel c : channelList){
            //新增数据
            if(!channelSet.contains(c)){
                channelSet.add(c);
            }
        }
        product.setChannelSet(channelSet);
    }

    //设置产品的编号
    private void setProductCode(Product product, ProductType productType){
        product.setProductCode(getNextProductCode(productType));
    }
    //获取产品的编号
    private String getNextProductCode( ProductType productType){
        Long value = getProductSizeByType(productType);
        String str = productType.getAbbreviate() + StringUtils.leftPad(NumberFormatorUtils.Parse34Encode(value), 2, "0");
        List<Product> productList = productDao.findProductByProductCode(str);
        if(productList != null && productList.size() > 0){
            return getNextProductCode(productType);
        }
        return str;
    }

    //获取同类型的产品数量
    private Long getProductSizeByType(ProductType productType){
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        String key = PRODUCT_KEY + productType.getAbbreviate();
        Long value = (Long)ops.get(key);
        if(value == null){
            Long size = productDao.countProductByProductType(productType.getCode());
            value = size == null ? 1 : size + 1;
        }else{
            value += 1;
        }
        ops.set(key, value, BasicConstants.QINIU_EXPIRED_MINUTES, TimeUnit.HOURS);
        return value;
    }

    @Override
    public void onShelves(ProductVO productVO) {
        if (productVO == null || productVO.getId() == null) {
            throw new FInsuranceBaseException(104101);
        }
        Integer id = productVO.getId();
        Product product = null;
        if (id != null) {
            product = productDao.getProductById(id);
        }

        if (id != null && product == null) {
            throw new FInsuranceBaseException(104102);
        }
        product.setOnsale(true);
        productDao.save(product);
    }

    @Override
    public void downShelves(ProductVO productVO) {
        if (productVO == null || productVO.getId() == null) {
            throw new FInsuranceBaseException(104101);
        }
        Integer id = productVO.getId();
        Product product = productDao.getProductById(id);
        if (product == null) {
            throw new FInsuranceBaseException(104102);
        }
        product.setOnsale(false);
        productDao.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<ProductVO> queryProduct(String productTypeStr, String productName, String repayDayTypeStr,
                                              Integer isOnsale, int pageIndex, int pageSize) {
        ProductType productType = null;
        if (StringUtils.isNoneBlank(productTypeStr)) {
            productType = ProductType.codeOf(productTypeStr);
        }
        RepayDayType repayDayType = null;
        if (StringUtils.isNoneBlank(repayDayTypeStr)) {
            repayDayType = RepayDayType.codeOf(repayDayTypeStr);
        }
        Boolean flag = null;
        if (isOnsale != null && isOnsale == 1) {
            flag = true;
        } else if (isOnsale != null && isOnsale == 0) {
            flag = false;
        }
        Page<Product> productPage = productDao.query(productType, productName, repayDayType, flag, pageIndex, pageSize);
        List<ProductVO> productVOList = new ArrayList<ProductVO>();
        if(productPage != null && productPage.getContent() != null){
            for(Product product : productPage.getContent()){
                productVOList.add(getVOByEntity(product));
            }
        }
        Pagination<ProductVO> page = Pagination.createInstance(pageIndex,pageSize,productPage.getTotalElements(),productVOList);
        return page;
    }

    /**
     * 将产品利率配置转换为MAP数据类型输出
     * @param product
     * @return
     */
    @Override
    public Map<String, ProductRate> getProductRate(Product product) {
        if (product == null || product.getProductRateSet() == null) {
            return Collections.emptyMap();
        } else {
            Map<String, ProductRate> ratesMap = new HashMap<>();
            Set<ProductRate> rates = product.getProductRateSet();
            for (ProductRate rate : rates) {
                if (rate != null) {
                    ratesMap.put(String.valueOf(rate.getBusinessDuration()), rate);
                }
            }
            return ratesMap;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<SimpleProductVO> queryWeChatProductInfoByChannelId(Integer channelId, String productType, Integer pageIndex, Integer pageSize) {
        if (channelId == null) {
            return null;
        }
        Page<Product> productPage = productDao.queryWeChatProductInfoByChannelId(channelId, productType, pageIndex, pageSize);
        List<SimpleProductVO> productVOList = new ArrayList<SimpleProductVO>();
        if(productPage != null && productPage.getContent() != null){
            for(Product product : productPage.getContent()){
                productVOList.add(getWeChatSimpleProductVOByEntity(product));
            }
        }
        Pagination<SimpleProductVO> page = Pagination.createInstance(pageIndex,pageSize,productPage.getTotalElements(),productVOList);
        return page;
    }

    private SimpleProductVO getWeChatSimpleProductVOByEntity(Product entity) {
        if (entity == null) {
            return null;
        }
        SimpleProductVO p = new SimpleProductVO();
        p.setId(entity.getId());
        //产品名称
        p.setProductName(entity.getProductName());
        p.setProductType(entity.getProductType());
        p.setRepayType(entity.getRepayType());
        p.setRepayDayType(entity.getRepayDayType());
        p.setServiceFeeRate(entity.getServiceFeeRate() + entity.getOtherFeeRate());

        Set<ProductRate> productRates = entity.getProductRateSet();
        if (productRates != null) {
            ProductRate[] rates = productRates.toArray(new ProductRate[productRates.size()]);
            // 从大到小排列
            Arrays.sort(rates, new Comparator<ProductRate>() {
                @Override
                public int compare(ProductRate o1, ProductRate o2) {
                    if (o1.getInterestRate() == null) {
                        return -1;
                    }
                    if (o2.getInterestRate() == null) {
                        return 1;
                    }
                    if (o1.getInterestRate() < o2.getInterestRate()) {
                        return -1;
                    } else if (o1.getInterestRate() > o2.getInterestRate()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            p.setMinInterestRate(rates[0].getInterestRate());
            p.setMaxInterestRate(rates[rates.length-1].getInterestRate());
        }
        return p;
    }



    //实体转VO
    private ProductVO getVOByEntity(Product entity) {
        if (entity == null) {
            return null;
        }
        ProductVO vo = new ProductVO();
        vo.setId(entity.getId());
        vo.setProductName(entity.getProductName());
        vo.setProductCode(entity.getProductCode());
        vo.setProductType(entity.getProductType());
        vo.setProductIcon(entity.getProductIcon());
        vo.setProductBanner(entity.getProductBanner());
        vo.setIsOnsale(entity.getOnsale() != null && entity.getOnsale() ? 1 : 0);
        vo.setRepayType(entity.getRepayType());
        vo.setRepayDayType(entity.getRepayDayType());
        vo.setServiceFeeRate(entity.getServiceFeeRate());
        vo.setOtherFeeRate(entity.getOtherFeeRate());
        vo.setPrepaymentPenaltyRate(entity.getPrepaymentPenaltyRate());
        vo.setPrepaymentDays(entity.getPrepaymentDays());
        vo.setOverdueFineRate(entity.getOverdueFineRate());
        vo.setMaxOverdueDays(entity.getMaxOverdueDays());
        vo.setProductDescription(entity.getProductDescription());
        vo.setCreateBy(entity.getCreateBy());
        vo.setCreateAt(entity.getCreateAt());
        vo.setUpdateBy(entity.getUpdateBy());
        vo.setUpdateBy(entity.getUpdateBy());
        //产品利率集合
        List<ProductRateVO> productRateVOList = null;
        Set<ProductRate> productRateSet = entity.getProductRateSet();
        if(productRateSet != null && productRateSet.size() > 0){
            productRateVOList = new ArrayList<ProductRateVO>();
            for(ProductRate rate : productRateSet){
                ProductRateVO rateVO = new ProductRateVO();
                rateVO.setId(rate.getId());
                //业务期数
                rateVO.setBusinessDuration(rate.getBusinessDuration());
                //计息类型，按月或者按日计息
                rateVO.setInterestType(rate.getInterestType());
                //利率点（万倍）
                rateVO.setInterestRate(rate.getInterestRate());
                //可借比例（万倍）
                rateVO.setLoanRatio(rate.getLoanRatio());
                productRateVOList.add(rateVO);
            }
        }
        vo.setProductRateVOList(productRateVOList);
        return vo;
    }
}
