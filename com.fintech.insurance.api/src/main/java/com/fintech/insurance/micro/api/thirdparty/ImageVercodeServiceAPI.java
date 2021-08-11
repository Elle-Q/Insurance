package com.fintech.insurance.micro.api.thirdparty;

import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.micro.dto.thirdparty.ImageVercodeVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * @Description: 随机(图形)验证码服务
 * @Author: Yong Li
 * @Date: 2017/11/11 10:25
 */
@RequestMapping(value = "/thirdparty/vercode")
public interface ImageVercodeServiceAPI {

    /**
     * 获取校验码
     *
     * @return 校验码随机生成的图片
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    FintechResponse<ImageVercodeVO> getVercode() throws IOException;


    /**
     * 校验验证码是否正确
     *
     * @param vercodeId 图片验证码标识
     * @param content 用户填写的验证码内容
     * @return 正确与否，如果验证码过期也返回false
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    FintechResponse<Integer> checkVercode(@RequestParam(name = "vercodeId") String vercodeId,
                                          @RequestParam(name = "content") String content);

}
