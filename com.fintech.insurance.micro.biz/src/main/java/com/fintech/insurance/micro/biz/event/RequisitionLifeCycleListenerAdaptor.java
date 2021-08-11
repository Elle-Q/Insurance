package com.fintech.insurance.micro.biz.event;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.commons.beans.TemplateMessageConfigBean;
import com.fintech.insurance.commons.beans.WeixinConfigBean;
import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.common.TemplateMessageVO;
import com.fintech.insurance.micro.feign.customer.CustomerServiceFeign;
import com.fintech.insurance.micro.feign.system.SysUserServiceFeign;
import com.fintech.insurance.micro.feign.thirdparty.weixin.WeixinBizServiceFeign;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 申请单生命周期监听器适配器类
 */
@Component
public class RequisitionLifeCycleListenerAdaptor extends AbstractRequisitionLifeCycleListener {

    private static final Logger logger = LoggerFactory.getLogger(RequisitionLifeCycleListenerAdaptor.class);

    protected DateFormat dateTimeFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

    @Autowired
    protected WeixinBizServiceFeign weixinBizServiceClient;

    @Autowired
    protected SysUserServiceFeign sysUserServiceClient;

    @Autowired
    protected CustomerServiceFeign customerServiceClient;

    @Autowired
    protected WeixinConfigBean customerWxConfigBean;

    @Autowired
    protected WeixinConfigBean clientWxConfigBean;

    @Autowired
    protected TemplateMessageConfigBean templateMessageConfigBean;

    @Override
    boolean canProcess(RequisitionLifeCycleEvent event) {
        return true;
    }

    /**
     * 获取客户的openid
     * @param customerId
     * @return
     */
    private String getCustomerOpenidById(Integer customerId) {
        FintechResponse<String> openidResponse = this.customerServiceClient.getCustomerWxOpenid(customerId, this.customerWxConfigBean.getAppid());
        if (openidResponse != null && openidResponse.isOk() && StringUtils.isNotEmpty(openidResponse.getData())) {
            return openidResponse.getData();
        } else {
            return "";
        }
    }

    /**
     * 获取渠道用户的openid
     * @param userId
     * @return
     */
    private String getUserOpenidById(Integer userId) {
        FintechResponse<String> openidResponse = this.sysUserServiceClient.getUserWxOpenid(userId, this.clientWxConfigBean.getAppid());
        if (openidResponse != null && openidResponse.isOk() && StringUtils.isNotEmpty(openidResponse.getData())) {
            return openidResponse.getData();
        } else {
            return "";
        }
    }

    /**
     * 发送微信模版消息
     * @param customerId
     * @param userId
     * @param appid
     * @param templateString
     * @param url
     * @param templateData
     */
    protected void sendTemplateMessage(Integer customerId, Integer userId, String appid, String templateString, String url, List<WxMpTemplateData> templateData) {
        if ((customerId == null && userId == null) || StringUtils.isEmpty(appid) || StringUtils.isEmpty(templateString)) {
            logger.info("Cannot send the template message due to the empty user info or empty appid info [{}] or empty template string [{}]", appid, templateString);
            return ;
        }
        if (templateData == null || templateData.size() == 0) {
            logger.info("Cannot send the template message due to the empty message content");
            return ;
        }
        String openid = "";
        if (customerId != null) {
            openid = this.getCustomerOpenidById(customerId);
        } else {
            openid = this.getUserOpenidById(userId);
        }
        if (StringUtils.isEmpty(openid)) {
            logger.info("Cannot find the openid for customer with id " + customerId + " or user with id " + userId + " with appid " + appid);
            return ;
        }
        TemplateMessageVO templateMessageVO = new TemplateMessageVO();
        templateMessageVO.setAppid(appid);
        templateMessageVO.setOpenid(openid);
        templateMessageVO.setTemplateString(templateString);
        templateMessageVO.setUrl(url);
        templateMessageVO.setTemplateData(templateData);
        FintechResponse<String> sendResult = this.weixinBizServiceClient.sendTemplateMessage(templateMessageVO);
        logger.info("Following message has been sent to user: \n{}", JSON.toJSONString(templateMessageVO));
        logger.info("The send result is:\n {}", JSON.toJSONString(sendResult));
    }

    @Override
    void onRequsitionCreated(RequisitionLifeCycleEvent event) {
        logger.info("default requisition lifecycle listener will do nothing");
    }

    @Override
    void onRequisitionSubmitted(RequisitionLifeCycleEvent event) {
        logger.info("default requisition lifecycle listener will do nothing");
    }

    @Override
    void onRequisitionAuditing(RequisitionLifeCycleEvent event) {
        logger.info("default requisition lifecycle listener will do nothing");
    }

    @Override
    void onRequisitionWaitPayment(RequisitionLifeCycleEvent event) {
        logger.info("default requisition lifecycle listener will do nothing");
    }

    @Override
    void onRequisitionFailPayment(RequisitionLifeCycleEvent event) {
        logger.info("default requisition lifecycle listener will do nothing");
    }

    @Override
    void onRequisitionRejected(RequisitionLifeCycleEvent event) {
        logger.info("default requisition lifecycle listener will do nothing");
    }

    @Override
    void onRequisitionCancelled(RequisitionLifeCycleEvent event) {
        logger.info("default requisition lifecycle listener will do nothing");
    }

    @Override
    void onRequisitionWaitLoan(RequisitionLifeCycleEvent event) {
        logger.info("default requisition lifecycle listener will do nothing");
    }

    @Override
    void onRequisitionLoaned(RequisitionLifeCycleEvent event) {
        logger.info("default requisition lifecycle listener will do nothing");
    }
}
