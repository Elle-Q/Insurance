package com.fintech.insurance.micro.biz.service;

import com.fintech.insurance.commons.enums.UserType;
import com.fintech.insurance.commons.utils.Pagination;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.exception.FInsuranceBaseException;
import com.fintech.insurance.micro.biz.persist.dao.ChannelDao;
import com.fintech.insurance.micro.biz.persist.dao.ProductDao;
import com.fintech.insurance.micro.biz.persist.entity.Channel;
import com.fintech.insurance.micro.biz.persist.entity.Product;
import com.fintech.insurance.micro.dto.biz.ChannelVO;
import com.fintech.insurance.micro.dto.biz.ProductChannelVO;
import com.fintech.insurance.micro.dto.biz.RequisitionVO;
import com.fintech.insurance.micro.dto.support.OrganizationVO;
import com.fintech.insurance.micro.dto.system.UserVO;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.support.OrganizationServiceFeign;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @Description: (some words)
 * @Author: qxy
 * @Date: 2017/11/14 0014 16:06
 */
@Service
@Transactional
public class ChannelServiceImpl implements ChannelService {

    private static final Logger LOG = LoggerFactory.getLogger(ChannelServiceImpl.class);

    //渠道号返回，渠道号长度为3
    private static final String CHANNEL_CODE_FORMAT = "000";

    @Autowired
    ChannelDao channelDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private RequisitionService requisitionService;

    @Autowired
    private SysUserServiceFeign sysUserServiceFeign;

    @Autowired
    private OrganizationServiceFeign organizationServiceFeign;

    @Autowired
    private CustomerServiceFeign customerServiceFeign;


    @Override
    public List<Channel> findChannelVOByIds(List<Integer> ids) {
        return channelDao.findChannelVOByIds(ids);
    }

    @Override
    public Channel saveChannel(ChannelVO channelVO, Integer currentLoginUserID) {

        UserVO userVO = null;
        Channel channel = null;
        boolean isCreateChannel = false;

        if (channelVO.getId() != null) {
            channel = this.channelDao.getById(channelVO.getId());
            channel.setUpdateAt(new Date());
            channel.setUpdateBy(currentLoginUserID);
            if (channel != null) {
                userVO = sysUserServiceFeign.getChannelAdminByCode(channel.getChannelCode());
                userVO.setUpdateAt(new Date());
                userVO.setUpdateBy(currentLoginUserID);
            }
        } else {
            channel = new Channel();
            channel.setCreateAt(new Date());
            channel.setCreateBy(currentLoginUserID);
            channel.setChannelCode(CHANNEL_CODE_FORMAT);
            channel.setLocked(false);
            userVO = new UserVO();
            userVO.setCrateAt(new Date());
            userVO.setCreateBy(currentLoginUserID);
            isCreateChannel = true;
        }
        //验证渠道名称是否重复
        if (!StringUtils.isEmpty(channelVO.getChannelName())) {
            List<Channel> preChannels = channelDao.getByChannelNameLike(channelVO.getChannelName(), null == channel.getId() ? 0 : channel.getId());
            if (channelVO.getId() == null && preChannels.size() > 0 || (channelVO.getId() != null && preChannels.size() > 0)) {
                LOG.error("the ChannelName with [" + channelVO.getChannelName() + "]" + "has already been used");
                throw new FInsuranceBaseException(104501);
            }
        }
        //验证渠道营业执照号是否重复
        if (!StringUtils.isEmpty(channelVO.getBusinessLicence())) {
            List<Channel> preChannels = channelDao.getByBusinessLicenceLike(channelVO.getBusinessLicence(), null == channel.getId() ? 0 : channel.getId());
            if (channelVO.getId() == null && preChannels.size() > 0 || (channelVO.getId() != null && preChannels.size() > 0)) {
                LOG.error("the BusinessLicence with [" + channelVO.getBusinessLicence() + "]" + "has already been used");
                throw new FInsuranceBaseException(104502);
            }
        }
        //验证手机号是否存在
        if (!StringUtils.isEmpty(channelVO.getMobile())) {
            UserVO preUserVO = sysUserServiceFeign.getUserByMobile(channelVO.getMobile());
            if ((userVO.getId() == null && preUserVO != null) || (userVO.getId() != null && preUserVO != null && !userVO.getId().equals(preUserVO.getId()))) {
                LOG.error("the Mobile with [" + channelVO.getMobile() + "]" + "has already been used");
                throw new FInsuranceBaseException(104503);
            }
        }
        channel.setChannelName(channelVO.getChannelName());
        channel.setBusinessLicence(channelVO.getBusinessLicence());
        channel.setAreaCode(channelVO.getAreaCode());
        channel.setOrganizationId(channelVO.getCompanyId());

        channelDao.save(channel);// 先用000保存一下，获取自增的ID再来更新渠道代码
        if (isCreateChannel) {
            channel.setChannelCode(generateChannelCode(channel.getId()));//生成渠道编号
        }
        channelDao.save(channel);//保存渠道

        //创建渠道同时创建渠道账户，更新渠道同时更新渠道账户信息
        userVO.setMobile(channelVO.getMobile());
        userVO.setName(channelVO.getChannelName());
        userVO.setChannelAdmin(true);
        userVO.setChannelCode(channel.getChannelCode());
        userVO.setUserType(UserType.CHANNEL.getCode());
        sysUserServiceFeign.saveUser(userVO);
        return channel;
    }

