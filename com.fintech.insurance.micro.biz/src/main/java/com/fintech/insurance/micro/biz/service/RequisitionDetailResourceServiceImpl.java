package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.enums.ResourceType;
import com.fintech.insurance.micro.biz.persist.dao.RequisitionDetailResourceDao;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetail;
import com.fintech.insurance.micro.biz.persist.entity.RequisitionDetailResource;
import com.fintech.insurance.micro.dto.biz.RequisitionDetailResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: yongneng liu
 * @Date: 2017/11/14 0014 16:11
 */
@Service
@Transactional
public class RequisitionDetailResourceServiceImpl implements RequisitionDetailResourceService {

    @Autowired
    private RequisitionDetailResourceDao requisitionDetailResourceDao;

    @Override
    public RequisitionDetailResourceVO getRequisitionDetailResourceById(Integer id) {
        return null;
    }

    @Override
    public List<RequisitionDetailResource> listRequisitionDetailResource(Integer requisitionDetailId, ResourceType resourceType) {
        return requisitionDetailResourceDao.listRequisitionDetailResource(requisitionDetailId, resourceType);
    }

    @Override
    public void saveRequisitionDetailResource(RequisitionDetail requisitionDetail, List<RequisitionDetailResourceVO> resourceVOList) {
        List<RequisitionDetailResource> list = new ArrayList<RequisitionDetailResource>();
        for(RequisitionDetailResourceVO vo : resourceVOList){
            RequisitionDetailResource resource = new RequisitionDetailResource();
            resource.setRequisitionDetail(requisitionDetail);
            //资源类型'")
            resource.setResourceType(vo.getResourceType());
            //资源名称，对应为保单号码或者车船税
            resource.setResouceName(vo.getResouceName());
            //资源图片
            resource.setResourcePicture(vo.getResourcePicture());
            //排序号
            resource.setDisplaySequence(vo.getDisplaySequence());
            resource.setCreateAt(new Date());
            resource.setUpdateAt(new Date());
            list.add(resource);
        }
        requisitionDetailResourceDao.save(list);
    }

    @Override
    public void deleteRequisitionDetailResource(List<RequisitionDetailResource> resourceList) {
        if(resourceList != null && resourceList.size() > 0) {
            for(RequisitionDetailResource resource : resourceList) {
                requisitionDetailResourceDao.delete(resource);
            }
        }
    }

    @Override
    public void saveRequisitionDetailResource(RequisitionDetailResource requisitionDetailResource) {
        requisitionDetailResourceDao.save(requisitionDetailResource);
    }
}
