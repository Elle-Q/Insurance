package com.fintech.insurance.micro.thirdparty.controller;


import com.fintech.insurance.commons.web.FintechResponse;
import com.fintech.insurance.components.web.BaseFintechController;
import com.fintech.insurance.micro.api.thirdparty.ImageVercodeServiceAPI;
import com.fintech.insurance.micro.dto.thirdparty.ImageVercodeVO;
import com.fintech.insurance.micro.thirdparty.service.VercodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Description: 图片验证码服务接口
 * @Author: Yong Li
 * @Date: 2017/11/11 14:05
 */
@RestController
public class VercodeController extends BaseFintechController implements ImageVercodeServiceAPI {

    @Autowired
    VercodeService vercodeService;

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;


    @Override
    public FintechResponse<ImageVercodeVO> getVercode() throws IOException {
        return FintechResponse.responseData(vercodeService.getVercode(4, true));

    }

    @Override
    public FintechResponse<Integer> checkVercode(@RequestParam(name = "vercodeId") String vercodeId,
                                                 @RequestParam(name = "content") String content) {
        return FintechResponse.responseData(vercodeService.verify(vercodeId, content) ? 1 : 0);
    }
}
