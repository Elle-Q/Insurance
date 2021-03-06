package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.constants.BasicConstants;
import com.fintech.insurance.commons.enums.ProductType;
import com.fintech.insurance.commons.enums.RequisitionStatus;
import com.fintech.insurance.commons.enums.ResourceType;
import com.fintech.insurance.commons.utils.CalculationFormulaUtils;
import com.fintech.insurance.commons.utils.DateCommonUtils;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.biz.persist.dao.ContractDao;
import com.fintech.insurance.micro.biz.persist.dao.RequisitionDao;
import com.fintech.insurance.micro.biz.persist.dao.RequisitionDetailDao;
import com.fintech.insurance.micro.biz.persist.dao.RequisitionDetailResourceDao;
import com.fintech.insurance.micro.biz.persist.entity.*;
import com.fintech.insurance.micro.dto.biz.*;
import com.fintech.insurance.micro.dto.thirdparty.ImageVO;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.QiniuBusinessServiceFeign;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: (some words)
 * @Author: qxy
 * @Date: 2017/11/14 0014 16:10
 */
@Service
@Transactional
public class RequisitionDetailServiceImpl implements RequisitionDetailService {


    private static final Logger LOG = LoggerFactory.getLogger(RequisitionDetailServiceImpl.class);

    @Autowired
    RequisitionDetailDao requisitionDetailDao;

    @Autowired
    RequisitionDetailResourceDao requisitionDetailResourceDao;

    @Autowired
    RequisitionDao requisitionDao;

    @Autowired
    RequisitionService requisitionService;

    @Autowired
    ContractDao contractDao;

    @Autowired
    RequisitionDetailResourceService requisitionDetailResourceService;

    @Autowired
    QiniuBusinessServiceFeign qiniuBusinessServiceFeign;

    @Autowired
    CustomerServiceFeign customerServiceFeign;

    @Autowired
    ChannelService channelService;

    @Autowired
    ProductService productService;