    public static String generateChannelCode(Integer id){
        if (id == null || id < 1) {
            return CHANNEL_CODE_FORMAT;
        } else {
            if (id > 999) {
                return String.valueOf(id);
            } else {
                DecimalFormat df = new DecimalFormat(CHANNEL_CODE_FORMAT);
                return df.format(id);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<ChannelVO> queryChannel(String channelCode, String channelName, List<Integer> companyIds, List<String> channelCodes,
                                              Integer pageIndex, Integer pageSize) {
        Page<Channel> channelPage = channelDao.queryChannel(channelCode, channelName, companyIds, channelCodes, pageIndex, pageSize);
        List<ChannelVO> channelVOList = new ArrayList<>();
        if(channelPage != null && channelPage.getContent() != null && channelPage.getContent().size() > 0) {
            for (Channel channel : channelPage.getContent()) {
                ChannelVO channelVO = getVOByEntity(channel, null, null);//转VO
                channelVOList.add(channelVO);
            }
        }
        if (channelPage.getContent().size() < 1) {//无数据返回emptyList
            channelVOList = Collections.emptyList();
        }
        return Pagination.createInstance(pageIndex, pageSize, channelPage.getTotalElements(), channelVOList);
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelVO getDetail(Integer id) {
        if (id == null) {
            return null;
        }
        Channel channel = channelDao.getById(id);
        if (null == channel) {
            LOG.error("no channel with id[" + id + "]");
            throw new FInsuranceBaseException(104105);
        }
        UserVO userVO = sysUserServiceFeign.getChannelAdminByCode(channel.getChannelCode());//查询渠道管理员
        FintechResponse<OrganizationVO> organizationVOResponse = organizationServiceFeign.getById(channel.getOrganizationId());
        if (!organizationVOResponse.isOk()) {
            throw FInsuranceBaseException.buildFromErrorResponse(organizationVOResponse);
        }

        return this.getVOByEntity(channel, userVO, organizationVOResponse.getData());
    }

    @Override
    public void addProductChannel(ProductChannelVO pcVO) {
        Integer productId = pcVO.getProductId();
        Integer channelId = pcVO.getChannelId();
        if(productId == null || channelId == null){
            throw new FInsuranceBaseException(104101);
        }
        Product product = productDao.getProductById(productId);
        Channel channel = channelDao.getById(channelId);
        if(product == null || channel == null){
            throw new FInsuranceBaseException(104103);
        }
        Set<Channel> channelSet = product.getChannelSet();
        if (channelSet == null) {
            channelSet = new HashSet<Channel>();
        }
        Iterator<Channel> it = channelSet.iterator();
        if(!channelSet.contains(channel)){
            channelSet.add(channel);
        }
        product.setChannelSet(channelSet);
        productDao.save(product);
    }

    @Override
    public void deleteProductChannel(ProductChannelVO pcVO) {
        Integer productId = pcVO.getProductId();
        Integer channelId = pcVO.getChannelId();
        if(productId == null || channelId == null){
            throw new FInsuranceBaseException(104101);
        }
        Product product = productDao.getProductById(productId);
        Channel channel = channelDao.getById(channelId);
        if(product == null || channel == null){
            throw new FInsuranceBaseException(104103);
        }
        Set<Channel> channelSet = product.getChannelSet();
        if (channelSet == null) {
            channelSet = new HashSet<Channel>();
        }
        Iterator<Channel> it = channelSet.iterator();
        for (int i = 0; i < channelSet.size(); i++) {
            System.out.println(i);
            Channel cc = it.next();
            if (channel.getId() == cc.getId()) {
                channelSet.remove(cc);
            }
        }
        product.setChannelSet(channelSet);
        productDao.save(product);
    }

    @Override
    public void freezeOrUnfreeze(Integer id, Boolean isLocked) {
        Channel channel = channelDao.getChannelById(id);
        if (null == channel) {
            LOG.error("null channel");
            throw new FInsuranceBaseException(104504);
        }
        channel.setLocked(isLocked);
        channel.setUpdateAt(new Date());
        channelDao.save(channel);
        FintechResponse<List<UserVO>> userVOS = sysUserServiceFeign.listChannelUserByCode(channel.getChannelCode());//查询渠道用户
        if(userVOS != null && userVOS.getData().size() > 0) {
            for (UserVO userVO : userVOS.getData()) {
                userVO.setIsLocked(isLocked ? 1 : 0);//解冻解冻渠道下的用户账号
                userVO.setUpdateAt(new Date());
                sysUserServiceFeign.saveUser(userVO);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<ChannelVO> findChannelByChannelCodes(List<String> codes, Integer pageIndex, Integer pageSize) {
        Page<Channel> page = channelDao.findChannelByChannelCodes(codes, pageIndex, pageSize);
        List<ChannelVO> voList = new ArrayList<ChannelVO>();
        if(page != null && page.getContent() != null)
        for(Channel c : page.getContent()){
            voList.add(getVOByEntity(c, null, null));
        }
        return Pagination.createInstance(pageIndex, pageSize, page.getTotalElements(), voList);
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelVO getChannelDetailByChannelCode(String channelCode) {
        Channel channel = channelDao.getChannelDetailByChannelCode(channelCode);
        if(channel == null){
            throw new FInsuranceBaseException(104504);
        }
        return getVOByEntity(channel, null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelVO getAcquiescenceChannelByRequisitionIdAndCustomerId(Integer requisitionId, Integer customerId) {
        ChannelVO channelVO = null;
        //查询上次已近使用的渠道
        RequisitionVO requisition = requisitionService.getAcquiescenceChannelByRequisitionIdAndCustomerId(requisitionId, customerId);
        if (requisition != null) {
            channelVO = this.getChannelDetailByChannelCode(requisition.getChannelCode());
        }
        //如果渠道未冻结直接返回
        if(channelVO != null && channelVO.getIsLocked().equals(0)){
            return channelVO;
        }
        //查询客户所有未冻结渠道
        FintechResponse<List<String>> listFintechResponse = customerServiceFeign.findCustomerChannelCodeByCustomerId(customerId);
        if(!listFintechResponse.isOk() ){
            LOG.error("findCustomerChannelCodeByCustomerId error listFintechResponse with customerId=[" + customerId + "]");
            throw FInsuranceBaseException.buildFromErrorResponse(listFintechResponse);
        }
        if(listFintechResponse.getData() == null || listFintechResponse.getData().size() < 1){
            LOG.error("findCustomerChannelCodeByCustomerId null listFintechResponse with customerId=[" + customerId + "]");
            throw new FInsuranceBaseException(107050);
        }
        //渠道code集合
        List<String> list = new ArrayList<String>();
        list.addAll(listFintechResponse.getData());
        if(list != null && list.size() > 0) {
            Pagination<ChannelVO> channelVOPagination = this.findChannelByChannelCodes(list, 1, Integer.MAX_VALUE);
            if (channelVOPagination != null && !channelVOPagination.getItems().isEmpty()) {
                channelVO = channelVOPagination.getItems().get(0);
            }
        }
        return channelVO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelVO> queryChannelByProductIdAndBelongFlag(Integer productId , boolean belongFlag) {
        List<Channel> list = channelDao.queryChannelByProductIdAndBelongFlag(productId, belongFlag);
        List<ChannelVO> channelVOList = new ArrayList<ChannelVO>();
        List<Integer> ids = new ArrayList<Integer>();
        if(list == null || list.size() < 1) {
            return null;
        }
        for (Channel c : list){
            if(c.getOrganizationId() != null) {
                if(!ids.contains(c.getOrganizationId())) {
                    ids.add(c.getOrganizationId());
                }
            }
        }
        FintechResponse<List<OrganizationVO>> listFintechResponse = null;
        if(ids != null && ids.size() > 0) {
            listFintechResponse = organizationServiceFeign.queryAllCompanyIds(ids);
        }
        List<OrganizationVO> organizationVOList = listFintechResponse == null ? null : listFintechResponse.getData();
        for (Channel c : list){
            channelVOList.add(getChannelVOByEntity(c, null, organizationVOList));
        }
        return channelVOList;
    }

    //实体转VO
    private ChannelVO getVOByEntity(Channel entity, UserVO userVO, OrganizationVO organizationVO){
        if (entity == null) {
            LOG.debug("null channel");
            return null;
        }
        ChannelVO vo = new ChannelVO();
        //产品名称
        vo.setChannelCode(entity.getChannelCode());
        vo.setChannelName(entity.getChannelName());
        vo.setBusinessLicence(entity.getBusinessLicence());
        vo.setAreaCode(entity.getAreaCode());
        vo.setId(entity.getId());
        if (null != userVO) {
            vo.setMobile(userVO.getMobile());
        }
        if (null != organizationVO) {
            vo.setCompanyName(organizationVO.getCompanyName());
        }
        vo.setCompanyId(entity.getOrganizationId());
        vo.setCreateAt(entity.getCreateAt());
        vo.setIsLocked(entity.getLocked() ? 1 : 0);

        return vo;
    }

    //实体转VO
    private ChannelVO getChannelVOByEntity(Channel entity, UserVO userVO, List<OrganizationVO> organizationVOList){
        if (entity == null) {
            return null;
        }
        ChannelVO vo = new ChannelVO();
        //产品名称
        vo.setChannelCode(entity.getChannelCode());
        vo.setChannelName(entity.getChannelName());
        vo.setBusinessLicence(entity.getBusinessLicence());
        vo.setAreaCode(entity.getAreaCode());
        vo.setId(entity.getId());
        if (null != userVO) {
            vo.setMobile(userVO.getMobile());
        }
        if (null != organizationVOList) {
            for(OrganizationVO organizationVO : organizationVOList) {
                if(entity.getOrganizationId() != null && organizationVO.getId().equals(entity.getOrganizationId()) ) {
                    vo.setCompanyId(organizationVO.getId());
                    vo.setCompanyName(organizationVO.getCompanyName());
                }
            }
        }
        vo.setCreateAt(entity.getCreateAt());
        vo.setIsLocked(entity.getLocked() ? 1 : 0);

        return vo;
    }
}
