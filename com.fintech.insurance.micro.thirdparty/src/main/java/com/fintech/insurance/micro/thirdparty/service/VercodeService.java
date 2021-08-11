package com.fintech.insurance.micro.thirdparty.service;

import com.fintech.insurance.micro.dto.thirdparty.ImageVercodeVO;

import java.io.IOException;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/13 21:53
 */

public interface VercodeService {

    /**
     * 根据指定的验证码长度以及算法难度生成图片验证码
     *
     * @param vercodeLength     验证码长度
     * @param isSimpleAlgothrim 是否使用简单算法生成
     * @return
     */
    ImageVercodeVO getVercode(int vercodeLength, boolean isSimpleAlgothrim) throws IOException;

    /**
     * 验证识别出的内容是否匹配验证码标识符所代表的内容
     *
     * @param vercodeId
     * @param possibleResult
     * @return
     */
    boolean verify(String vercodeId, String possibleResult);
}
