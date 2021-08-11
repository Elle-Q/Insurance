package com.fintech.insurance.micro.thirdparty.service.sms;

import com.fintech.insurance.micro.dto.thirdparty.sms.SMSQueryDetailsRequestVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSQueryDetailsResponseVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendRequestVO;
import com.fintech.insurance.micro.dto.thirdparty.sms.SMSSendResponseVO;

/**
 * @Description: 短信服务接口
 * @Author: East
 * @Date: 2017/11/13 0013 16:50
 */

public interface SMSService {

    /**
     * 发送短信
     *
     * @param requestVO 短信发送请求
     * @return 短信发送结果
     */
    SMSSendResponseVO sendSMS(SMSSendRequestVO requestVO);

    /**
     * 查询短信
     *
     * @param requestVO 短信查询号码
     * @return 短信查询详情
     */
    SMSQueryDetailsResponseVO querySMS(SMSQueryDetailsRequestVO requestVO);

}