    //???????????????????????????
    private void checkoutCustomerIsLocked(Integer customerId, String channelCode){
        if(customerId == null){
            LOG.error("checkoutCustomerIsLocked failed with null customerId");
            throw new FInsuranceBaseException(107031);
        }
        FintechResponse<Boolean> fintechResponse = customerServiceFeign.getCustomerLockedStatusById(customerId);
        if(!fintechResponse.isOk()){
            LOG.error("checkoutCustomerIsLocked failed with error code=["+ fintechResponse.getCode() +"]" );
            throw new FInsuranceBaseException(fintechResponse.getCode());
        }
        if(fintechResponse.getData() == null || fintechResponse.getData()){
            LOG.error("checkoutCustomerIsLocked failed with error customerId=["+ customerId +"]" );
            throw new FInsuranceBaseException(107032);
        }
        if(StringUtils.isNoneBlank(channelCode)){
            ChannelVO channelVO = channelService.getChannelDetailByChannelCode(channelCode);
            if(channelVO == null){
                LOG.error("checkoutCustomerIsLocked failed  null ChannelVO with  channelCode=["+ channelCode +"]" );
                throw new FInsuranceBaseException(107039);
            }
            if(channelVO.getIsLocked().equals(1)) {
                LOG.error("checkoutCustomerIsLocked failed ChannelVO is locked with  channelCode=["+ channelCode +"]" );
                throw new FInsuranceBaseException(107044);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionDetailVO listRequisitionDetail(Integer contractId) {
        Contract contract = contractDao.getById(contractId);
        List<RequisitionDetail> requisitionDetailList = requisitionDetailDao.listRequisitionDetail(contractId);
        RequisitionDetail requisitionDetail = requisitionDetailList == null || requisitionDetailList.size() < 1 ? null : requisitionDetailList.get(0);
        RequisitionDetailVO requisitionDetailVO = new RequisitionDetailVO();
        if (null != requisitionDetail) {
            Requisition requisition = requisitionDao.getById(requisitionDetail.getRequisition().getId());
            Set<RequisitionDetailResource> requisitionDetailResources = requisitionDetail.getRequisitionDetailResourceSet();
            requisitionDetailVO = this.toRequisitionDetailVO(requisitionDetail, requisitionDetailResources);
            if (requisitionDetailVO != null) {
                if (null != requisition) {
                    requisitionDetailVO.setProductType(requisition.getProductType());
                }
                if (null != contract) {
                    requisitionDetailVO.setDuration(contract.getBusinessDuration());
                    requisitionDetailVO.setBorrowAmount(contract.getContractAmount().doubleValue());
                }
            }
        }
        return requisitionDetailVO;
    }

    //?????????VO
    private RequisitionDetailVO toRequisitionDetailVO(RequisitionDetail requisitionDetail, Set<RequisitionDetailResource> requisitionDetailResources) {
        RequisitionDetailVO vo = new RequisitionDetailVO();
        if (null == requisitionDetail) {
            return null;
        }
        List<RequisitionDetailResourceVO> requisitionDetailResourceVOS = new ArrayList<>();
        if (null != requisitionDetailResources && requisitionDetailResources.size() > 0){//?????????????????????
            for (RequisitionDetailResource r : requisitionDetailResources) {
                RequisitionDetailResourceVO resourceVO = new RequisitionDetailResourceVO();
                resourceVO.setId(r.getId());
                resourceVO.setResourceType(r.getResourceType());
                resourceVO.setResouceName(r.getResouceName());
                resourceVO.setResourcePicture(r.getResourcePicture());
                requisitionDetailResourceVOS.add(resourceVO);
            }
            vo.setResource(requisitionDetailResourceVOS);
        }
        vo.setId(requisitionDetail.getId());
        vo.setDrivingLicense(requisitionDetail.getDrivingLicense());
        vo.setCommercialInsuranceAmount(requisitionDetail.getCommercialInsuranceAmount().doubleValue());
        vo.setCommercialInsuranceNumber(requisitionDetail.getCommercialInsuranceNumber());
        vo.setCommercialInsuranceStart(requisitionDetail.getCommercialInsuranceStart());
        vo.setCommercialInsuranceEnd(requisitionDetail.getCommercialInsuranceEnd());
        vo.setCompulsoryInsuranceAmount(requisitionDetail.getCompulsoryInsuranceAmount().doubleValue());
        vo.setCompulsoryInsuranceNumber(requisitionDetail.getCompulsoryInsuranceNumber());
        vo.setCompulsoryInsuranceStart(requisitionDetail.getCompulsoryInsuranceStart());
        vo.setCompulsoryInsuranceEnd(requisitionDetail.getCompulsoryInsuranceEnd());
        vo.setTaxAmount(requisitionDetail.getTaxAmount().doubleValue());
        vo.setDrivingLicenseMain(requisitionDetail.getDrivingLicenseMain());
        vo.setDrivingLicenseAttach(requisitionDetail.getDrivingLicenseAttach());
        vo.setCarNumber(requisitionDetail.getCarNumber());
        return vo;
    }

    //?????????VO
    private RequisitionDetailInfoVO toRequisitionDetailInfoVO(RequisitionDetail requisitionDetail, Set<RequisitionDetailResource> requisitionDetailResources) {
        RequisitionDetailInfoVO vo = new RequisitionDetailInfoVO();
        if (null == requisitionDetail) {
            return null;
        }
        //???????????????
        List<String> drivingLicenseResource = new ArrayList<String>();
        //????????????
       List<String> otherMaterial = new ArrayList<String>();
        //????????????????????????
         List<String> busiInsurance = new ArrayList<String>();
        //???????????????
         List<String> driveInsurance = new ArrayList<String>();
        //?????????
         List<String> taxResource = new ArrayList<String>();
        if (null != requisitionDetailResources && requisitionDetailResources.size() > 0){//?????????????????????
            for (RequisitionDetailResource r : requisitionDetailResources) {
                String resource = r.getResourcePicture();
                if(StringUtils.equals(ResourceType.BUSI_INSURANCE.getCode(),r.getResourceType())){
                    busiInsurance.add(resource);
                }else  if(StringUtils.equals(ResourceType.TAX_INSURANCE.getCode(),r.getResourceType())){
                    taxResource.add(resource);
                }else  if(StringUtils.equals(ResourceType.OTHER_MATERIAL.getCode(),r.getResourceType())){
                    otherMaterial.add(resource);
                }else  if(StringUtils.equals(ResourceType.BUSI_INSURANCE.getCode(),r.getResourceType())){
                    driveInsurance.add(resource);
                }else  if(StringUtils.equals(ResourceType.DRIVING_LICENSE.getCode(),r.getResourceType())){
                    drivingLicenseResource.add(resource);
                }
            }
        }
        vo.setOtherMaterial(otherMaterial);

        vo.setTaxResource(taxResource);

        vo.setDrivingLicenseResource(drivingLicenseResource);

        vo.setDriveInsurance(driveInsurance);

        vo.setBusiInsurance(busiInsurance);
        vo.setId(requisitionDetail.getId());
        vo.setDrivingLicense(requisitionDetail.getDrivingLicense());
        vo.setCommercialInsuranceAmount(requisitionDetail.getCommercialInsuranceAmount().doubleValue());
        vo.setCommercialInsuranceNumber(requisitionDetail.getCommercialInsuranceNumber());
        vo.setCommercialInsuranceStart(requisitionDetail.getCommercialInsuranceStart());
        vo.setCommercialInsuranceEnd(requisitionDetail.getCommercialInsuranceEnd());
        vo.setCompulsoryInsuranceAmount(requisitionDetail.getCompulsoryInsuranceAmount().doubleValue());
        vo.setCompulsoryInsuranceNumber(requisitionDetail.getCompulsoryInsuranceNumber());
        vo.setCompulsoryInsuranceStart(requisitionDetail.getCompulsoryInsuranceStart());
        vo.setCompulsoryInsuranceEnd(requisitionDetail.getCompulsoryInsuranceEnd());
        vo.setTaxAmount(requisitionDetail.getTaxAmount().doubleValue());
        vo.setCarNumber(requisitionDetail.getCarNumber());
        return vo;
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionDetailVO getRequisitionDetailById(Integer id) {
        RequisitionDetail requisitionDetail = requisitionDetailDao.getRequisitionDetailById(id);
        if(requisitionDetail == null){
            return null;
        }
        Set<RequisitionDetailResource> requisitionDetailResources = requisitionDetail.getRequisitionDetailResourceSet();
        return this.toRequisitionDetailVO(requisitionDetail, requisitionDetailResources);
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionDetailInfoVO getWXRequisitionDetailInfoById(Integer id) {
        RequisitionDetail requisitionDetail = requisitionDetailDao.getRequisitionDetailById(id);
        if(requisitionDetail == null){
            return null;
        }
        Set<RequisitionDetailResource> requisitionDetailResources = requisitionDetail.getRequisitionDetailResourceSet();
        return this.toRequisitionDetailInfoVO(requisitionDetail, requisitionDetailResources);
    }

    @Override
    public Integer saveRequisitionDetail(RequisitionDetailInfoVO requisitionDetailVO) {
        Requisition requisition = requisitionDao.getRequisitionById(requisitionDetailVO.getRequisitionId());
        if(requisition == null){
            throw new FInsuranceBaseException(104904, new Object[]{requisitionDetailVO.getRequisitionId()});
        }

        //??????????????????????????????
        if(requisition != null && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Draft.getCode())
                && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Rejected.getCode())
                && !StringUtils.equals(requisition.getRequisitionStatus(), RequisitionStatus.Canceled.getCode()) ){
            throw new FInsuranceBaseException(105930);
        }

        //???????????????????????????????????????????????????
        if(StringUtils.equals(ProductType.CAR_INSTALMENTS.getCode(), requisition.getProductType()) &&
                !requisitionDetailVO.getCommercialInsuranceStart().after(DateCommonUtils.getToday())){
            throw new FInsuranceBaseException(104928);
        }

        //????????????????????????
        this.checkoutCustomerIsLocked(requisition.getCustomerId(), requisition.getChannel().getChannelCode());

        if (null == requisition.getDetails()) { // ????????????
            requisition.setDetails(new HashSet<RequisitionDetail>());
        }
        Set<RequisitionDetail> requisitionDetails = requisition.getDetails();

        List<String> oldCarNumberList = new ArrayList<String>();
        for(RequisitionDetail detail : requisitionDetails){
            oldCarNumberList.add(detail.getCarNumber());
        }
        //??????id
        Integer id = requisitionDetailVO.getId();
        if(oldCarNumberList.contains(requisitionDetailVO.getCarNumber()) && id == null ){
            throw new FInsuranceBaseException(104931);
        }
        RequisitionDetail loaningRequisitionDetail = null;
        if(StringUtils.isNoneBlank(requisitionDetailVO.getCarNumber())) {
            loaningRequisitionDetail = requisitionDetailDao.findLoaningRequisitionDetailByCarNumber(requisitionDetailVO.getCarNumber());
        }
        if(loaningRequisitionDetail != null ){
            throw new FInsuranceBaseException(104932);
        }
        //????????????????????????
        RequisitionDetail requisitionDetail = saveOrUpdateRequsitionDetail(requisition, requisitionDetailVO);
        // ??????
        if(id == null) {
            requisition.getDetails().add(requisitionDetail);
        }
        //?????????????????????
        requisitionService.updateRequisition(requisition);
        requisitionDao.save(requisition);
        return requisitionDetail.getId();
    }


    //??????????????????
    private RequisitionDetail saveOrUpdateRequsitionDetail(Requisition requisition, RequisitionDetailInfoVO requisitionDetailVO){
        Integer id = requisitionDetailVO.getId();
        RequisitionDetail requisitionDetail = null;
        if(id != null){
            requisitionDetail = requisitionDetailDao.getRequisitionDetailById(id);
        }
        if(id != null && requisitionDetail == null){
            throw new FInsuranceBaseException(104927);
        }
        if(requisitionDetail == null){
            requisitionDetail = new RequisitionDetail();
        }
        //????????????
        ProductType productType = ProductType.codeOf(requisition.getProductType());

        if(productType == ProductType.CAR_INSTALMENTS &&
                !requisitionDetailVO.getCommercialInsuranceStart().after(DateCommonUtils.getToday())){
            throw new FInsuranceBaseException(104928);
        }
        Date beginDate = requisitionDetailVO.getCommercialInsuranceStart().before(DateCommonUtils.getToday()) ? DateCommonUtils.getToday() : requisitionDetailVO.getCommercialInsuranceStart();

        int maxDuration = requisitionService.computeMaxAvaiableInstalmentNum(beginDate, requisitionDetailVO.getCommercialInsuranceEnd(), requisition);
        requisitionDetail.setMaxDuration(maxDuration);

        Integer businessDuration =  maxDuration;
        if(requisition.getBusinessDuration() != null
                && requisition.getBusinessDuration() != 0 && maxDuration > requisition.getBusinessDuration()){
            businessDuration = requisition.getBusinessDuration();
        }
        requisitionDetail.setBusinessDuration(businessDuration);
        //????????????
        requisitionDetail.setDrivingLicense(requisitionDetailVO.getDrivingLicense());
        //???????????????????????????????????????
        requisitionDetail.setCommercialInsuranceNumber(requisitionDetailVO.getCommercialInsuranceNumber());
        //?????????????????????????????????
        requisitionDetail.setCommercialInsuranceAmount(requisitionDetailVO.getCommercialInsuranceAmount().intValue());
        //????????????????????????
        requisitionDetail.setCommercialInsuranceStart(requisitionDetailVO.getCommercialInsuranceStart());
        //????????????????????????
        requisitionDetail.setCommercialInsuranceEnd(requisitionDetailVO.getCommercialInsuranceEnd());
        //????????????????????????
        requisitionDetail.setCommercialInsuranceValue(CalculationFormulaUtils.getCommercialRisk(requisitionDetailVO.getCommercialInsuranceAmount().intValue(),beginDate, requisitionDetailVO.getCommercialInsuranceEnd(), productType).intValue());
        //????????????????????????????????????
        //????????????????????????????????????
        //??????????????????
        requisitionDetail.setCompulsoryInsuranceNumber(requisitionDetailVO.getCompulsoryInsuranceNumber());
        //??????????????????????????????
        requisitionDetail.setCompulsoryInsuranceAmount(requisitionDetailVO.getCompulsoryInsuranceAmount() == null ? 0 : requisitionDetailVO.getCompulsoryInsuranceAmount().intValue());
        //????????????????????????
        requisitionDetail.setCompulsoryInsuranceStart(requisitionDetailVO.getCompulsoryInsuranceStart());
        //????????????????????????
        requisitionDetail.setCompulsoryInsuranceEnd(requisitionDetailVO.getCompulsoryInsuranceEnd());
        //?????????
        requisitionDetail.setTaxAmount(requisitionDetailVO.getTaxAmount() == null ? 0 : requisitionDetailVO.getTaxAmount().intValue());
        requisitionDetail.setCarNumber(requisitionDetailVO.getCarNumber());
        //??????????????????????????????
        //????????????????????????????????????
        requisitionDetail.setCommercialOnly(StringUtils.isBlank(requisitionDetailVO.getCompulsoryInsuranceNumber()));
        requisitionDetail.setRequisition(requisition);
        requisitionDetailDao.save(requisitionDetail);
        requisitionDetail.setRequisitionDetailResourceSet(getRequisitionDetailResources(requisitionDetail, requisitionDetailVO));
        return requisitionDetail;
    }



    @Override
    @Transactional(readOnly = true)
    public RequisitionDetailInfoVO getRequisitionDetailInfoById(Integer id) {
        RequisitionDetail info = requisitionDetailDao.getRequisitionDetailById(id);
        return convertToVOByEntity(info);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<SimpleRequisitionDetailVO> findSimpleRequisitionDetailByRequisitionId(Integer requisitionId, Integer pageIndex, Integer pageSize) {
        Page<RequisitionDetail> page = requisitionDetailDao.findRequisitionDetailByRequisitionId(requisitionId, pageIndex, pageSize);
        List<SimpleRequisitionDetailVO> voList = new ArrayList<SimpleRequisitionDetailVO>();
        if(page.getContent() != null){
            for(int i = 0; i < page.getContent().size(); i++){
                SimpleRequisitionDetailVO detailVO = this.convertToSimpleRequisitionDetailByEntity(page.getContent().get(i));
                voList.add(detailVO);
            }
        }
        Pagination<SimpleRequisitionDetailVO> pagination = Pagination.createInstance(pageIndex, pageSize, page.getTotalElements(), voList);
        return pagination;
    }

    @Override
    public void deleteRequisitionDetailById(Integer id) {
        RequisitionDetail detail = requisitionDetailDao.getById(id);

        if(detail == null){
            LOG.info("Can not found the Requisition Detail for id:" + id );
            throw new FInsuranceBaseException(104933);
        }
        LOG.info("Get RequisitionDetail, id=" + detail.getId() + ", info:" + detail.toString());

        requisitionDetailDao.delete(detail);
        LOG.info("delete RequisitionDetail success, id=" + detail.getId());

        List<RequisitionDetailResource> oldList =  requisitionDetailResourceService.listRequisitionDetailResource(detail.getId(),null);
        List<String> list = new ArrayList<String>();
        if(oldList != null && oldList.size() > 0){
            for(RequisitionDetailResource resource : oldList){
                list.add(resource.getResourcePicture());
            }
        }
        deleteQiniuFile(list);
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionDetail findLoaningRequisitionDetailByCarNumber(String carNumber) {
        return requisitionDetailDao.findLoaningRequisitionDetailByCarNumber(carNumber);
    }

    @Override
    public void setRequisitionDetailCommercialInsuranceValue(RequisitionDetail requisitionDetail) {
        //????????????
        ProductType productType = ProductType.codeOf(requisitionDetail.getRequisition().getProductType());
        Date beiginTime = requisitionDetail.getCommercialInsuranceStart().before(DateCommonUtils.getToday()) ? DateCommonUtils.getToday() : requisitionDetail.getCommercialInsuranceStart();
        //????????????
        Double commercialRisk = CalculationFormulaUtils.getCommercialRisk(requisitionDetail.getCommercialInsuranceAmount(), beiginTime, requisitionDetail.getCommercialInsuranceEnd(), productType);
        requisitionDetail.setCommercialInsuranceValue( new BigDecimal(commercialRisk).intValue());
    }

    //??????????????????
    private Set<RequisitionDetailResource> getRequisitionDetailResources(RequisitionDetail requisitionDetail,RequisitionDetailInfoVO requisitionDetailVO){
        //?????????
        List<RequisitionDetailResource> oldList = new ArrayList<RequisitionDetailResource>();
        //????????????
        Set<RequisitionDetailResource> remainResourceSet = new HashSet<RequisitionDetailResource>();
        //????????????key
        List<String> remainResourceKeys = new ArrayList<String>();
        //??????????????????
        if(requisitionDetailVO != null && requisitionDetailVO.getId() != null){
            oldList = requisitionDetailResourceService.listRequisitionDetailResource(requisitionDetailVO.getId(),null);
        }
        List<String> oldResourceList = new ArrayList<String>();
        List<String> newResourceList = new ArrayList<String>();
        List<String> deleteResourceList = new ArrayList<String>();
        List<String> addResourceList = new ArrayList<String>();

        if(oldList != null && oldList.size()>0){
            for(RequisitionDetailResource resource : oldList){
                oldResourceList.add(resource.getResourcePicture());
            }
        }

        //???????????????
        List<String> drivingLicenseResourceMap = requisitionDetailVO.getDrivingLicenseResource();
        List<String> drivingLicenseResource = null;
        if(drivingLicenseResourceMap != null){
            drivingLicenseResource = new ArrayList<String>();
            for(int i = 0 ; i < drivingLicenseResourceMap.size() ; i++){
                drivingLicenseResource.add(drivingLicenseResourceMap.get(i));
            }
        }
        Map<String,Object> map = new HashMap<String,Object>();
        if(drivingLicenseResource != null && drivingLicenseResource.size() > 0){
            newResourceList.addAll(drivingLicenseResource);
            for(String str : drivingLicenseResource){
                map.put(str, ResourceType.DRIVING_LICENSE);
            }
        }
        //????????????
        List<String> otherMaterialMap = requisitionDetailVO.getOtherMaterial();
        List<String> otherMaterial = null;
        if(otherMaterialMap != null){
            otherMaterial = new ArrayList<String>();
            for(int i = 0 ; i < otherMaterialMap.size() ; i++){
                otherMaterial.add(otherMaterialMap.get(i));
            }
        }
        if(otherMaterial != null && otherMaterial.size() > 0){
            newResourceList.addAll(otherMaterial);
            for(String str : otherMaterial){
                map.put(str, ResourceType.OTHER_MATERIAL);
            }
        }
        //???????????????
        List<String> driveInsuranceMap = requisitionDetailVO.getDriveInsurance();
        List<String> driveInsurance = null;
        if(driveInsuranceMap != null){
            driveInsurance = new ArrayList<String>();
            for(int i = 0 ; i < driveInsuranceMap.size() ; i++){
                driveInsurance.add(driveInsuranceMap.get(i));
            }
        }
        if(driveInsurance != null && driveInsurance.size() > 0){
            newResourceList.addAll(driveInsurance);
            for(String str : driveInsurance){
                map.put(str, ResourceType.DRIVER_INSURANCE);
            }
        }
        //?????????
        List<String> taxResourceMap = requisitionDetailVO.getTaxResource();
        List<String> taxResource = null;
        if(taxResourceMap != null){
            taxResource = new ArrayList<String>();
            for(int i = 0 ; i < taxResourceMap.size() ; i++){
                taxResource.add(taxResourceMap.get(i));
            }
        }
        if(taxResource != null && taxResource.size() > 0){
            newResourceList.addAll(taxResource);
            for(String str : taxResource){
                map.put(str, ResourceType.TAX_INSURANCE);
            }
        }
        //????????????
        List<String> busiInsuranceMap = requisitionDetailVO.getBusiInsurance();
        List<String> busiInsurance = null;
        if(busiInsuranceMap != null){
            busiInsurance = new ArrayList<String>();
            for(int i = 0 ; i < busiInsuranceMap.size() ; i++){
                busiInsurance.add(busiInsuranceMap.get(i));
            }
        }
        if(busiInsurance != null && busiInsurance.size() > 0){
            newResourceList.addAll(busiInsurance);
            for(String str : busiInsurance){
                map.put(str, ResourceType.BUSI_INSURANCE);
            }
        }
        deleteResourceList.addAll(oldResourceList);
        deleteResourceList.removeAll(newResourceList);

        //???????????????
        for(RequisitionDetailResource resource : oldList){
            if(!deleteResourceList.contains(resource.getResourcePicture())){
                remainResourceSet.add(resource);
                remainResourceKeys.add(resource.getResourcePicture());
            }
        }

        deleteQiniuFile(deleteResourceList);
        List<RequisitionDetailResource> waitingForDeleteList = new ArrayList<RequisitionDetailResource>();
        for(RequisitionDetailResource resource:oldList){
            if(deleteResourceList.contains(resource.getResourcePicture())){
                waitingForDeleteList.add(resource);
            }
        }
        if(waitingForDeleteList != null && waitingForDeleteList.size() > 0) {
            requisitionDetailResourceService.deleteRequisitionDetailResource(waitingForDeleteList);
        }
        addResourceList.addAll(newResourceList);
        addResourceList.removeAll(oldResourceList);
        Set<RequisitionDetailResource> addResources = new HashSet<RequisitionDetailResource>();
        addResources.addAll(remainResourceSet);
        if(addResourceList != null && addResourceList.size() > 0){
            int i = remainResourceSet.size();
            for(String str:addResourceList){
                if(remainResourceKeys.contains(str)){
                    continue;
                }
                i++;
                RequisitionDetailResource detailResource = new RequisitionDetailResource();
                ResourceType type = (ResourceType)map.get(str);
                detailResource.setResourceType(type.getCode());
                detailResource.setResouceName(type.getDesc());
                detailResource.setDisplaySequence(i);
                detailResource.setResourcePicture(str);
                detailResource.setRequisitionDetail(requisitionDetail);
                requisitionDetailResourceService.saveRequisitionDetailResource(detailResource);
                addResources.add(detailResource);
            }
        }
        return addResources;
    }

    //??????????????????
    private void deleteQiniuFile(List<String> list){
            if(list != null && list.size() >0 ){
                for(String str : list){
                    try {
                        qiniuBusinessServiceFeign.deleteFile(str, 1);
                    } catch (Exception e) {
                        LOG.error("deleteQiniuFile failed with key=[" + str + "]", e);
                    }
                }
            }

    }

    private RequisitionDetailInfoVO convertToVOByEntity(RequisitionDetail entity){
        if(entity == null){
            return null;
        }
        //???????????????
        List<String> drivingLicenseResource = new ArrayList<String>();
        //????????????????????????
        List<String> busiInsurance = new ArrayList<String>();
        //???????????????
        List<String> driveInsurance = new ArrayList<String>();
        //????????????
        List<String> otherMaterial = new ArrayList<String>();
        //?????????
        List<String> taxResource = new ArrayList<String>();

        List<RequisitionDetailResource> requisitionDetailResources = requisitionDetailResourceService.listRequisitionDetailResource(entity.getId(), null);
       if(requisitionDetailResources != null && requisitionDetailResources.size() > 0){
           for(RequisitionDetailResource resource : requisitionDetailResources){
               String resoucrePicture = resource.getResourcePicture();
               if(StringUtils.equals(resource.getResourceType(),ResourceType.DRIVING_LICENSE.getCode())){
                   drivingLicenseResource.add(resoucrePicture);
               }else if(StringUtils.equals(resource.getResourceType(),ResourceType.BUSI_INSURANCE.getCode())){
                   busiInsurance.add(resoucrePicture);
               }else if(StringUtils.equals(resource.getResourceType(),ResourceType.DRIVER_INSURANCE.getCode())){
                   driveInsurance.add(resoucrePicture);
               }else if(StringUtils.equals(resource.getResourceType(),ResourceType.OTHER_MATERIAL.getCode())){
                   otherMaterial.add(resoucrePicture);
               }else if(StringUtils.equals(resource.getResourceType(),ResourceType.TAX_INSURANCE.getCode())){
                   taxResource.add(resoucrePicture);
               }
           }
       }

        RequisitionDetailInfoVO vo = new RequisitionDetailInfoVO();
        //??????id
        vo.setId(entity.getId());
        //?????????
        vo.setCarNumber(entity.getCarNumber());
        //????????????
        vo.setDrivingLicense(entity.getDrivingLicense());
        //???????????????
        vo.setDrivingLicenseResource(drivingLicenseResource);
        //????????????
        vo.setOtherMaterial(otherMaterial);

        //??????????????????
        vo.setCommercialInsuranceNumber(entity.getCommercialInsuranceNumber());
        //??????????????????
        vo.setCommercialInsuranceAmount(entity.getCommercialInsuranceAmount().doubleValue());
        //?????????????????????
        vo.setCommercialInsuranceStart(entity.getCommercialInsuranceStart());
        //?????????????????????
        vo.setCommercialInsuranceEnd(entity.getCommercialInsuranceEnd());

        //????????????????????????
        vo.setBusiInsurance(busiInsurance);
        //???????????????
        vo.setCompulsoryInsuranceNumber(entity.getCompulsoryInsuranceNumber());
        //???????????????
        vo.setCommercialInsuranceAmount(entity.getCommercialInsuranceAmount().doubleValue());
        //??????????????????
        vo.setCompulsoryInsuranceStart(entity.getCompulsoryInsuranceStart());
        //??????????????????
        vo.setCompulsoryInsuranceEnd(entity.getCompulsoryInsuranceEnd());

        //???????????????
        vo.setDriveInsurance(driveInsurance);

        //?????????
        vo.setTaxResource(taxResource);

        //??????id
        vo.setRequisitionId(entity.getRequisition().getId());
        //???????????????
        vo.setCompulsoryInsuranceAmount(entity.getCompulsoryInsuranceAmount().doubleValue());
        //?????????
        vo.setTaxAmount(entity.getTaxAmount().doubleValue());
        return vo;
    }

    private SimpleRequisitionDetailVO convertToSimpleRequisitionDetailByEntity(RequisitionDetail entity){
        if(entity == null){
            return null;
        }
        SimpleRequisitionDetailVO vo = new SimpleRequisitionDetailVO();
        //??????id
        vo.setId(entity.getId());
        //?????????
        vo.setCarNumber(entity.getCarNumber());
        //?????????
        vo.setInsuranceNumber(StringUtils.isBlank(entity.getCompulsoryInsuranceNumber()) ? 1 : 2);
        Requisition requisition = entity.getRequisition();
        ProductType productType = ProductType.codeOf(requisition.getProductType());

        Date beginDate = entity.getCommercialInsuranceStart().before(DateCommonUtils.getToday()) ? DateCommonUtils.getToday() : entity.getCommercialInsuranceStart();

        //????????????
        Integer maxDuration = CalculationFormulaUtils.getBusinessDuration(beginDate, entity.getCommercialInsuranceEnd(), productType);
        //????????????????????????
        if(ProductType.CAR_INSTALMENTS == productType){//?????????????????????????????????????????????
            if(maxDuration == null || maxDuration < BasicConstants.REQUSITION_YEAR_DAYS || !entity.getCommercialInsuranceStart().after(DateCommonUtils.getToday()) ||
                    (requisition.getBusinessDuration() !=null && requisition.getBusinessDuration() != 0 && maxDuration < requisition.getBusinessDuration())){
                vo.setIsCanUse(0);
            }else{
                vo.setIsCanUse(1);
            }
        }else{
            if(maxDuration < BasicConstants.REQUSITION_MIN_MONTH){
                vo.setIsCanUse(0);
            }else{
                vo.setIsCanUse(1);
            }
        }
        return  vo;
    }

}
