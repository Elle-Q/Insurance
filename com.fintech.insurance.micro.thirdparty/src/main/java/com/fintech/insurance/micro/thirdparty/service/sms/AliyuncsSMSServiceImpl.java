package com.fintech.insurance.micro.thirdparty.service.sms;

import com.alibaba.fastjson.JSON;
import com.fintech.insurance.commons.enums.NotificationEvent;
import com.fintech.insurance.components.cache.RedisSequenceFactory;
import com.fintech.insurance.micro.dto.thirdparty.sms.*;
import com.fintech.insurance.micro.thirdparty.model.sms.QuerySendDetailsRequest;
import com.fintech.insurance.micro.thirdparty.model.sms.QuerySendDetailsResponse;
import com.fintech.insurance.micro.thirdparty.model.sms.SendSmsRequest;
import com.fintech.insurance.micro.thirdparty.model.sms.SendSmsResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description: Aliyun短信服务
 * @Author: East
 * @Date: 2017/11/13 0013 16:55
 */
@Service
public class AliyuncsSMSServiceImpl extends AbstractAliyuncsService implements SMSService {

    private static final Logger LOG = LoggerFactory.getLogger(AliyuncsSMSServiceImpl.class);

    private static final List<NotificationEvent> SMS_VERIFICATION_EVENTS = new ArrayList<NotificationEvent>();

    static {
        SMS_VERIFICATION_EVENTS.add(NotificationEvent.DEFAULT_VERIFICATION);
        SMS_VERIFICATION_EVENTS.add(NotificationEvent.WX_CHANNEL_LOGIN_AUTH);
        SMS_VERIFICATION_EVENTS.add(NotificationEvent.WX_CUSTOMER_LOGIN_AUTH);
        SMS_VERIFICATION_EVENTS.add(NotificationEvent.BIND_CARD);
    }

    private static final String SUCCESS_CODE = "OK";

    @Override
    public SMSSendResponseVO sendSMS(SMSSendRequestVO requestVO) {

        if (requestVO == null) {
            throw new IllegalArgumentException("Null requestVO");
        }
        if (requestVO.getPhoneNumbers().length < 1) {
            throw new IllegalArgumentException("Null ro none phoneNumbers in requestVO!");
        }
        if (requestVO.getEvent() == null || !this.isSupportedEvent(requestVO.getEvent())) {
            throw new IllegalArgumentException("Null or unsupported event in requestVO!");
        }

        SendSmsRequest request = this.buildSendSmsRequest(requestVO);
        try {
            LOG.error("Send SMS for request [{}] with phoneNumbers [{}].", request, request.getPhoneNumbers());
            SendSmsResponse response = this.getAcsClient().getAcsResponse(request);
            LOG.error("Receive SMS response [{}] with phoneNumbers [{}].", response, request.getPhoneNumbers());

            if (response != null) {
                return this.buildSMSSendResponseVO(request, response);
            } else {
                throw new IllegalStateException("Send SMS failed with null response!");
            }
        } catch (Exception e) {
            LOG.error("Send SMS failed with request [" + requestVO + "]", e);

            return this.buildSMSSendResponseVO(request, e);
        }
    }

    private SMSSendResponseVO buildSMSSendResponseVO(SendSmsRequest request, SendSmsResponse response) {

        SMSSendResponseVO responseVO = new SMSSendResponseVO();

        responseVO.setSequenceId(request.getOutId());

        responseVO.setBizId(response.getBizId());

        if (SUCCESS_CODE.equals(response.getCode())) {
            responseVO.setCode(SMSSendResponseVO.SUCCESS_CODE);
            responseVO.setMessage(response.getMessage());
        } else {
            responseVO.setCode(response.getCode());
            responseVO.setMessage(response.getMessage());
        }

        return responseVO;
    }

    private SMSSendResponseVO buildSMSSendResponseVO(SendSmsRequest request, Exception e) {
        SMSSendResponseVO responseVO = new SMSSendResponseVO();

        responseVO.setSequenceId(request.getOutId());
        responseVO.setCode(SMSSendResponseVO.FAILURE_CODE);
        responseVO.setMessage(e.getMessage());

        return responseVO;
    }

    private SendSmsRequest buildSendSmsRequest(SMSSendRequestVO requestVO) {

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(StringUtils.join(requestVO.getPhoneNumbers(), ","));
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(this.getSignName());
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(this.getEventTemplateCode(requestVO.getEvent()));
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        if (requestVO.getTemplateParam() != null && !requestVO.getTemplateParam().isEmpty()) {
            request.setTemplateParam(JSON.toJSONString(requestVO.getTemplateParam()));
        }
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        if (!StringUtils.isBlank(requestVO.getUpExtendCode())) {
            request.setSmsUpExtendCode(requestVO.getUpExtendCode());
        }
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //用于短信验证码缓存中的key,普通的短信发送可选
        if (SMS_VERIFICATION_EVENTS.contains(requestVO.getEvent())) {
            request.setOutId(requestVO.getPhoneNumbers()[0]+requestVO.getEvent().getCode());
        }
        return request;
    }

