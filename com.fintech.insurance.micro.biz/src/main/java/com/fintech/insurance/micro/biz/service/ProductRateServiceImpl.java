package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.enums.InterestType;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.biz.persist.dao.ProductRateDao;
import com.fintech.insurance.micro.biz.persist.entity.Product;
import com.fintech.insurance.micro.biz.persist.entity.ProductRate;
import com.fintech.insurance.micro.biz.persist.entity.Requisition;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import com.fintech.insurance.micro.dto.biz.ProductRateVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: (产品利率管理)
 * @Author: yongneng liu
 * @Date: 2017/11/14 0014 16:08
 */
@Service
@Transactional
public class ProductRateServiceImpl implements ProductRateService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductRateServiceImpl.class);

    @Autowired
    private ProductRateDao productRateDao;

    @Autowired
    private RequisitionService requisitionService;

    @Override
    @Transactional(readOnly = true)
    public ProductRateVO getProductRateVOById(Integer id) {
        if (id == null) {
            return null;
        }
        ProductRate rate = productRateDao.getProductRateById(id);
        return getVOByEntity(rate);
    }

    @Override
    public void save(Product product, List<ProductRateVO> productRateVOList) {
        if (product == null || productRateVOList == null || productRateVOList.size() < 1) {
            throw new FInsuranceBaseException(104106);
        }
        for(ProductRateVO rateVO : productRateVOList){
            ProductRate rate = new ProductRate();
            rate.setProduct(product);
            rate.setBusinessDuration(rateVO.getBusinessDuration());
            rate.setInterestType(rateVO.getInterestType() == null ? InterestType.INTEREST_BY_MONTHS.getCode() : rateVO.getInterestType());
            rate.setInterestRate(rateVO.getInterestRate());
            rate.setLoanRatio(rateVO.getLoanRatio());
            productRateDao.save(rate);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductRateVO> findProductRateVOByProductId(Integer productId) {
        List<ProductRate> list = productRateDao.findAllProductRateByProductId(productId);
        if(list == null || list.size() < 1){
            return null;
        }
        List<ProductRateVO> voList = new ArrayList<>();
        for(ProductRate rate : list){
            voList.add(getVOByEntity(rate));
        }
        return voList;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductRate getProductRate(RequisitionDetail requisitionDetail, Integer month) {
        Requisition requisition = requisitionDetail.getRequisition();
        if(StringUtils.equals(requisition.getProductType(), ProductType.CAR_INSTALMENTS.getCode()) &&
                !requisitionDetail.getCommercialInsuranceStart().after(DateCommonUtils.getToday())){
            throw new FInsuranceBaseException(104928);
        }

        Date beginDate = requisitionDetail.getCommercialInsuranceStart().before(DateCommonUtils.getToday()) ? DateCommonUtils.getToday() : requisitionDetail.getCommercialInsuranceStart();

        int maxDuration = requisitionService.computeMaxAvaiableInstalmentNum(beginDate, requisitionDetail.getCommercialInsuranceEnd(), requisition);
        requisitionDetail.setMaxDuration(maxDuration);

        Integer businessDuration = maxDuration;
        if(month != null && month > 0 ) {
            businessDuration = maxDuration < month ? maxDuration : month;
        }
        ProductRate rate = null;
        for(ProductRate productRate : requisitionDetail.getRequisition().getProduct().getProductRateSet()){
            if(productRate.getBusinessDuration().equals(businessDuration)){
                rate = productRate;
                break;
            }
        }
        return rate;
    }

    //实体转VO
    private ProductRateVO getVOByEntity(ProductRate entity) {
        if (entity == null) {
            return null;
        }
        ProductRateVO vo = new ProductRateVO();
        vo.setId(entity.getId());
        vo.setBusinessDuration(entity.getBusinessDuration());
        vo.setInterestType(entity.getInterestType());
        vo.setInterestRate(entity.getInterestRate());
        vo.setLoanRatio(entity.getLoanRatio());
        return vo;
    }
}