    private String buildSequenceId() {
        String uuid = UUID.randomUUID().toString();
        return StringUtils.remove(uuid, "-").toUpperCase();
    }


    @Override
    public SMSQueryDetailsResponseVO querySMS(SMSQueryDetailsRequestVO requestVO) {

        if (requestVO == null) {
            throw new IllegalArgumentException("Null requestVO");
        }
        if (StringUtils.isBlank(requestVO.getPhoneNumber())) {
            throw new IllegalArgumentException("Null phoneNumbers in requestVO!");
        }

        QuerySendDetailsRequest request = this.buildQuerySendDetailsRequest(requestVO);
        try {
            LOG.error("Query SMS for request [{}] with phoneNumber [{}].", request, request.getPhoneNumber());
            QuerySendDetailsResponse response = this.getAcsClient().getAcsResponse(request);
            LOG.error("Receive response [{}] for Query SMS with phoneNumber [{}].", response, requestVO.getPhoneNumber());

            if (response != null) {
                return this.buildSMSQueryDetailsResponseVO(request, response);
            } else {
                throw new IllegalStateException("Query SMS faild with null response!");
            }
        } catch (Exception e) {
            LOG.error("Query SMS faild with request [" + requestVO + "]", e);

            return this.buildSMSQueryDetailsResponseVO(request, e);
        }
    }

    private SMSQueryDetailsResponseVO buildSMSQueryDetailsResponseVO(QuerySendDetailsRequest request, Exception e) {
        SMSQueryDetailsResponseVO responseVO = new SMSQueryDetailsResponseVO();

        responseVO.setCode(SMSSendResponseVO.FAILURE_CODE);
        responseVO.setMessage(e.getMessage());
        responseVO.setPhoneNumber(request.getPhoneNumber());
        responseVO.setBizId(request.getBizId());

        return responseVO;
    }

    private SMSQueryDetailsResponseVO buildSMSQueryDetailsResponseVO(QuerySendDetailsRequest request, QuerySendDetailsResponse response) {

        SMSQueryDetailsResponseVO responseVO = new SMSQueryDetailsResponseVO();
        responseVO.setPhoneNumber(request.getPhoneNumber());
        responseVO.setBizId(request.getBizId());

        if (SUCCESS_CODE.equals(response.getCode())) {
            responseVO.setCode(SMSSendResponseVO.SUCCESS_CODE);
            responseVO.setMessage(response.getMessage());
            long totalCount = NumberUtils.toLong(response.getTotalCount());
            responseVO.setCurrentPage(request.getCurrentPage());
            responseVO.setTotalCount(totalCount);
            responseVO.setTotalPage(totalCount % request.getPageSize() > 0 ? totalCount / request.getPageSize() + 1 : totalCount / request.getPageSize());
            if (response.getSmsSendDetailDTOs() != null && !response.getSmsSendDetailDTOs().isEmpty()) {
                responseVO.setSendDetailVOList(this.buildSMSSendDetailVO(response.getSmsSendDetailDTOs()));
            }
        } else {
            responseVO.setCode(response.getCode());
            response.setMessage(response.getMessage());
        }

        return responseVO;
    }

    private List<SMSSendDetailVO> buildSMSSendDetailVO(List<QuerySendDetailsResponse.SmsSendDetailDTO> smsSendDetailDTOList) {

        List<SMSSendDetailVO> detailVOList = new ArrayList<SMSSendDetailVO>();
        for (QuerySendDetailsResponse.SmsSendDetailDTO detailDTO : smsSendDetailDTOList) {
            SMSSendDetailVO detailVO = new SMSSendDetailVO();
            detailVO.setPhoneNum(detailDTO.getPhoneNum());
            detailVO.setSendStatus(detailDTO.getSendStatus());
            detailVO.setErrCode(detailDTO.getErrCode());
            if (detailDTO.getSendDate() != null) {
                detailVO.setSendDate(DateTime.parse(detailDTO.getSendDate(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
            }
            if (detailDTO.getReceiveDate() != null) {
                detailVO.setReceiveDate(DateTime.parse(detailDTO.getReceiveDate(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
            }
            detailVO.setSequenceId(detailDTO.getOutId());
        }

        return detailVOList;
    }

    private QuerySendDetailsRequest buildQuerySendDetailsRequest(SMSQueryDetailsRequestVO requestVO) {

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(requestVO.getPhoneNumber());
        //可选-流水号
        if (!StringUtils.isBlank(requestVO.getBizId())) {
            request.setBizId(requestVO.getBizId());
        }
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        DateTime sendDate = DateTime.now();
        if (requestVO.getSendDate() != null) {
            sendDate = new DateTime(requestVO.getSendDate());
        }
        request.setSendDate(sendDate.toString("yyyyMMdd"));
        //必填-页大小
        long pageSize = 10L;
        if (requestVO.getPageSize() > 0) {
            pageSize = requestVO.getPageSize();
        }
        request.setPageSize(pageSize);
        //必填-当前页码从1开始计数
        long currentPage = 1L;
        if (requestVO.getCurrentPage() > 0) {
            currentPage = requestVO.getCurrentPage();
        }
        request.setCurrentPage(currentPage);

        return request;
    }
}
